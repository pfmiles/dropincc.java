package com.github.pfmiles.dropincc.impl.syntactical;

import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import com.github.pfmiles.dropincc.CC;
import com.github.pfmiles.dropincc.DropinccException;
import com.github.pfmiles.dropincc.Element;
import com.github.pfmiles.dropincc.Grule;
import com.github.pfmiles.dropincc.Lang;
import com.github.pfmiles.dropincc.Token;
import com.github.pfmiles.dropincc.Tokens;
import com.github.pfmiles.dropincc.impl.Alternative;
import com.github.pfmiles.dropincc.impl.GruleType;
import com.github.pfmiles.dropincc.testhelper.AnalyzedLangForTest;
import com.github.pfmiles.dropincc.testhelper.TestHelper;

/**
 * @author pf-miles
 * 
 */
public class ParserCompilerTest extends TestCase {

    @SuppressWarnings("unchecked")
    public void testOrSubRuleRewrite() {
        Lang calculator = new Lang();
        Token DIGIT = calculator.addToken("\\d+");
        Token ADD = calculator.addToken("\\+");
        Token SUB = calculator.addToken("\\-");
        Token MUL = calculator.addToken("\\*");
        Token DIV = calculator.addToken("/");
        Token LEFTPAREN = calculator.addToken("\\(");
        Token RIGHTPAREN = calculator.addToken("\\)");
        Grule expr = calculator.newGrule();
        Grule term = calculator.newGrule();
        Element mulTail = calculator.addGrammarRule(MUL.or(DIV), term);
        term.fillGrammarRule(DIGIT, mulTail).alt(LEFTPAREN, expr, RIGHTPAREN).alt(DIGIT);
        Element addendTail = calculator.addGrammarRule(ADD.or(SUB), term);
        expr.fillGrammarRule(term, addendTail, Tokens.EOF);

        List<Grule> grules = (List<Grule>) TestHelper.priField(calculator, "grules");
        List<Grule> genGrules = ParserCompiler.rewriteSubRules(grules);
        assertTrue(genGrules.size() == 2);
        for (Grule gg : genGrules) {
            assertTrue(gg.getAlts().size() == 2);
            for (Alternative alt : gg.getAlts()) {
                assertTrue(alt.getElements().size() == 1);
                assertTrue(alt.getElements().get(0) instanceof Token);
            }
        }
        Map<Grule, GruleType> gruleTypeMapping = ParserCompiler.buildGruleTypeMapping(grules, genGrules);
        assertTrue(gruleTypeMapping.size() == 6);
    }

    /**
     * Intended to test sub rule rewrite
     * 
     * <pre>
     * term ::= DIGIT ((MUL | DIV) term) 
     *        | LEFTPAREN expr RIGHTPAREN
     *        | DIGIT ;
     * expr ::= term ((ADD | SUB) term) EOF;
     * </pre>
     */
    @SuppressWarnings("unchecked")
    public void testSubRuleRewriteOrCascadingAnd() {
        Lang calculator = new Lang();
        Token DIGIT = calculator.addToken("\\d+");
        Token ADD = calculator.addToken("\\+");
        Token SUB = calculator.addToken("\\-");
        Token MUL = calculator.addToken("\\*");
        Token DIV = calculator.addToken("/");
        Token LEFTPAREN = calculator.addToken("\\(");
        Token RIGHTPAREN = calculator.addToken("\\)");

        Grule term = calculator.newGrule();
        Grule expr = calculator.newGrule();
        term.fillGrammarRule(DIGIT, MUL.or(DIV).and(term)).alt(LEFTPAREN, expr, RIGHTPAREN).alt(DIGIT);
        expr.fillGrammarRule(term, ADD.or(SUB).and(term), Tokens.EOF);

        List<Grule> grules = (List<Grule>) TestHelper.priField(calculator, "grules");
        List<Grule> genGrules = ParserCompiler.rewriteSubRules(grules);
        assertTrue(genGrules.size() == 4);
        Grule g1 = genGrules.get(0);
        assertTrue(g1.getAlts().size() == 1);
        assertTrue(g1.getAlts().get(0).getElements().size() == 2);
        assertTrue(g1.getAlts().get(0).getElements().get(0) instanceof Grule);
        assertTrue(g1.getAlts().get(0).getElements().get(1) instanceof Grule);

        Grule g2 = genGrules.get(1);
        assertTrue(g2.getAlts().size() == 2);
        assertTrue(g2.getAlts().get(0).getElements().size() == 1);
        assertTrue(g2.getAlts().get(0).getElements().get(0) instanceof Token);
        assertTrue(g2.getAlts().get(1).getElements().size() == 1);
        assertTrue(g2.getAlts().get(1).getElements().get(0) instanceof Token);

        Grule g3 = genGrules.get(2);
        assertTrue(g3.getAlts().size() == 1);
        assertTrue(g3.getAlts().get(0).getElements().size() == 2);
        assertTrue(g3.getAlts().get(0).getElements().get(0) instanceof Grule);
        assertTrue(g3.getAlts().get(0).getElements().get(1) instanceof Grule);

        Grule g4 = genGrules.get(3);
        assertTrue(g4.getAlts().size() == 2);
        assertTrue(g4.getAlts().get(0).getElements().size() == 1);
        assertTrue(g4.getAlts().get(0).getElements().get(0) instanceof Token);
        assertTrue(g4.getAlts().get(1).getElements().size() == 1);
        assertTrue(g4.getAlts().get(1).getElements().get(0) instanceof Token);

        Map<Grule, GruleType> gruleTypeMapping = ParserCompiler.buildGruleTypeMapping(grules, genGrules);
        assertTrue(gruleTypeMapping.size() == 6);
    }

    // TODO add a test which rewrites 'and' invocation cascading 'or'

    /**
     * direct left recursion:
     * 
     * <pre>
     * L ::= L '>' '0'
     * </pre>
     * 
     * chained left recursion:
     * 
     * <pre>
     * L ::= '(' A ')'
     *     | B ']'
     *     | '0'
     * A ::= '{' B '}'
     * B ::= L '&gt;'
     * </pre>
     * 
     * left recursion with kleene nodes:
     * 
     * <pre>
     * L ::= '(' A ')'
     *     | (B ']')*
     *     | '0'
     * A ::= '{' B '}'
     *     | (L)?
     * B ::= (A)+ '&gt;'
     * </pre>
     */
    public void testCheckAndReportLeftRecursions() {
        // direct left recursion
        Lang testLang = new Lang();
        Token gt = testLang.addToken("\\>");
        Token zero = testLang.addToken("0");
        Grule L = testLang.newGrule();
        L.fillGrammarRule(L, gt, zero);
        AnalyzedLangForTest a = TestHelper.resolveAnalyzedLangForTest(testLang);
        try {
            ParserCompiler.checkAndReportLeftRecursions(a.ruleTypeToAlts, a.kleeneTypeToNode);
            assertTrue(false);
        } catch (DropinccException e) {
            // System.out.println(e.getMessage());
            assertTrue(true);
        }

        // chained left recursion
        testLang = new Lang();
        Token leftParen = testLang.addToken("\\(");
        Token rightParen = testLang.addToken("\\)");
        Token rightBracket = testLang.addToken("\\]");
        zero = testLang.addToken("0");
        Token leftBrace = testLang.addToken("\\{");
        Token rightBrace = testLang.addToken("\\}");
        gt = testLang.addToken("\\>");
        Grule A = testLang.newGrule();
        Grule B = testLang.newGrule();
        Element l = testLang.addGrammarRule(leftParen, A, rightParen).alt(B, rightBracket).alt(zero);
        A.fillGrammarRule(leftBrace, B, rightBrace);
        B.fillGrammarRule(l, gt);
        a = TestHelper.resolveAnalyzedLangForTest(testLang);
        try {
            ParserCompiler.checkAndReportLeftRecursions(a.ruleTypeToAlts, a.kleeneTypeToNode);
            assertTrue(false);
        } catch (DropinccException e) {
            // System.out.println(e.getMessage());
            assertTrue(true);
        }

        // left recursion in kleene nodes
        testLang = new Lang();
        leftParen = testLang.addToken("\\(");
        rightParen = testLang.addToken("\\)");
        rightBracket = testLang.addToken("\\]");
        zero = testLang.addToken("0");
        leftBrace = testLang.addToken("\\{");
        rightBrace = testLang.addToken("\\}");
        gt = testLang.addToken("\\>");
        A = testLang.newGrule();
        B = testLang.newGrule();
        l = testLang.addGrammarRule(leftParen, A, rightParen).alt(CC.ks(B, rightBracket)).alt(zero);
        A.fillGrammarRule(leftBrace, B, rightBrace).alt(CC.op(l));
        B.fillGrammarRule(CC.kc(A), gt);
        a = TestHelper.resolveAnalyzedLangForTest(testLang);
        try {
            ParserCompiler.checkAndReportLeftRecursions(a.ruleTypeToAlts, a.kleeneTypeToNode);
            assertTrue(false);
        } catch (DropinccException e) {
            // System.out.println(e.getMessage());
            assertTrue(true);
        }
    }
}

/*******************************************************************************
 * Copyright (c) 2012 pf_miles.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     pf_miles - initial API and implementation
 ******************************************************************************/
package com.github.pfmiles.dropincc.impl.syntactical;

import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import com.github.pfmiles.dropincc.Action;
import com.github.pfmiles.dropincc.CC;
import com.github.pfmiles.dropincc.DropinccException;
import com.github.pfmiles.dropincc.Element;
import com.github.pfmiles.dropincc.Exe;
import com.github.pfmiles.dropincc.Grule;
import com.github.pfmiles.dropincc.Lang;
import com.github.pfmiles.dropincc.ParamedAction;
import com.github.pfmiles.dropincc.TokenDef;
import com.github.pfmiles.dropincc.impl.Alternative;
import com.github.pfmiles.dropincc.impl.GruleType;
import com.github.pfmiles.dropincc.impl.llstar.PredictingGrule;
import com.github.pfmiles.dropincc.testhelper.AnalyzedLangForTest;
import com.github.pfmiles.dropincc.testhelper.TestHelper;

/**
 * @author pf-miles
 * 
 */
public class ParserCompilerTest extends TestCase {

    @SuppressWarnings("unchecked")
    public void testOrSubRuleRewrite() {
        Lang calculator = new Lang("Test");
        TokenDef DIGIT = calculator.newToken("\\d+");
        TokenDef ADD = calculator.newToken("\\+");
        TokenDef SUB = calculator.newToken("\\-");
        TokenDef MUL = calculator.newToken("\\*");
        TokenDef DIV = calculator.newToken("/");
        TokenDef LEFTPAREN = calculator.newToken("\\(");
        TokenDef RIGHTPAREN = calculator.newToken("\\)");
        Grule expr = calculator.newGrule();
        Grule term = calculator.newGrule();
        Element mulTail = calculator.defineGrule(MUL.or(DIV), term);
        term.define(DIGIT, mulTail).alt(LEFTPAREN, expr, RIGHTPAREN).alt(DIGIT);
        Element addendTail = calculator.defineGrule(ADD.or(SUB), term);
        expr.define(term, addendTail, CC.EOF);

        List<Grule> grules = (List<Grule>) TestHelper.priField(calculator, "grules");
        List<Grule> genGrules = ParserCompiler.rewriteSubRules(grules);
        assertTrue(genGrules.size() == 2);
        for (Grule gg : genGrules) {
            assertTrue(gg.getAlts().size() == 2);
            for (Alternative alt : gg.getAlts()) {
                assertTrue(alt.getElements().size() == 1);
                assertTrue(alt.getElements().get(0) instanceof TokenDef);
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
        Lang calculator = new Lang("Test");
        TokenDef DIGIT = calculator.newToken("\\d+");
        TokenDef ADD = calculator.newToken("\\+");
        TokenDef SUB = calculator.newToken("\\-");
        TokenDef MUL = calculator.newToken("\\*");
        TokenDef DIV = calculator.newToken("/");
        TokenDef LEFTPAREN = calculator.newToken("\\(");
        TokenDef RIGHTPAREN = calculator.newToken("\\)");

        Grule term = calculator.newGrule();
        Grule expr = calculator.newGrule();
        term.define(DIGIT, MUL.or(DIV).and(term)).alt(LEFTPAREN, expr, RIGHTPAREN).alt(DIGIT);
        expr.define(term, ADD.or(SUB).and(term), CC.EOF);

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
        assertTrue(g2.getAlts().get(0).getElements().get(0) instanceof TokenDef);
        assertTrue(g2.getAlts().get(1).getElements().size() == 1);
        assertTrue(g2.getAlts().get(1).getElements().get(0) instanceof TokenDef);

        Grule g3 = genGrules.get(2);
        assertTrue(g3.getAlts().size() == 1);
        assertTrue(g3.getAlts().get(0).getElements().size() == 2);
        assertTrue(g3.getAlts().get(0).getElements().get(0) instanceof Grule);
        assertTrue(g3.getAlts().get(0).getElements().get(1) instanceof Grule);

        Grule g4 = genGrules.get(3);
        assertTrue(g4.getAlts().size() == 2);
        assertTrue(g4.getAlts().get(0).getElements().size() == 1);
        assertTrue(g4.getAlts().get(0).getElements().get(0) instanceof TokenDef);
        assertTrue(g4.getAlts().get(1).getElements().size() == 1);
        assertTrue(g4.getAlts().get(1).getElements().get(0) instanceof TokenDef);

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
        Lang testLang = new Lang("Test");
        TokenDef gt = testLang.newToken("\\>");
        TokenDef zero = testLang.newToken("0");
        Grule L = testLang.newGrule();
        L.define(L, gt, zero);
        AnalyzedLangForTest a = TestHelper.resolveAnalyzedLangForTest(testLang);
        try {
            ParserCompiler.checkAndReportLeftRecursions(a.ruleTypeToAlts, a.kleeneTypeToNode);
            assertTrue(false);
        } catch (DropinccException e) {
            // System.out.println(e.getMessage());
            assertTrue(true);
        }

        // chained left recursion
        testLang = new Lang("Test");
        TokenDef leftParen = testLang.newToken("\\(");
        TokenDef rightParen = testLang.newToken("\\)");
        TokenDef rightBracket = testLang.newToken("\\]");
        zero = testLang.newToken("0");
        TokenDef leftBrace = testLang.newToken("\\{");
        TokenDef rightBrace = testLang.newToken("\\}");
        gt = testLang.newToken("\\>");
        Grule A = testLang.newGrule();
        Grule B = testLang.newGrule();
        Element l = testLang.defineGrule(leftParen, A, rightParen).alt(B, rightBracket).alt(zero);
        A.define(leftBrace, B, rightBrace);
        B.define(l, gt);
        a = TestHelper.resolveAnalyzedLangForTest(testLang);
        try {
            ParserCompiler.checkAndReportLeftRecursions(a.ruleTypeToAlts, a.kleeneTypeToNode);
            assertTrue(false);
        } catch (DropinccException e) {
            // System.out.println(e.getMessage());
            assertTrue(true);
        }

        // left recursion in kleene nodes
        testLang = new Lang("Test");
        leftParen = testLang.newToken("\\(");
        rightParen = testLang.newToken("\\)");
        rightBracket = testLang.newToken("\\]");
        zero = testLang.newToken("0");
        leftBrace = testLang.newToken("\\{");
        rightBrace = testLang.newToken("\\}");
        gt = testLang.newToken("\\>");
        A = testLang.newGrule();
        B = testLang.newGrule();
        l = testLang.defineGrule(leftParen, A, rightParen).alt(CC.ks(B, rightBracket)).alt(zero);
        A.define(leftBrace, B, rightBrace).alt(CC.op(l));
        B.define(CC.kc(A), gt);
        a = TestHelper.resolveAnalyzedLangForTest(testLang);
        try {
            ParserCompiler.checkAndReportLeftRecursions(a.ruleTypeToAlts, a.kleeneTypeToNode);
            assertTrue(false);
        } catch (DropinccException e) {
            // System.out.println(e.getMessage());
            assertTrue(true);
        }
    }

    /**
     * Test compute predicting grule, basic LL(1) grammar
     * 
     * <pre>
     *  S ::= A $
     *  A ::= a c*
     *      | b c*
     * </pre>
     */
    public void testComputePredictingGrules() {
        Lang ll1 = new Lang("Test");
        Element a = ll1.newToken("a");
        Element b = ll1.newToken("b");
        Element c = ll1.newToken("c");
        Grule A = ll1.newGrule();
        ll1.defineGrule(A, CC.EOF);
        A.define(a, CC.ks(c)).alt(b, CC.ks(c));
        AnalyzedLangForTest al = TestHelper.resolveAnalyzedLangForTest(ll1);
        List<PredictingGrule> ps = ParserCompiler.computePredictingGrules(al.ruleTypeToAlts, al.kleeneTypeToNode).getPgs();
        // System.out.println(ps);
        assertTrue(ps.size() == 2);
    }

    public void testComputePredictingGrulesWithInstantTokens() {
        Lang ll3 = new Lang("Test");
        Grule A = ll3.newGrule();

        ll3.defineGrule(A, CC.EOF);

        Grule B = ll3.newGrule();
        Grule C = ll3.newGrule();
        Grule D = ll3.newGrule();

        A.define(B, CC.ks("a")).alt(C, CC.kc("a")).alt(D, CC.op("a"));

        B.define("a", "b", "c", C).alt("a", "b", "c", D).alt("d");

        C.define("e", "f", "g", D).alt("e", "f", "g", "h");

        D.define("i", "j", "k", "l").alt("i", "j", "k", "m");

        AnalyzedLangForTest al = TestHelper.resolveAnalyzedLangForTest(ll3);
        List<PredictingGrule> ps = ParserCompiler.computePredictingGrules(al.ruleTypeToAlts, al.kleeneTypeToNode).getPgs();
        // System.out.println(ps);
        assertTrue(ps.size() == 5);
    }

    public void testParserCodeGen() {
        Lang lang = new Lang("Calculator");
        Grule L = lang.newGrule();
        TokenDef a = lang.newToken("\\+");
        lang.defineGrule(L, CC.EOF).action(new Action<Object>() {
            public Object act(Object matched) {
                Object[] ms = (Object[]) matched;
                System.out.println("Total result, length(2 exp): " + ms.length);
                return ms;
            }
        });
        Grule A = lang.newGrule();
        L.define(A, CC.ks(a.or("\\-"), A)).action(new Action<Object>() {
            public Object act(Object matched) {
                Object[] ms = (Object[]) matched;
                System.out.println("L result, length(2 exp): " + ms.length);
                return ms;
            }
        });
        TokenDef m = lang.newToken("\\*");
        Grule F = lang.newGrule();
        A.define(F, CC.ks(m.or("/"), F)).action(new Action<Object>() {
            public Object act(Object matched) {
                Object[] ms = (Object[]) matched;
                System.out.println("A result, length(2 exp): " + ms.length);
                return ms;
            }
        });
        F.define("\\(", L, "\\)").action(new Action<Object>() {
            public Object act(Object matched) {
                Object[] ms = (Object[]) matched;
                System.out.println("F result, length(3 exp): " + ms.length);
                return ms;
            }
        }).alt("[0-9]+").action(new ParamedAction<Object, Object>() {
            public Object act(Object arg, Object matched) {
                String m = (String) matched;
                System.out.println("F result, single value: " + m + ", arg: " + arg);
                return m;
            }
        });

        Exe exe = lang.compile();
        assertTrue(exe.eval("1+2", "hello") != null);
    }

    /**
     * <pre>
     * S ::= A $
     * A ::= a b c
     *     | a b c
     * </pre>
     */
    public void testDebugAndWarningsMsgs() {
        Lang lang = new Lang("Test");
        Grule A = lang.newGrule();
        lang.defineGrule(A, CC.EOF);
        A.define("a", "b", "c").alt("a", "b", "c");
        lang.compile();
        // System.out.println(lang.getDebugMsgs());
        assertTrue(lang.getDebugMsgs() != null);
        // System.out.println(lang.getWarnings());
        assertTrue(lang.getWarnings() != null);
    }
}

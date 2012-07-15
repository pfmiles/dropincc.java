package com.github.pfmiles.dropincc.impl;

import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import com.github.pfmiles.dropincc.Action;
import com.github.pfmiles.dropincc.CC;
import com.github.pfmiles.dropincc.Element;
import com.github.pfmiles.dropincc.Grule;
import com.github.pfmiles.dropincc.Lang;
import com.github.pfmiles.dropincc.TokenDef;
import com.github.pfmiles.dropincc.Tokens;
import com.github.pfmiles.dropincc.impl.kleene.CKleeneNode;
import com.github.pfmiles.dropincc.impl.kleene.KleeneStarNode;
import com.github.pfmiles.dropincc.impl.kleene.KleeneStarType;
import com.github.pfmiles.dropincc.impl.kleene.KleeneType;
import com.github.pfmiles.dropincc.testhelper.TestHelper;

/**
 * @author pf-miles
 * 
 */
public class AnalyzedLangTest extends TestCase {

    /**
     * <pre>
     * expr ::= addition EOF;
     * addition ::= addend ((AND | SUB) addend)*;
     * addend ::= factor ((MUL | DIV) factor)*;
     * factor ::= DIGIT
     *          | LEFTPAREN addition RIGHTPAREN;
     * </pre>
     */
    @SuppressWarnings({ "unused", "unchecked" })
    public void testSubRuleRewriteWithKleeneNodes() {
        Lang calculator = new Lang();
        TokenDef DIGIT = calculator.addToken("\\d+");
        TokenDef ADD = calculator.addToken("\\+");
        TokenDef SUB = calculator.addToken("\\-");
        TokenDef MUL = calculator.addToken("\\*");
        TokenDef DIV = calculator.addToken("/");
        TokenDef LEFTPAREN = calculator.addToken("\\(");
        TokenDef RIGHTPAREN = calculator.addToken("\\)");
        // 2.define grammar rules and corresponding actions
        Grule addition = calculator.newGrule();
        Grule addend = calculator.newGrule();
        Grule factor = calculator.newGrule();
        Element expr = calculator.addGrammarRule(addition, Tokens.EOF);
        addition.fillGrammarRule(addend, CC.ks(ADD.or(SUB), addend));
        addend.fillGrammarRule(factor, CC.ks(MUL.or(DIV), factor));
        factor.fillGrammarRule(DIGIT).alt(LEFTPAREN, addition, RIGHTPAREN);
        AnalyzedLang cl = new AnalyzedLang((List<TokenDef>) TestHelper.priField(calculator, "tokens"), (List<Grule>) TestHelper.priField(calculator, "grules"), false);
        KleeneStarNode k1 = (KleeneStarNode) addition.getAlts().get(0).getElements().get(1);
        Object shouldBeRewritten = k1.getElements().get(0);
        assertTrue(shouldBeRewritten instanceof Grule);
        Grule r = (Grule) shouldBeRewritten;
        assertTrue(r.getAlts().size() == 2);
        assertTrue(r.getAlts().get(0).getElements().get(0).equals(ADD));
        assertTrue(r.getAlts().get(1).getElements().get(0).equals(SUB));
        KleeneStarNode k2 = (KleeneStarNode) addend.getAlts().get(0).getElements().get(1);
        shouldBeRewritten = k2.getElements().get(0);
        assertTrue(shouldBeRewritten instanceof Grule);
        r = (Grule) shouldBeRewritten;
        assertTrue(r.getAlts().size() == 2);
        assertTrue(r.getAlts().get(0).getElements().get(0).equals(MUL));
        assertTrue(r.getAlts().get(1).getElements().get(0).equals(DIV));

        assertTrue(cl.getGruleTypeMapping().size() == 6);
    }

    public void testResolveParserAst() {
        Lang calculator = new Lang();
        TokenDef DIGIT = calculator.addToken("\\d+");
        TokenDef ADD = calculator.addToken("\\+");
        TokenDef SUB = calculator.addToken("\\-");
        TokenDef MUL = calculator.addToken("\\*");
        TokenDef DIV = calculator.addToken("/");
        TokenDef LEFTPAREN = calculator.addToken("\\(");
        TokenDef RIGHTPAREN = calculator.addToken("\\)");
        Grule addition = calculator.newGrule();
        Grule addend = calculator.newGrule();
        Grule factor = calculator.newGrule();
        Element expr = calculator.addGrammarRule(addition, Tokens.EOF).action(new Action() {
            public Object act(Object... params) {
                return params[0];
            }
        });
        addition.fillGrammarRule(addend, CC.ks((ADD.or(SUB)), addend)).action(new Action() {
            public Object act(Object... params) {
                double leftMost = (Double) params[0];
                Object[] opAndOther = (Object[]) params[1];
                for (int i = 0; i < opAndOther.length; i++) {
                    Object[] opAndOne = (Object[]) opAndOther[i];
                    if ("+".equals(opAndOne[0])) {
                        leftMost += (Double) opAndOne[1];
                    } else if ("-".equals(opAndOne[1])) {
                        leftMost -= (Double) opAndOne[1];
                    } else {
                        throw new RuntimeException("Invalid operator: " + opAndOne[0]);
                    }
                }
                return leftMost;
            }
        });
        addend.fillGrammarRule(factor, CC.ks(MUL.or(DIV), factor)).action(new Action() {
            public Object act(Object... params) {
                double leftMost = (Double) params[0];
                Object[] opAndOthers = (Object[]) params[1];
                for (int i = 0; i < opAndOthers.length; i++) {
                    Object[] opAndOther = (Object[]) opAndOthers[i];
                    if ("*".equals(opAndOthers[0])) {
                        leftMost *= (Double) opAndOthers[1];
                    } else if ("/".equals(opAndOthers[0])) {
                        leftMost /= (Double) opAndOthers[1];
                    } else {
                        throw new RuntimeException("Invalid operator: " + opAndOther[0]);
                    }
                }
                return leftMost;
            }
        });
        factor.fillGrammarRule(DIGIT).action(new Action() {
            public Object act(Object... params) {
                return Double.parseDouble((String) params[0]);
            }
        }).alt(LEFTPAREN, addition, RIGHTPAREN).action(new Action() {
            public Object act(Object... params) {
                return (Double) params[1];
            }
        });
        calculator.compile();
        AnalyzedLang alang = TestHelper.priField(calculator, "alang");
        Map<GruleType, List<CAlternative>> ruleTypeToAlts = alang.getRuleTypeToAlts();
        assertTrue(ruleTypeToAlts.size() == 6);
        for (Map.Entry<GruleType, List<CAlternative>> entry : ruleTypeToAlts.entrySet()) {
            List<CAlternative> calts = entry.getValue();
            switch (entry.getKey().getDefIndex()) {
            case 0:
                // addition
                assertTrue(calts.size() == 1);
                assertTrue(calts.get(0).getMatchSequence().size() == 2);
                assertTrue(calts.get(0).getMatchSequence().get(0) instanceof GruleType);
                assertTrue(calts.get(0).getMatchSequence().get(1) instanceof KleeneStarType);
                break;
            case 1:
                // addend
                assertTrue(calts.size() == 1);
                assertTrue(calts.get(0).getMatchSequence().size() == 2);
                assertTrue(calts.get(0).getMatchSequence().get(0) instanceof GruleType);
                assertTrue(calts.get(0).getMatchSequence().get(1) instanceof KleeneStarType);
                break;
            case 2:
                // factor
                assertTrue(calts.size() == 2);
                assertTrue(calts.get(0).getMatchSequence().size() == 1);
                assertTrue(calts.get(0).getMatchSequence().get(0) instanceof TokenType);

                assertTrue(calts.get(1).getMatchSequence().size() == 3);
                assertTrue(calts.get(1).getMatchSequence().get(0) instanceof TokenType);
                assertTrue(calts.get(1).getMatchSequence().get(1) instanceof GruleType);
                assertTrue(calts.get(1).getMatchSequence().get(2) instanceof TokenType);
                break;
            case 3:
                // expr
                assertTrue(calts.size() == 1);
                assertTrue(calts.get(0).getMatchSequence().size() == 2);
                assertTrue(calts.get(0).getMatchSequence().get(0) instanceof GruleType);
                assertTrue(calts.get(0).getMatchSequence().get(1) instanceof TokenType);
                break;
            case 4:
                // sub rule in addend
                assertTrue(calts.size() == 2);
                assertTrue(calts.get(0).getMatchSequence().size() == 1);
                assertTrue(calts.get(0).getMatchSequence().get(0) instanceof TokenType);
                assertTrue(calts.get(0).getMatchSequence().get(0).getDefIndex() == 3);

                assertTrue(calts.get(1).getMatchSequence().size() == 1);
                assertTrue(calts.get(1).getMatchSequence().get(0) instanceof TokenType);
                assertTrue(calts.get(1).getMatchSequence().get(0).getDefIndex() == 4);
                break;
            case 5:
                // sub rule in addition
                assertTrue(calts.size() == 2);
                assertTrue(calts.get(0).getMatchSequence().size() == 1);
                assertTrue(calts.get(0).getMatchSequence().get(0) instanceof TokenType);
                assertTrue(calts.get(0).getMatchSequence().get(0).getDefIndex() == 1);

                assertTrue(calts.get(1).getMatchSequence().size() == 1);
                assertTrue(calts.get(1).getMatchSequence().get(0) instanceof TokenType);
                assertTrue(calts.get(1).getMatchSequence().get(0).getDefIndex() == 2);
                break;
            default:
                assertTrue(false);// error num of grules
            }
        }
        Map<KleeneType, CKleeneNode> kleeneTypeToNode = alang.getKleeneTypeToNode();
        assertTrue(kleeneTypeToNode.size() == 2);
        for (Map.Entry<KleeneType, CKleeneNode> entry : kleeneTypeToNode.entrySet()) {
            List<EleType> matchSeq = entry.getValue().getContents();
            switch (entry.getKey().getDefIndex()) {
            case 0:
                assertTrue(matchSeq.size() == 2);
                assertTrue(matchSeq.get(0) instanceof GruleType);
                assertTrue(matchSeq.get(0).getDefIndex() == 4);// mul or div

                assertTrue(matchSeq.get(1) instanceof GruleType);
                assertTrue(matchSeq.get(1).getDefIndex() == 2);// factor
                break;
            case 1:
                assertTrue(matchSeq.size() == 2);
                assertTrue(matchSeq.get(0) instanceof GruleType);
                assertTrue(matchSeq.get(0).getDefIndex() == 5);// add or sub

                assertTrue(matchSeq.get(1) instanceof GruleType);
                assertTrue(matchSeq.get(1).getDefIndex() == 1);// addend
                break;
            default:
                assertTrue(false);// error num of entries
            }
        }
    }
}

package com.github.pfmiles.dropincc.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import com.github.pfmiles.dropincc.CC;
import com.github.pfmiles.dropincc.Element;
import com.github.pfmiles.dropincc.Grule;
import com.github.pfmiles.dropincc.Lang;
import com.github.pfmiles.dropincc.Token;
import com.github.pfmiles.dropincc.testhelper.TestHelper;

/**
 * @author pf-miles
 * 
 */
public class CompiledLangTest extends TestCase {
	public void testCheckAndCompileTokenRulesInvalidTokens() {
		Lang dl = new Lang();
		List<Token> tokens = new ArrayList<Token>();
		// null token test
		tokens.add(dl.addToken(null));
		CompiledLang cl = new CompiledLang(tokens, null);
		try {
			TestHelper.priIvk(cl, "checkAndCompileTokenRules");
			assertTrue(false);
		} catch (Exception e) {
			assertTrue("Cannot create null token.".equals(e.getMessage()));
		}
		tokens.clear();
		// empty token test
		tokens.add(dl.addToken(""));
		try {
			TestHelper.priIvk(cl, "checkAndCompileTokenRules");
			assertTrue(false);
		} catch (Exception e) {
			assertTrue("Cannot create null token.".equals(e.getMessage()));
		}
		tokens.clear();
		// error pattern test
		tokens.add(dl.addToken("aaa"));
		tokens.add(dl.addToken("[[["));
		try {
			TestHelper.priIvk(cl, "checkAndCompileTokenRules");
			assertTrue(false);
		} catch (Exception e) {
			assertTrue("Invalid token rule: '[[['".equals(e.getMessage()));
		}
		tokens.clear();
	}

	public void testCombinedTokenRulesGroupNums() {
		Lang dl = new Lang();
		List<Token> tokens = new ArrayList<Token>();
		tokens.add(dl.addToken("aaa"));
		tokens.add(dl.addToken("bb(c(d))"));
		tokens.add(dl.addToken("ee(f\\(g\\))"));
		tokens.add(dl.addToken("hh\\(i\\(j\\)k\\)l"));
		tokens.add(dl.addToken("zzz"));
		CompiledLang cl = new CompiledLang(tokens, null);
		TestHelper.priIvk(cl, "checkAndCompileTokenRules");
		// List<Integer> nums = cl.getTokenGroupNums();
		Map<Integer, EleType> gnumToType = cl.getGroupNumToType();
		assertTrue(gnumToType.size() == 5);
		// Integer[] exps = new Integer[] { 1, 2, 5, 7, 8 };
		Map<Integer, EleType> exps = new HashMap<Integer, EleType>();
		exps.put(1, new TokenType(0));
		exps.put(2, new TokenType(1));
		exps.put(5, new TokenType(2));
		exps.put(7, new TokenType(3));
		exps.put(8, new TokenType(4));
		assertTrue(gnumToType.equals(exps));
	}

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
		term.fillGrammarRule(DIGIT, mulTail).alt(LEFTPAREN, expr, RIGHTPAREN)
				.alt(DIGIT);
		Element addendTail = calculator.addGrammarRule(ADD.or(SUB), term);
		expr.fillGrammarRule(term, addendTail, CC.EOF);

		CompiledLang cl = new CompiledLang((List<Token>) TestHelper.priField(
				calculator, "tokens"), (List<Grule>) TestHelper.priField(
				calculator, "grules"));
		List<Grule> genGrules = TestHelper.priField(cl, "genGrules");
		assertTrue(genGrules.size() == 2);
		for (Grule gg : genGrules) {
			assertTrue(gg.getAlts().size() == 2);
			for (Alternative alt : gg.getAlts()) {
				assertTrue(alt.getElements().size() == 1);
				assertTrue(alt.getElements().get(0) instanceof Token);
			}
		}
		assertTrue(cl.getGruleTypeMapping().size() == 6);
	}

	/**
	 * Intended to test sub rule rewrite
	 * 
	 * <pre>
	 * term ::= DIGIT ((MUL | DIV) term) 
	 * 		  | LEFTPAREN expr RIGHTPAREN
	 * 		  | DIGIT ;
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
		term.fillGrammarRule(DIGIT, MUL.or(DIV).and(term))
				.alt(LEFTPAREN, expr, RIGHTPAREN).alt(DIGIT);
		expr.fillGrammarRule(term, ADD.or(SUB).and(term), CC.EOF);

		CompiledLang cl = new CompiledLang((List<Token>) TestHelper.priField(
				calculator, "tokens"), (List<Grule>) TestHelper.priField(
				calculator, "grules"));
		List<Grule> genGrules = TestHelper.priField(cl, "genGrules");
		assertTrue(genGrules.size() == 4);
		Grule g1 = genGrules.get(0);
		assertTrue(g1.getAlts().size() == 1);
		assertTrue(g1.getAlts().get(0).getElements().size() == 2);
		assertTrue(g1.getAlts().get(0).getElements().get(0) instanceof GruleCreator);
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
		assertTrue(g3.getAlts().get(0).getElements().get(0) instanceof GruleCreator);
		assertTrue(g3.getAlts().get(0).getElements().get(1) instanceof Grule);

		Grule g4 = genGrules.get(3);
		assertTrue(g4.getAlts().size() == 2);
		assertTrue(g4.getAlts().get(0).getElements().size() == 1);
		assertTrue(g4.getAlts().get(0).getElements().get(0) instanceof Token);
		assertTrue(g4.getAlts().get(1).getElements().size() == 1);
		assertTrue(g4.getAlts().get(1).getElements().get(0) instanceof Token);

		assertTrue(cl.getGruleTypeMapping().size() == 6);
	}

	// TODO add a test which rewrites 'and' invocation cascading 'or'
}

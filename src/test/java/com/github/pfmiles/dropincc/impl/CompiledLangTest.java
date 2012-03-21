/**
 * 
 */
package com.github.pfmiles.dropincc.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import junit.framework.TestCase;

import com.github.pfmiles.dropincc.Token;

/**
 * @author pf-miles
 * 
 */
public class CompiledLangTest extends TestCase {
	public void testCheckAndCompileTokenRulesInvalidTokens() {
		CompiledLang cl = new CompiledLang();
		List<Token> tokens = new ArrayList<Token>();
		// null token test
		tokens.add(new Token(null));
		try {
			cl.checkAndCompileTokenRules(tokens);
			assertTrue(false);
		} catch (Exception e) {
			assertTrue("Cannot create null token.".equals(e.getMessage()));
		}
		tokens.clear();
		// empty token test
		tokens.add(new Token(""));
		try {
			cl.checkAndCompileTokenRules(tokens);
			assertTrue(false);
		} catch (Exception e) {
			assertTrue("Cannot create null token.".equals(e.getMessage()));
		}
		tokens.clear();
		// error pattern test
		tokens.add(new Token("aaa"));
		tokens.add(new Token("[[["));
		try {
			cl.checkAndCompileTokenRules(tokens);
			assertTrue(false);
		} catch (Exception e) {
			assertTrue("Invalid token rule: '[[['".equals(e.getMessage()));
		}
		tokens.clear();
	}

	public void testCombinedTokenRulesGroupNums() {
		CompiledLang cl = new CompiledLang();
		List<Token> tokens = new ArrayList<Token>();
		tokens.add(new Token("aaa"));
		tokens.add(new Token("bb(c(d))"));
		tokens.add(new Token("ee(f\\(g\\))"));
		tokens.add(new Token("hh\\(i\\(j\\)k\\)l"));
		tokens.add(new Token("zzz"));
		cl.checkAndCompileTokenRules(tokens);
		List<Integer> nums = cl.getTokenGroupNums();
		assertTrue(nums.size() == 5);
		Integer[] exps = new Integer[] { 1, 2, 5, 7, 8 };
		List<Integer> expl = Arrays.asList(exps);
		assertTrue(nums.equals(expl));
	}
}

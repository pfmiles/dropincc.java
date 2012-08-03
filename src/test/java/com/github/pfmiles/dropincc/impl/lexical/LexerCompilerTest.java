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
package com.github.pfmiles.dropincc.impl.lexical;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import junit.framework.TestCase;

import com.github.pfmiles.dropincc.Lang;
import com.github.pfmiles.dropincc.TokenDef;
import com.github.pfmiles.dropincc.impl.EleType;
import com.github.pfmiles.dropincc.impl.TokenType;
import com.github.pfmiles.dropincc.impl.util.Pair;
import com.github.pfmiles.dropincc.testhelper.TestHelper;

/**
 * @author pf-miles
 * 
 */
@SuppressWarnings("unchecked")
public class LexerCompilerTest extends TestCase {
    public void testCheckAndCompileTokenRulesInvalidTokens() {
        Lang dl = new Lang("Test");
        List<TokenDef> tokens = new ArrayList<TokenDef>();
        // null token test
        tokens.add(dl.newToken(null));
        dl.defineGrule(dl.newToken("ok!"));
        Map<TokenDef, TokenType> tokenTypeMapping = LexerCompiler.buildTokenTypeMapping(tokens, false);
        try {
            LexerCompiler.checkAndCompileTokenRules(tokens, tokenTypeMapping);
            assertTrue(false);
        } catch (Exception e) {
            assertTrue("Cannot create null token.".equals(e.getMessage()));
        }
        tokens.clear();
        // empty token test
        tokens.add(dl.newToken(""));
        try {
            LexerCompiler.checkAndCompileTokenRules(tokens, tokenTypeMapping);
            assertTrue(false);
        } catch (Exception e) {
            assertTrue("Cannot create null token.".equals(e.getMessage()));
        }
        tokens.clear();
        // error pattern test
        tokens.add(dl.newToken("aaa"));
        tokens.add(dl.newToken("[[["));
        try {
            LexerCompiler.checkAndCompileTokenRules(tokens, tokenTypeMapping);
            assertTrue(false);
        } catch (Exception e) {
            assertTrue("Invalid token rule: '[[['".equals(e.getMessage()));
        }
        tokens.clear();
    }

    public void testCombinedTokenRulesGroupNums() {
        Lang dl = new Lang("Test");
        List<TokenDef> tokens = new ArrayList<TokenDef>();
        tokens.add(dl.newToken("aaa"));
        tokens.add(dl.newToken("bb(c(d))"));
        tokens.add(dl.newToken("ee(f\\(g\\))"));
        tokens.add(dl.newToken("hh\\(i\\(j\\)k\\)l"));
        tokens.add(dl.newToken("zzz"));
        dl.defineGrule(dl.newToken("stubToken"));
        Map<TokenDef, TokenType> tokenTypeMapping = LexerCompiler.buildTokenTypeMapping(tokens, false);
        Pair<Map<Integer, TokenType>, Pattern> pair = LexerCompiler.checkAndCompileTokenRules(tokens, tokenTypeMapping);
        Map<Integer, TokenType> gnumToType = pair.getLeft();
        assertTrue(gnumToType.size() == 6);
        // Integer[] exps = new Integer[] { 1, 2, 5, 7, 8 , -2};
        Map<Integer, EleType> exps = new HashMap<Integer, EleType>();
        exps.put(1, new TokenType(0, "0"));
        exps.put(2, new TokenType(1, "1"));
        exps.put(5, new TokenType(2, "2"));
        exps.put(7, new TokenType(3, "3"));
        exps.put(8, new TokenType(4, "4"));
        exps.put(9, new TokenType(-2, "-2"));
        assertTrue(gnumToType.equals(exps));
    }

    public void testBuildTokenTypeMappingWhiteSpaceSensitive() {
        Lang dl = new Lang("Test");
        List<TokenDef> tokens = new ArrayList<TokenDef>();
        tokens.add(dl.newToken("aaa"));
        tokens.add(dl.newToken("bb(c(d))"));
        tokens.add(dl.newToken("ee(f\\(g\\))"));
        tokens.add(dl.newToken("hh\\(i\\(j\\)k\\)l"));
        tokens.add(dl.newToken("zzz"));
        dl.defineGrule(dl.newToken("stubToken"));
        Map<TokenDef, TokenType> tokenTypeMapping = LexerCompiler.buildTokenTypeMapping(tokens, true);
        Pair<Map<Integer, TokenType>, Pattern> pair = LexerCompiler.checkAndCompileTokenRules(tokens, tokenTypeMapping);
        Map<Integer, TokenType> gnumToType = pair.getLeft();
        assertTrue(gnumToType.size() == 5);
        // Integer[] exps = new Integer[] { 1, 2, 5, 7, 8};
        Map<Integer, EleType> exps = new HashMap<Integer, EleType>();
        exps.put(1, new TokenType(0, "0"));
        exps.put(2, new TokenType(1, "1"));
        exps.put(5, new TokenType(2, "2"));
        exps.put(7, new TokenType(3, "3"));
        exps.put(8, new TokenType(4, "4"));
        assertTrue(gnumToType.equals(exps));
    }

    public void testAddSameTokens() {
        Lang l = new Lang("Test");
        l.newToken("a");
        l.newToken("b");
        l.newToken("c");
        l.newToken("c");
        l.newToken("a");
        l.newToken("a");
        l.newToken("b");
        Map<TokenDef, TokenType> tokenTypeMapping = LexerCompiler.buildTokenTypeMapping((List<TokenDef>) TestHelper.priField(l, "tokens"), true);
        assertTrue(tokenTypeMapping.size() == 4);
    }
}

package com.github.pfmiles.dropincc.impl.lexical;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import junit.framework.TestCase;

import com.github.pfmiles.dropincc.Lang;
import com.github.pfmiles.dropincc.Token;
import com.github.pfmiles.dropincc.impl.EleType;
import com.github.pfmiles.dropincc.impl.TokenType;
import com.github.pfmiles.dropincc.impl.util.Pair;

/**
 * @author pf-miles
 * 
 */
public class LexerCompilerTest extends TestCase {
    public void testCheckAndCompileTokenRulesInvalidTokens() {
        Lang dl = new Lang();
        List<Token> tokens = new ArrayList<Token>();
        // null token test
        tokens.add(dl.addToken(null));
        dl.addGrammarRule(dl.addToken("ok!"));
        Map<Token, TokenType> tokenTypeMapping = LexerCompiler.buildTokenTypeMapping(tokens);
        try {
            LexerCompiler.checkAndCompileTokenRules(tokens, tokenTypeMapping);
            assertTrue(false);
        } catch (Exception e) {
            assertTrue("Cannot create null token.".equals(e.getMessage()));
        }
        tokens.clear();
        // empty token test
        tokens.add(dl.addToken(""));
        try {
            LexerCompiler.checkAndCompileTokenRules(tokens, tokenTypeMapping);
            assertTrue(false);
        } catch (Exception e) {
            assertTrue("Cannot create null token.".equals(e.getMessage()));
        }
        tokens.clear();
        // error pattern test
        tokens.add(dl.addToken("aaa"));
        tokens.add(dl.addToken("[[["));
        try {
            LexerCompiler.checkAndCompileTokenRules(tokens, tokenTypeMapping);
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
        dl.addGrammarRule(dl.addToken("stubToken"));
        Map<Token, TokenType> tokenTypeMapping = LexerCompiler.buildTokenTypeMapping(tokens);
        Pair<Map<Integer, EleType>, Pattern> pair = LexerCompiler.checkAndCompileTokenRules(tokens,
                tokenTypeMapping);
        Map<Integer, EleType> gnumToType = pair.getLeft();
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
}

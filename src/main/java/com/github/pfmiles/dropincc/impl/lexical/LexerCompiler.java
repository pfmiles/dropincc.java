package com.github.pfmiles.dropincc.impl.lexical;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import com.github.pfmiles.dropincc.DropinccException;
import com.github.pfmiles.dropincc.Token;
import com.github.pfmiles.dropincc.Tokens;
import com.github.pfmiles.dropincc.impl.EleType;
import com.github.pfmiles.dropincc.impl.TokenType;
import com.github.pfmiles.dropincc.impl.util.Pair;
import com.github.pfmiles.dropincc.impl.util.Util;

/**
 * Util, to check and compile lexer rules.
 * 
 * @author pf-miles
 * 
 */
public class LexerCompiler {

    public static Map<Token, TokenType> buildTokenTypeMapping(List<Token> tokens, boolean whitespaceSensitive) {
        Map<Token, TokenType> tokenTypeMapping = new HashMap<Token, TokenType>();
        if (tokens != null) {
            for (int i = 0; i < tokens.size(); i++) {
                tokenTypeMapping.put(tokens.get(i), new TokenType(i));
            }
            // EOF is of token type -1
            tokenTypeMapping.put(Tokens.EOF, new TokenType(-1));
            if (!whitespaceSensitive) {
                // if the lexer want to be not sensitive about whitespaces, add
                // a default lexer rule to the end of lexer rule chain to catch
                // all whitespaces which is not matched by user-defined
                // lexer rules previously
                Token whiteSpaceToken = new GenedToken("\\s+");
                tokens.add(whiteSpaceToken);
                tokenTypeMapping.put(whiteSpaceToken, new TokenType(-2));
            }
        }
        return tokenTypeMapping;
    }

    /**
     * @param tokens
     * @return
     */
    public static Pair<Map<Integer, EleType>, Pattern> checkAndCompileTokenRules(List<Token> tokens, Map<Token, TokenType> tokenTypeMapping) {
        // check regex valid
        checkRegexps(tokens);
        return combineAndCompileRules(tokens, tokenTypeMapping);
    }

    private static void checkRegexps(List<Token> tokens) {
        for (Token t : tokens) {
            if (Util.isEmpty(t.getRegexp()))
                throw new DropinccException("Cannot create null token.");
            try {
                Pattern.compile(t.getRegexp());
            } catch (PatternSyntaxException e) {
                throw new DropinccException("Invalid token rule: '" + t.getRegexp() + "'", e);
            }
        }
    }

    // combine all token rules into one for matching these tokens using 'group
    // capturing', also returns each rule's corresponding group number
    private static Pair<Map<Integer, EleType>, Pattern> combineAndCompileRules(List<Token> tokens, Map<Token, TokenType> tokenTypeMapping) {
        Map<Integer, EleType> groupNumToType = new HashMap<Integer, EleType>();
        StringBuilder sb = new StringBuilder();
        int groupCount = 1;// group num starts at 1
        for (Token t : tokens) {
            if (t.equals(Tokens.EOF))
                continue;
            if (sb.length() != 0)
                sb.append("|");
            sb.append("(\\G");

            String regExp = t.getRegexp();
            sb.append(regExp);
            groupNumToType.put(groupCount, tokenTypeMapping.get(t));

            sb.append(")");
            groupCount++;
            groupCount += countInnerGroups(regExp);// skip groups in the pattern
        }

        return new Pair<Map<Integer, EleType>, Pattern>(groupNumToType, Pattern.compile(sb.toString()));
    }

    // count groups inside the sub regExp
    private static int countInnerGroups(String regExp) {
        int ret = 0;
        for (int i = 0; i < regExp.length(); i++) {
            if ('(' == regExp.charAt(i) && (i - 1 < 0 || '\\' != regExp.charAt(i - 1)))
                ret++;
        }
        return ret;
    }

}

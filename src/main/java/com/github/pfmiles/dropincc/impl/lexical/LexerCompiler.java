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
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import com.github.pfmiles.dropincc.CC;
import com.github.pfmiles.dropincc.DropinccException;
import com.github.pfmiles.dropincc.Element;
import com.github.pfmiles.dropincc.Grule;
import com.github.pfmiles.dropincc.TokenDef;
import com.github.pfmiles.dropincc.impl.Alternative;
import com.github.pfmiles.dropincc.impl.AndSubRule;
import com.github.pfmiles.dropincc.impl.ConstructingGrule;
import com.github.pfmiles.dropincc.impl.OrSubRule;
import com.github.pfmiles.dropincc.impl.TokenType;
import com.github.pfmiles.dropincc.impl.kleene.AbstractKleeneNode;
import com.github.pfmiles.dropincc.impl.util.Pair;
import com.github.pfmiles.dropincc.impl.util.Util;

/**
 * Util, to check and compile lexer rules.
 * 
 * @author pf-miles
 * 
 */
public class LexerCompiler {

    public static Map<TokenDef, TokenType> buildTokenTypeMapping(List<TokenDef> tokens, boolean whitespaceSensitive) {
        Map<TokenDef, TokenType> tokenTypeMapping = new HashMap<TokenDef, TokenType>();
        if (tokens != null) {
            int i = 0;
            for (Iterator<TokenDef> iter = tokens.iterator(); iter.hasNext();) {
                TokenDef t = iter.next();
                if (tokenTypeMapping.containsKey(t))
                    continue;
                tokenTypeMapping.put(t, new TokenType(i, t.getRegexp()));
                i++;
            }
            // EOF is of token type -1
            tokenTypeMapping.put(CC.EOF, TokenType.EOF);
            if (!whitespaceSensitive) {
                // if the lexer want to be not sensitive about whitespaces, add
                // a default lexer rule to the end of lexer rule chain to catch
                // all whitespaces which is not matched by user-defined
                // lexer rules previously
                TokenDef whiteSpaceToken = new GenedTokenDef("\\s+");
                tokens.add(whiteSpaceToken);
                tokenTypeMapping.put(whiteSpaceToken, TokenType.WHITESPACE);
            }
        }
        return tokenTypeMapping;
    }

    /**
     * @param tokens
     * @return Pair&lt;GroupNumToType, Patterns&gt;
     * 
     */
    public static Pair<Map<Integer, TokenType>, Pattern> checkAndCompileTokenRules(List<TokenDef> tokens, Map<TokenDef, TokenType> tokenTypeMapping) {
        // check regex valid
        checkRegexps(tokens);
        return combineAndCompileRules(tokens, tokenTypeMapping);
    }

    private static void checkRegexps(List<TokenDef> tokens) {
        for (TokenDef t : tokens) {
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
    private static Pair<Map<Integer, TokenType>, Pattern> combineAndCompileRules(List<TokenDef> tokens, Map<TokenDef, TokenType> tokenTypeMapping) {
        Map<Integer, TokenType> groupNumToType = new HashMap<Integer, TokenType>();
        StringBuilder sb = new StringBuilder();
        int groupCount = 1;// group num starts at 1
        for (TokenDef t : tokens) {
            if (t.equals(CC.EOF))
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

        return new Pair<Map<Integer, TokenType>, Pattern>(groupNumToType, Pattern.compile(sb.toString()));
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

    /**
     * Traverse the grammar rule tree, and gather all instantly added token defs
     * 
     * @param grules
     * @return
     */
    public static List<InstantTokenDef> collectInstantTokenDefs(List<Grule> grules) {
        List<InstantTokenDef> ret = new ArrayList<InstantTokenDef>();
        Set<Element> traversed = new HashSet<Element>();
        for (Grule g : grules) {
            traversed.add(g);
            ret.addAll(collectInstantTokenDefFromAlts(g.getAlts(), traversed));
        }
        return ret;
    }

    private static List<InstantTokenDef> collectInstantTokenDefFromAlts(List<Alternative> alts, Set<Element> traversed) {
        List<InstantTokenDef> ret = new ArrayList<InstantTokenDef>();
        for (Alternative alt : alts) {
            ret.addAll(collectInstantTokenDefFromElements(alt.getElements(), traversed));
        }
        return ret;
    }

    private static List<InstantTokenDef> collectInstantTokenDefFromElements(List<Element> eles, Set<Element> traversed) {
        List<InstantTokenDef> ret = new ArrayList<InstantTokenDef>();
        for (Element ele : eles) {
            if (traversed.contains(ele))
                continue;
            traversed.add(ele);
            if (ele instanceof AbstractKleeneNode) {
                ret.addAll(collectInstantTokenDefFromElements(((AbstractKleeneNode) ele).getElements(), traversed));
            } else if (ele instanceof AndSubRule) {
                ret.addAll(collectInstantTokenDefFromAlts(((AndSubRule) ele).getAlts(), traversed));
            } else if (ele instanceof OrSubRule) {
                ret.addAll(collectInstantTokenDefFromAlts(((OrSubRule) ele).getAlts(), traversed));
            } else if (ele instanceof ConstructingGrule) {
                throw new DropinccException("Something must be wrong, ConstructingGrule shouldn't appear here");
            } else if (ele instanceof Grule) {
                ret.addAll(collectInstantTokenDefFromAlts(((Grule) ele).getAlts(), traversed));
            } else if (ele instanceof InstantTokenDef) {
                ret.add((InstantTokenDef) ele);
            } else {
                // other, pass
                continue;
            }
        }
        return ret;
    }
}

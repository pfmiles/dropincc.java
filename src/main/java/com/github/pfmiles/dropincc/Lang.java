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
package com.github.pfmiles.dropincc;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import com.github.pfmiles.dropincc.impl.Alternative;
import com.github.pfmiles.dropincc.impl.AnalyzedLang;
import com.github.pfmiles.dropincc.impl.ConstructingGrule;
import com.github.pfmiles.dropincc.impl.util.Util;

/**
 * Represents a constructing language of your own... It could add lexer &
 * grammar rules on the fly and compile at any time.
 * 
 * @author pf-miles
 * 
 */
public class Lang implements Serializable {

    private static final long serialVersionUID = 631738160652653120L;

    private String name;
    private Pattern langNamePattern = Pattern.compile("^[a-zA-Z]\\w*$");

    /*
     * if true, the lexer generator won't add a whitespace token automatically
     * at the end of token regex chain. Left over the whitespace manipulation
     * tasks to user defined lexer rules.
     */
    private boolean whiteSpaceSensitive;

    /*
     * if turned on, tokens does not record any more infomation other than
     * 'type' and 'lexeme', TODO infiniteParsing?
     */

    private List<TokenDef> tokens = new ArrayList<TokenDef>();
    private List<Grule> grules = new ArrayList<Grule>();

    /**
     * Create language object with a name
     * 
     * @param name
     */
    public Lang(String name) {
        if (name == null)
            throw new DropinccException("Language name could not be null.");
        if (!langNamePattern.matcher(name).matches())
            throw new DropinccException(
                    "Language name should be a valid identifier name in java(Start by a character, followed by arbitrary numbers of digit or character).");
        this.name = name;
    }

    /**
     * Add a new token rule, note that the token rules are trying to match as
     * the same order as it is added. TODO lexer should do 'longest match', when
     * same length, choose first defined
     * 
     * @param regExpr
     *            , the regExp to describe a token rule
     * @return the added token itself as an element, for use in later grammar
     *         rule definitions.
     */
    public TokenDef newToken(String regExpr) {
        TokenDef t = new TokenDef(regExpr);
        this.tokens.add(t);
        return t;
    }

    /**
     * Add a new grammar rule, its returned value could continuing
     * 'construction' in latter cascading invocations.
     * 
     * @param grammerRule
     * @return the added grammar rule object itself(wrapped for continuing rule
     *         construction), for use in later grammar rule definitions.
     */
    public ConstructingGrule defineGrule(Object... eles) {
        if (eles == null || eles.length == 0)
            throw new DropinccException("Could not add empty grammar rule, if you want to add a rule alternative that matches nothing, use CC.NOTHING.");
        Grule g = new Grule(this.grules.size());
        Element[] elements = Util.filterProductionEles(eles);
        g.getAlts().add(new Alternative(elements));
        this.grules.add(g);
        return new ConstructingGrule(g);
    }

    /**
     * compile the rules to an more efficient form and ready for code gen.
     */
    public Exe compile() {
        AnalyzedLang cl = new AnalyzedLang(this.name, this.tokens, this.grules, this.whiteSpaceSensitive);
        cl.compile();
        return new Exe(cl);
    }

    /**
     * Check if the parser is white-space sensitive('sensitive' means see
     * white-spaces as tokens instead of ignoring them).
     * 
     * @return
     */
    public boolean isWhiteSpaceSensitive() {
        return whiteSpaceSensitive;
    }

    /**
     * Set the generated lexer white-space sensitive, the lexer is not
     * white-space sensitive by default(it would ignore any whitespace
     * characters not matched by user defined lexer rules).You may want this to
     * be 'true' if you want to parse a language like Python...
     * 
     * @param whiteSpaceSensitive
     */
    public void setWhiteSpaceSensitive(boolean whiteSpaceSensitive) {
        this.whiteSpaceSensitive = whiteSpaceSensitive;
    }

    /**
     * Outputs a 'BNF'-alike representation of the constructing language.
     */
    public String toString() {
        return "Lang []";// TODO
    }

    /**
     * Create a new Grule. The new grule is intended for later reference in
     * other grule constructs. If not so, you should add a grammar rule to your
     * language via 'Lang.addGrammarRule' method.
     * 
     * @return
     */
    public Grule newGrule() {
        Grule g = new Grule(this.grules.size());
        this.grules.add(g);
        return g;
    }

    // same hashCode method as Object.class needed
    public int hashCode() {
        return super.hashCode();
    }

    // same equals method as Object.class needed
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

}

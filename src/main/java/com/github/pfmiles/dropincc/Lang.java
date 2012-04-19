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

    private boolean whiteSpaceSensitive;

    private List<Token> tokens = new ArrayList<Token>();
    private List<Grule> grules = new ArrayList<Grule>();

    private AnalyzedLang alang;

    /**
     * Add a new token rule
     * 
     * @param regExpr
     *            , the regExp to describe a token rule
     * @return the added token itself as an element, for use in later grammar
     *         rule definitions.
     */
    public Token addToken(String regExpr) {
        Token t = new Token(regExpr);
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
    public ConstructingGrule addGrammarRule(Element... eles) {
        if (eles == null || eles.length == 0)
            throw new DropinccException(
                    "Could not add empty grammar rule, if you want to add a rule alternative that matches nothing, use CC.NOTHING.");
        Grule g = new Grule();
        eles = Util.filterConstructingGrules(eles);
        g.getAlts().add(new Alternative(eles));
        this.grules.add(g);
        return new ConstructingGrule(g);
    }

    /**
     * compile the rules to an more efficient form and ready for code gen.
     */
    public void compile() {
        AnalyzedLang cl = new AnalyzedLang(this.tokens, this.grules);
        cl.compile();
        this.alang = cl;
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
     * set the parse white-space sensitive, the parser is not white-space
     * sensitive by default.
     * 
     * @param whiteSpaceSensitive
     */
    public void setWhiteSpaceSensitive(boolean whiteSpaceSensitive) {
        this.whiteSpaceSensitive = whiteSpaceSensitive;
    }

    /**
     * Outputs a 'BNF'-alike representation of the underlined language.
     */
    public String toString() {
        return "Lang []";// TODO
    }

    /**
     * Execute the new language's code
     * 
     * @param code
     * @return the execution return value of the inputed code, if any
     */
    public <T> T exe(String code) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * Create a new Grule. The new grule is intended for later reference in
     * other grule constructs. If not so, you should add a grammar rule to your
     * language via 'Lang.addGrammarRule' method.
     * 
     * @return
     */
    public Grule newGrule() {
        Grule g = new Grule();
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

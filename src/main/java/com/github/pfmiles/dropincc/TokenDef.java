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

import com.github.pfmiles.dropincc.impl.AndSubRule;
import com.github.pfmiles.dropincc.impl.OrSubRule;

/**
 * 
 * This represents a token definition. In dropincc.java, token rules are forced
 * to be described as regular expressions. Any rule conflict which cannot be
 * solved in regular grammar should be pushed back into syntactic analysis phase
 * or even semantics analysis phase. This should be a good practice that keeps
 * the lexical rules simple.
 * 
 * @author pf-miles
 * 
 */
public class TokenDef implements Element {

    private static final long serialVersionUID = -8800772928848920147L;

    private String regexp;

    /*
     * Construct a token described in regex, package access privilege makes it
     * could only be created via methods in Lang.java.
     * 
     * @param regexp
     */
    protected TokenDef(String regexp) {
        this.regexp = regexp;
    }

    /**
     * token alternative,for examle:
     * 
     * <pre>
     * Token ADD = lang.addToken(&quot;\\+&quot;);
     * Token SUB = lang.addToken(&quot;\\-&quot;);
     * Grule addendTail = lang.addGrammarRule(ADD.or(SUB), term);
     * </pre>
     * 
     * the code above means:
     * 
     * <pre>
     * 	ADD ::= '+';
     * 	SUB ::= '-';
     * 	addendTail ::= (ADD | SUB) term
     * </pre>
     * 
     * it has higher priority than normal concatenation
     * 
     * @param ts
     * @return
     */
    public OrSubRule or(Element ele) {
        return new OrSubRule(this, ele);
    }

    public AndSubRule and(Element ele) {
        return new AndSubRule(this, ele);
    }

    public String getRegexp() {
        return regexp;
    }

    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((regexp == null) ? 0 : regexp.hashCode());
        return result;
    }

    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof TokenDef))
            return false;
        TokenDef other = (TokenDef) obj;
        if (regexp == null) {
            if (other.regexp != null)
                return false;
        } else if (!regexp.equals(other.regexp))
            return false;
        return true;
    }

    public String toString() {
        return this.regexp;
    }

}

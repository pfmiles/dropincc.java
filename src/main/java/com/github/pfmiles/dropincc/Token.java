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
 * This represents a token. In dropincc.java, token rules are forced to be
 * described as regular expressions. Any rule conflict which cannot be solved in
 * regular grammar should be pushed back into syntactic analysis phase or even
 * semantics analysis phase. This should be a good practice that keeps the
 * lexical rules quite simple.
 * 
 * @author pf-miles
 * 
 */
public class Token implements Element {

	private static final long serialVersionUID = -8800772928848920147L;

	private String regexp;

	/**
	 * Construct a token described in regex
	 * 
	 * @param regexp
	 */
	public Token(String regexp) {
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
		return new OrSubRule(ele);
	}

	public AndSubRule and(Element ele) {
		return new AndSubRule(ele);
	}

	public String getRegexp() {
		return regexp;
	}
}

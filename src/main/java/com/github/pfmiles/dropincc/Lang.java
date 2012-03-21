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
import com.github.pfmiles.dropincc.impl.CompiledLang;
import com.github.pfmiles.dropincc.impl.ConstructingGrule;

/**
 * Represents a constructing language of your own... It could add lexer &
 * grammar rules on the fly and compile at any time.
 * 
 * @author pf-miles
 * 
 */
public class Lang implements Serializable {

	private static final long serialVersionUID = 631738160652653120L;

	boolean whiteSpaceSensitive;

	private List<Token> tokens = new ArrayList<Token>();
	private List<Grule> grules = new ArrayList<Grule>();

	CompiledLang clang;

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
		g.getAlts().add(new Alternative(eles));
		this.grules.add(g);
		return new ConstructingGrule(g);
	}

	/**
	 * compile the rules to an more efficient form and ready for parsing.
	 */
	public void compile() {
		CompiledLang cl = new CompiledLang();
		// 1.resolving the parser ast and token rules
		cl.checkAndCompileTokenRules(this.tokens);
		// 2.check & compile token rules
		// 3.check & simplify & compute grammar rules
		// 4.parser code gen
		// 5.compile and maintain the code in a separate classloader
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
	public Object exe(String code) {
		// TODO Auto-generated method stub
		return null;
	}
}

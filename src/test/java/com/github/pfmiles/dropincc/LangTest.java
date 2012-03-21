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

import junit.framework.TestCase;

/**
 * @author pf-miles
 * 
 */
public class LangTest extends TestCase {
	public void testCalculator() {
		// 3.define lexical rules
		Lang calculator = new Lang();
		Token DIGIT = calculator.addToken("\\d+");
		Token ADD = calculator.addToken("\\+");
		Token SUB = calculator.addToken("\\-");
		Token MUL = calculator.addToken("\\*");
		Token DIV = calculator.addToken("/");
		Token LEFTPAREN = calculator.addToken("\\(");
		Token RIGHTPAREN = calculator.addToken("\\)");
		// 2.define grammar rules and corresponding actions
		Grule expr = new Grule();
		Grule term = new Grule();
		Element mulTail = calculator.addGrammarRule(MUL.or(DIV), term).action(
				new Action() {
					public Object act(Object... params) {
						return params;
					}
				});
		term.fillGrammarRule(DIGIT, mulTail).action(new Action() {
			public Object act(Object... params) {
				int factor = Integer.parseInt((String) params[0]);
				Object[] mulTailReturn = (Object[]) params[1];
				String op = (String) mulTailReturn[0];
				int factor2 = (Integer) mulTailReturn[1];
				if ("*".equals(op)) {
					return factor * factor2;
				} else if ("/".equals(op)) {
					return factor / factor2;
				} else {
					throw new RuntimeException("Unsupported operator: " + op);
				}
			}
		}).alt(LEFTPAREN, expr, RIGHTPAREN).action(new Action() {
			public Object act(Object... params) {
				return params[1];
			}
		}).alt(DIGIT).action(new Action() {
			public Object act(Object... params) {
				return Integer.parseInt((String) params[0]);
			}
		});
		Element addendTail = calculator.addGrammarRule(ADD.or(SUB), term)
				.action(new Action() {
					public Object act(Object... params) {
						return params;
					}
				});
		expr.fillGrammarRule(term, addendTail, CC.EOF).action(new Action() {
			public Object act(Object... params) {
				int addend = (Integer) params[0];
				Object[] addendTailReturn = (Object[]) params[1];
				String op = (String) addendTailReturn[0];
				int addend2 = (Integer) addendTailReturn[1];
				if ("+".equals(op)) {
					return addend + addend2;
				} else if ("-".equals(op)) {
					return addend - addend2;
				} else {
					throw new RuntimeException("Unsupported operator: " + op);
				}
			}
		});
		// 1.compile it!
		calculator.compile();
		// 0.FIRE!!!
		System.out.println(calculator.exe("1+2+3+(4+5*6*7*(64/8/2/(2/1)/1)*8+9)+10"));
	}
}

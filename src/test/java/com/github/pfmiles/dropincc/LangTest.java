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
    /**
     * <pre>
     * left recursion version: 
     * expr ::= addition EOF;
     * addition ::= addend
     *            | addition (ADD | SUB) addend;
     * addend ::= factor
     *          | addend (MUL | DIV) factor;
     * factor ::= DIGIT
     *          | LEFTPAREN addition RIGHTPAREN;
     * 
     * kleene closure version: 
     * expr ::= addition EOF;
     * addition ::= addend ((AND | SUB) addend)*;
     * addend ::= factor ((MUL | DIV) factor)*;
     * factor ::= DIGIT
     * 			| LEFTPAREN addition RIGHTPAREN;
     * </pre>
     */
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
        Grule addition = calculator.newGrule();
        Grule addend = calculator.newGrule();
        Grule factor = calculator.newGrule();
        Element expr = calculator.addGrammarRule(addition, Tokens.EOF).action(new Action() {
            public Object act(Object... params) {
                return params[0];
            }
        });
        addition.fillGrammarRule(addend, CC.ks((ADD.or(SUB)), addend)).action(new Action() {
            public Object act(Object... params) {
                double leftMost = (Double) params[0];
                Object[] opAndOther = (Object[]) params[1];
                for (int i = 0; i < opAndOther.length; i++) {
                    Object[] opAndOne = (Object[]) opAndOther[i];
                    if ("+".equals(opAndOne[0])) {
                        leftMost += (Double) opAndOne[1];
                    } else if ("-".equals(opAndOne[1])) {
                        leftMost -= (Double) opAndOne[1];
                    } else {
                        throw new RuntimeException("Invalid operator: " + opAndOne[0]);
                    }
                }
                return leftMost;
            }
        });
        addend.fillGrammarRule(factor, CC.ks(MUL.or(DIV), factor)).action(new Action() {
            public Object act(Object... params) {
                double leftMost = (Double) params[0];
                Object[] opAndOthers = (Object[]) params[1];
                for (int i = 0; i < opAndOthers.length; i++) {
                    Object[] opAndOther = (Object[]) opAndOthers[i];
                    if ("*".equals(opAndOthers[0])) {
                        leftMost *= (Double) opAndOthers[1];
                    } else if ("/".equals(opAndOthers[0])) {
                        leftMost /= (Double) opAndOthers[1];
                    } else {
                        throw new RuntimeException("Invalid operator: " + opAndOther[0]);
                    }
                }
                return leftMost;
            }
        });
        factor.fillGrammarRule(DIGIT).action(new Action() {
            public Object act(Object... params) {
                return Double.parseDouble((String) params[0]);
            }
        }).alt(LEFTPAREN, addition, RIGHTPAREN).action(new Action() {
            public Object act(Object... params) {
                return (Double) params[1];
            }
        });
        // 1.compile it!
        calculator.compile();
        // 0.FIRE!!!
        System.out.println(calculator.exe("1+2+3+(4+5*6*7*(64/8/2/(2/1)/1)*8+9)+10"));
    }
}

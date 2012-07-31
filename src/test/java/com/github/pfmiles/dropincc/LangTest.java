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
 * 
 * @author pf-miles
 * 
 */
public class LangTest extends TestCase {
    /**
     * <pre>
     * calc ::= expr $
     * expr ::= addend (('+'|'-') addend)*
     * addend ::= factor (('*'|'/') factor)*
     * factor ::= '(' expr ')'
     *          | '\\d+(\\.\\d+)?'
     * </pre>
     */
    public void testCalculator() {
        Lang calc = new Lang("Calculator");
        Grule expr = calc.newGrule();
        calc.defineGrule(expr, CC.EOF).action(new Action() {
            public Double act(Object matched) {
                return (Double) ((Object[]) matched)[0];
            }
        });
        TokenDef a = calc.newToken("\\+");
        Grule addend = calc.newGrule();
        expr.define(addend, CC.ks(a.or("\\-"), addend)).action(new Action() {
            public Double act(Object matched) {
                Object[] ms = (Object[]) matched;
                Double a0 = (Double) ms[0];
                Object[] aPairs = (Object[]) ms[1];
                for (Object p : aPairs) {
                    String op = (String) ((Object[]) p)[0];
                    Double a = (Double) ((Object[]) p)[1];
                    if ("+".equals(op)) {
                        a0 += a;
                    } else {
                        a0 -= a;
                    }
                }
                return a0;
            }
        });
        TokenDef m = calc.newToken("\\*");
        Grule factor = calc.newGrule();
        addend.define(factor, CC.ks(m.or("/"), factor)).action(new Action() {
            public Double act(Object matched) {
                Object[] ms = (Object[]) matched;
                Double f0 = (Double) ms[0];
                Object[] fPairs = (Object[]) ms[1];
                for (Object p : fPairs) {
                    String op = (String) ((Object[]) p)[0];
                    Double f = (Double) ((Object[]) p)[1];
                    if ("*".equals(op)) {
                        f0 *= f;
                    } else {
                        f0 /= f;
                    }
                }
                return f0;
            }
        });
        factor.define("\\(", expr, "\\)").action(new Action() {
            public Double act(Object matched) {
                return (Double) ((Object[]) matched)[1];
            }
        }).alt("\\d+(\\.\\d+)?").action(new Action() {
            public Double act(Object matched) {
                return Double.parseDouble((String) matched);
            }
        });
        Exe exe = calc.compile();
        assertTrue(3389 == exe.<Double> eval("1+2+3+(4+5*6*7*(64/8/2/(2/1)/1)*8+9)+10"));
    }
}

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

import com.github.pfmiles.dropincc.calctest.Calculator;

/**
 * 
 * @author pf-miles
 * 
 */
public class LangTest extends TestCase {
    /**
     * A basic calculator test
     * 
     */
    public void testCalculator() {
        assertTrue(3389 == Calculator.compute("1   +2+3+(4   +5*6*7*(64/8/2/(2/1   )/1)*8   +9  )+   10"));
    }

    public void testIllegalToken() {
        try {
            Calculator.compute("1 +2+3^4");
            assertTrue(false);
        } catch (DropinccException e) {
            System.out.println("Error msg for test: " + e);
            assertTrue(true);
        }
    }

    public void testSyntaxErr() {
        try {
            Calculator.compute("1+2* \n (5+10.2 * + 7)*4");
            assertTrue(false);
        } catch (DropinccException e) {
            System.out.println("Error msg for test: " + e);
            assertTrue(true);
        }
    }

    /**
     * <pre>
     * S ::= A $
     * A ::= a
     *     |
     * </pre>
     */
    public void testEmptyAlternative() {
        // normal empty alt
        Lang lang = new Lang();
        Grule A = lang.newGrule();
        lang.defineGrule(A, CC.EOF);
        A.define("a").alt(CC.NOTHING);
        Exe exe = lang.compile();
        assertTrue("a".equals(((Object[]) exe.eval("a"))[0]));

        // empty single alt
        lang = new Lang();
        A = lang.newGrule();
        lang.defineGrule(A, CC.EOF);
        A.define(CC.NOTHING);
        exe = lang.compile();
        assertTrue(((Object[]) exe.eval(""))[0] == null);

        // with action
        // empty single alt
        lang = new Lang();
        A = lang.newGrule();
        lang.defineGrule(A, CC.EOF);
        A.define(CC.NOTHING).action(new Action() {
            public Object act(Object matched) {
                assertTrue(matched == null);
                return matched;
            }
        });
        exe = lang.compile();
        assertTrue(((Object[]) exe.eval(""))[0] == null);

        // normal empty alt
        lang = new Lang();
        A = lang.newGrule();
        lang.defineGrule(A, CC.EOF);
        A.define("a").action(new Action() {
            public Object act(Object matched) {
                assertTrue("a".equals(matched));
                return matched;
            }
        }).alt(CC.NOTHING).action(new ParamedAction<Object>() {
            public Object act(Object arg, Object matched) {
                assertTrue(matched == null);
                return matched;
            }
        });
        exe = lang.compile();
        assertTrue("a".equals(((Object[]) exe.eval("a"))[0]));
    }
}

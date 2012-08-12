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
 * Test various backtracking situations
 * 
 * @author pf-miles
 * 
 */
public class BacktrackTest extends TestCase {
    /**
     * <pre>
     * S ::= A $
     * A ::= (B c)* B d
     * B ::= b
     *     | e B f
     * </pre>
     */
    public void testBasicBacktrackKs() {
        Lang lang = new Lang("Test");
        Grule A = lang.newGrule();
        lang.defineGrule(A, CC.EOF);
        Grule B = lang.newGrule();
        A.define(CC.ks(B, "c"), B, "d");
        B.define("b").action(new Action<Object>() {
            private int count;

            public Object act(Object matched) {
                count++;
                assertTrue(count <= 3);
                // System.out.println("b count:" + count);
                return matched;
            }
        }).alt("e", B, "f").action(new Action<Object>() {
            private int count;

            public Object act(Object matched) {
                count++;
                assertTrue(count <= 9);
                // System.out.println("ef count:" + count);
                return matched;
            }
        });

        Exe exe = lang.compile();
        exe.eval("eebffceeebfffceeeebffffd");
    }

    /**
     * <pre>
     * S ::= A $
     * A ::= (B c)+ B d
     * B ::= b
     *     | e B f
     * </pre>
     */
    public void testBasicBacktrackKc() {
        Lang lang = new Lang("Test");
        Grule A = lang.newGrule();
        lang.defineGrule(A, CC.EOF);
        Grule B = lang.newGrule();
        A.define(CC.kc(B, "c"), B, "d");
        B.define("b").action(new Action<Object>() {
            private int count;

            public Object act(Object matched) {
                count++;
                assertTrue(count <= 3);
                // System.out.println("b count:" + count);
                return matched;
            }
        }).alt("e", B, "f").action(new Action<Object>() {
            private int count;

            public Object act(Object matched) {
                count++;
                assertTrue(count <= 9);
                // System.out.println("ef count:" + count);
                return matched;
            }
        });

        Exe exe = lang.compile();
        exe.eval("eebffceeebfffceeeebffffd");
    }

    /**
     * <pre>
     * S ::= A $
     * A ::= (B c)? B d
     * B ::= b
     *     | e B f
     * </pre>
     */
    public void testBasicBacktrackOp() {
        Lang lang = new Lang("Test");
        Grule A = lang.newGrule();
        lang.defineGrule(A, CC.EOF);
        Grule B = lang.newGrule();
        A.define(CC.op(B, "c"), B, "d");
        B.define("b").action(new Action<Object>() {
            private int count;

            public Object act(Object matched) {
                count++;
                assertTrue(count == 1);
                // System.out.println("b count:" + count);
                return matched;
            }
        }).alt("e", B, "f").action(new Action<Object>() {
            private int count;

            public Object act(Object matched) {
                count++;
                assertTrue(count <= 4);
                // System.out.println("ef count:" + count);
                return matched;
            }
        });

        Exe exe = lang.compile();
        exe.eval("eeeebffffd");
    }
}

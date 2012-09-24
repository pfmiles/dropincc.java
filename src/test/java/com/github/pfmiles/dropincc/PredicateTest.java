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
 * Some test cases for using semantic predicates.
 * 
 * @author pf-miles
 * 
 */
public class PredicateTest extends TestCase {
    /**
     * Typical predicate test
     * 
     * <pre>
     * S ::= A $
     * A ::= {pred2?} b* c
     *     | {pred3?} b* c
     * </pre>
     */
    public void testLookAhead() {
        Lang lang = new Lang("Test");
        Grule A = lang.newGrule();

        lang.defineGrule(A, CC.EOF).action(new Action<Object[]>() {
            public Integer act(Object[] matched) {
                return (Integer) matched[0];
            }
        });
        A.define(CC.ks("b"), "c").pred(new Predicate<Integer>() {
            public boolean pred(Integer arg, LookAhead la) {
                // skip any 'b' before seeing an 'c'
                int i = 1;
                String word = la.ahead(i);
                while (true) {
                    if (word == null)
                        return false;
                    if (word.equals("b")) {
                        i++;
                        word = la.ahead(i);
                        continue;
                    }
                    if (word.equals("c")) {
                        // context relative
                        return arg == 0;
                    } else {
                        return false;
                    }
                }
            }
        }).action(new Action<Object>() {
            public Integer act(Object matched) {
                return 0;
            }
        }).alt(CC.ks("b"), "c").pred(new Predicate<Integer>() {
            public boolean pred(Integer arg, LookAhead la) {
                // skip any 'b' before seeing an 'c'
                int i = 1;
                String word = la.ahead(i);
                while (true) {
                    if (word == null)
                        return false;
                    if (word.equals("b")) {
                        i++;
                        word = la.ahead(i);
                        continue;
                    }
                    if (word.equals("c")) {
                        // context relative
                        return arg == 1;
                    } else {
                        return false;
                    }
                }
            }
        }).action(new Action<Object>() {
            public Integer act(Object matched) {
                return 1;
            }
        });
        Exe exe = lang.compile();
        try {
            // illegal sequence
            exe.eval("bbbbbbba", 1);
            assertTrue(false);
        } catch (DropinccException e) {
            assertTrue(true);
        }
        try {
            // illegal context
            exe.eval("bbbbbbbc", -1);
            assertTrue(false);
        } catch (DropinccException e) {
            assertTrue(true);
        }
        try {
            // illegal context
            exe.eval("bbbbbbba", 2);
            assertTrue(false);
        } catch (DropinccException e) {
            assertTrue(true);
        }
        // correct
        assertTrue(exe.eval("bbbbbbbc", 1).equals(1));
        assertTrue(exe.eval("bbbbbbbc", 0).equals(0));
    }
}

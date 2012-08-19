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

import com.github.pfmiles.dropincc.testhelper.IccActionManager;

/**
 * Backtrack & non-backtrack rule, backtrack & non-backtrack kleene, nested,
 * complex situation tests. Action fire count is important.
 * 
 * @author pf-miles
 * 
 */
public class ComplexBacktrackTest extends TestCase {
    // NBR - BR
    /**
     * <pre>
     * S ::= A $
     * A ::= c b
     *     | d B
     * B ::= C e
     *     | C f
     * C ::= g
     *     | h C i
     * </pre>
     */
    public void testComplexBacktrack1() {
        IccActionManager mgr = new IccActionManager();
        Lang lang = new Lang("Test");
        Grule A = lang.newGrule();
        Grule B = lang.newGrule();
        Grule C = lang.newGrule();

        lang.defineGrule(A, CC.EOF).action(mgr.newCheck(1, 2));
        A.define("c", "b").action(mgr.newCheck(0, 0)).alt("d", B).action(mgr.newCheck(1, 2));
        B.define(C, "e").action(mgr.newCheck(0, 0)).alt(C, "f").action(mgr.newCheck(1, 2));
        C.define("g").action(mgr.newCheck(1, -1)).alt("h", C, "i").action(mgr.newCheck(3, 3));

        Exe exe = lang.compile();
        exe.eval("dhhhgiiif");

        mgr.checkFinalCounts();
    }

    // BR - NBK
    /**
     * <pre>
     * S ::= A $
     * A ::= a* B c
     *     | a+ B d
     *     | a? B e
     * B ::= f
     *     | g B h
     * </pre>
     */
    public void testComplexBacktrack2() {
        IccActionManager mgr = new IccActionManager();
        Lang lang = new Lang("Test");
        Grule A = lang.newGrule();
        Grule B = lang.newGrule();

        lang.defineGrule(A, CC.EOF).action(mgr.newCheck(1, 2));
        A.define(CC.ks("a"), B, "c").action(mgr.newCheck(0, 0)).alt(CC.kc("a"), B, "d").action(mgr.newCheck(0, 0)).alt(CC.op("a"), B, "e").action(mgr.newCheck(1, 3));
        B.define("f").action(mgr.newCheck(1, -1)).alt("g", B, "h").action(mgr.newCheck(3, 3));

        Exe exe = lang.compile();
        exe.eval("agggfhhhe");

        mgr.checkFinalCounts();
    }

    // BR - BK
    /**
     * <pre>
     * S ::= A $
     * A ::= B (B i)* B j 
     *     | B (B i)+ B k
     * B ::= e
     *     | f B g
     * </pre>
     */
    public void testComplexBacktrack4() {
        IccActionManager mgr = new IccActionManager();
        Lang lang = new Lang("Test");
        Grule A = lang.newGrule();
        Grule B = lang.newGrule();

        lang.defineGrule(A, CC.EOF).action(mgr.newCheck(1, 2));
        A.define(B, CC.ks(B, "i"), B, "j").action(mgr.newCheck(0, 0)).alt(B, CC.kc(B, "i"), B, "k").action(mgr.newCheck(1, 4));

        B.define("e").action(mgr.newCheck(5, -1)).alt("f", B, "g").action(mgr.newCheck(10, 3));

        Exe exe = lang.compile();
        exe.eval("efegiffeggifffegggiffffeggggk");

        mgr.checkFinalCounts();
    }

    /**
     * <pre>
     * S ::= A $
     * A ::= B (B i)* B j 
     *     | B (B i)+ B k
     * B ::= e
     *     | f B g
     * </pre>
     */
    public void testComplexBacktrack4b1() {
        IccActionManager mgr = new IccActionManager();
        Lang lang = new Lang("Test");
        Grule A = lang.newGrule();
        Grule B = lang.newGrule();

        lang.defineGrule(A, CC.EOF).action(mgr.newCheck(1, 2));
        A.define(B, CC.ks(B, "i"), B, "j").action(mgr.newCheck(0, 0)).alt(B, CC.kc(B, "i"), B, "k").action(mgr.newCheck(1, 4));

        B.define("e").action(mgr.newCheck(3, -1)).alt("f", B, "g").action(mgr.newCheck(1, 3));

        Exe exe = lang.compile();
        exe.eval("efegiek");

        mgr.checkFinalCounts();
    }

    // NBK - BR
    /**
     * <pre>
     * S ::= A $
     * A ::= (b C)* d
     * C ::= B e
     *     | B f
     * B ::= g
     *     | h B i
     * </pre>
     */
    public void testComplexBacktrack5() {
        IccActionManager mgr = new IccActionManager();

        Lang lang = new Lang("Test");
        Grule A = lang.newGrule();
        Grule C = lang.newGrule();
        Grule B = lang.newGrule();

        lang.defineGrule(A, CC.EOF).action(mgr.newCheck(1, 2));
        A.define(CC.ks("b", C), "d").action(mgr.newCheck(1, 2));
        C.define(B, "e").action(mgr.newCheck(2, 2)).alt(B, "f").action(mgr.newCheck(2, 2));
        B.define("g").action(mgr.newCheck(4, -1)).alt("h", B, "i").action(mgr.newCheck(6, 3));

        Exe exe = lang.compile();
        exe.eval("bgebhgifbhhgiiebhhhgiiifd");

        mgr.checkFinalCounts();
    }

    // BK - BR
    /**
     * <pre>
     * S ::= A $
     * A ::= (C b)* C d
     * C ::= B e
     *     | B f
     * B ::= g
     *     | h B i
     * </pre>
     */
    public void testComplexBacktrack7() {
        IccActionManager mgr = new IccActionManager();
        Lang lang = new Lang("Test");
        Grule A = lang.newGrule();
        Grule C = lang.newGrule();
        Grule B = lang.newGrule();

        lang.defineGrule(A, CC.EOF).action(mgr.newCheck(1, 2));
        A.define(CC.ks(C, "b"), C, "d").action(mgr.newCheck(1, 3));
        C.define(B, "e").action(mgr.newCheck(2, 2)).alt(B, "f").action(mgr.newCheck(3, 2));
        B.define("g").action(mgr.newCheck(5, -1)).alt("h", B, "i").action(mgr.newCheck(10, 3));

        Exe exe = lang.compile();
        exe.eval("gfbhgiebhhgiifbhhhgiiiebhhhhgiiiifd");

        mgr.checkFinalCounts();
    }

    // BR - NBR
    /**
     * <pre>
     * S ::= A $
     * A ::= B C d
     *     | B C e
     * B ::= b
     *     | f B g
     * C ::= h
     *     | i
     * </pre>
     */
    public void testComplexBacktrack8() {
        IccActionManager mgr = new IccActionManager();
        Lang lang = new Lang("Test");
        Grule A = lang.newGrule();
        Grule B = lang.newGrule();
        Grule C = lang.newGrule();

        lang.defineGrule(A, CC.EOF).action(mgr.newCheck(1, 2));
        A.define(B, C, "d").action(mgr.newCheck(0, 0)).alt(B, C, "e").action(mgr.newCheck(1, 3));

        B.define("b").action(mgr.newCheck(1, -1)).alt("f", B, "g").action(mgr.newCheck(2, 3));

        C.define("h").action(mgr.newCheck(0, 0)).alt("i").action(mgr.newCheck(1, -1));

        Exe exe = lang.compile();

        exe.eval("ffbggie");

        mgr.checkFinalCounts();
    }

    // NBK - NBR
    /**
     * <pre>
     * S ::= A $
     * A ::= (C d e)+ f g
     * C ::= h
     *     | i
     * </pre>
     */
    public void testComplexBacktrack9() {
        IccActionManager mgr = new IccActionManager();
        Lang lang = new Lang("Test");
        Grule A = lang.newGrule();
        Grule C = lang.newGrule();

        lang.defineGrule(A, CC.EOF).action(mgr.newCheck(1, 2));
        A.define(CC.kc(C, "d", "e"), "f", "g").action(mgr.newCheck(1, 3));
        C.define("h").action(mgr.newCheck(1, -1)).alt("i").action(mgr.newCheck(1, -1));

        Exe exe = lang.compile();
        exe.eval("hdeidefg");

        mgr.checkFinalCounts();
    }
}

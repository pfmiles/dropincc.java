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
 * Complex kleene nodes situation tests.
 * 
 * @author pf-miles
 * 
 */
public class KleeneTest extends TestCase {

    // NBK - NBK
    /**
     * <pre>
     * S ::= A $
     * A ::= (a (b d)+ c)*
     *     | (e (g h)? f)+
     * </pre>
     */
    public void test1() {
        IccActionManager mgr = new IccActionManager();
        Lang lang = new Lang("Test");
        Grule A = lang.newGrule();

        lang.defineGrule(A, CC.EOF).action(mgr.newCheck(1, 2));
        A.define(CC.ks("a", CC.kc("b", "d")), "c").action(mgr.newCheck(0, 0)).alt(CC.kc("e", CC.op("g", "h"), "f")).action(mgr.newCheck(1, 2));

        Exe exe = lang.compile();
        exe.eval("efeghf");

        mgr.checkFinalCounts();
    }

    // NBK - BK
    /**
     * <pre>
     * S ::= A $
     * A ::= (a (b C e)+ b C d)*
     * C ::= f
     *     | g C h
     * </pre>
     */
    public void test2() {
        IccActionManager mgr = new IccActionManager();
        Lang lang = new Lang("Test");
        Grule A = lang.newGrule();
        Grule C = lang.newGrule();

        lang.defineGrule(A, CC.EOF).action(mgr.newCheck(1, 2));
        A.define(CC.ks("a", CC.kc("b", C, "e"), "b", C, "d")).action(mgr.newCheck(1, 2));
        C.define("f").action(mgr.newCheck(7, -1)).alt("g", C, "h").action(mgr.newCheck(11, 3));

        Exe exe = lang.compile();
        exe.eval("abfebgfhebfdabggfhhebgggfhhhebggggfhhhhebgfhd");

        mgr.checkFinalCounts();
    }

    // BK - NBK
    /**
     * <pre>
     * S ::= A $
     * A ::= (B (e f)+ d)* B c
     * B ::= g
     *     | h B i
     * </pre>
     */
    public void test3() {
        IccActionManager mgr = new IccActionManager();
        Lang lang = new Lang("Test");
        Grule A = lang.newGrule();
        Grule B = lang.newGrule();

        lang.defineGrule(A, CC.EOF).action(mgr.newCheck(1, 2));

        A.define(CC.ks(B, CC.kc("e", "f"), "d"), B, "c").action(mgr.newCheck(1, 3));

        B.define("g").action(mgr.newCheck(4, -1)).alt("h", B, "i").action(mgr.newCheck(7, 3));

        Exe exe = lang.compile();

        exe.eval("hgiefdhhgiiefefdhhhgiiiefefefdhgic");
        mgr.checkFinalCounts();
    }

    // BK-BK
    /**
     * <pre>
     * S ::= A $
     * A ::= (B (B c)? B d)+ B c
     * B ::= e
     *     | f B g
     * </pre>
     */
    public void test4() {
        IccActionManager mgr = new IccActionManager();
        Lang lang = new Lang("Test");
        Grule A = lang.newGrule();
        Grule B = lang.newGrule();

        lang.defineGrule(A, CC.EOF).action(mgr.newCheck(1, 2));
        A.define(CC.kc(B, CC.op(B, "c"), B, "d"), B, "c").action(mgr.newCheck(1, 3));
        B.define("e").action(mgr.newCheck(6, -1)).alt("f", B, "g").action(mgr.newCheck(15, 3));

        Exe exe = lang.compile();
        exe.eval("efegdffeggfffegggcffffeggggdfffffegggggc");

        mgr.checkFinalCounts();
    }

    /**
     * <pre>
     * S ::= A $
     * A ::= (B (B c)? B d)+ B c
     * B ::= e
     *     | f B g
     * </pre>
     */
    public void test4b1() {
        IccActionManager mgr = new IccActionManager();
        Lang lang = new Lang("Test");
        Grule A = lang.newGrule();
        Grule B = lang.newGrule();

        lang.defineGrule(A, CC.EOF).action(mgr.newCheck(1, 2));
        A.define(CC.kc(B, CC.op(B, "c"), B, "d"), B, "c").action(mgr.newCheck(1, 3));
        B.define("e").action(mgr.newCheck(3, -1)).alt("f", B, "g").action(mgr.newCheck(1, 3));

        Exe exe = lang.compile();
        exe.eval("eedfegc");

        mgr.checkFinalCounts();
    }

    /**
     * <pre>
     * S ::= A $
     * A ::= (a b)*
     * </pre>
     */
    public void testKsNoMatch() {
        IccActionManager mgr = new IccActionManager();
        Lang lang = new Lang("Test");
        Grule A = lang.newGrule();

        lang.defineGrule(A, CC.EOF).action(mgr.newCheck(1, 2));
        A.define(CC.ks("a", "b")).action(mgr.newCheck(1, 0));

        Exe exe = lang.compile();
        exe.eval("");

        mgr.checkFinalCounts();
    }

    /**
     * <pre>
     * S ::= A $
     * A ::= (a b)?
     * </pre>
     */
    public void testOpNoMatch() {
        IccActionManager mgr = new IccActionManager();
        Lang lang = new Lang("Test");
        Grule A = lang.newGrule();
        lang.defineGrule(A, CC.EOF).action(mgr.newCheck(1, 2));
        A.define(CC.op("a", "b")).action(mgr.newCheck(1, -2));
    }
}

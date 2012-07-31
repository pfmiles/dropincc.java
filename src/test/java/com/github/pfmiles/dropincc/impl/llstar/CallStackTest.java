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
package com.github.pfmiles.dropincc.impl.llstar;

import junit.framework.TestCase;

/**
 * @author pf-miles
 * 
 */
public class CallStackTest extends TestCase {
    public void testEquality() {
        CallStack c1 = new CallStack();
        CallStack c2 = new CallStack();

        // two stack same
        c1.push(new AtnState("p1", false));
        c1.push(new AtnState("p2", false));

        c2.push(new AtnState("p1", false));
        c2.push(new AtnState("p2", false));

        assertTrue(c1.equals(c2));

        // c1 empty
        c1 = new CallStack();
        c2 = new CallStack();

        c2.push(new AtnState("p1", false));
        c2.push(new AtnState("p2", false));

        assertTrue(c1.equals(c2));

        // c2 empty
        c1 = new CallStack();
        c2 = new CallStack();

        c1.push(new AtnState("p1", false));
        c1.push(new AtnState("p2", false));

        assertTrue(c1.equals(c2));

        // c2 is suffix of c1
        c1 = new CallStack();
        c2 = new CallStack();

        c1.push(new AtnState("p1", false));
        c1.push(new AtnState("p2", false));
        c1.push(new AtnState("p3", false));

        c2.push(new AtnState("p2", false));
        c2.push(new AtnState("p3", false));

        assertTrue(c1.equals(c2));

        // c1 is suffix of c1
        c1 = new CallStack();
        c2 = new CallStack();

        c2.push(new AtnState("p1", false));
        c2.push(new AtnState("p2", false));
        c2.push(new AtnState("p3", false));

        c1.push(new AtnState("p2", false));
        c1.push(new AtnState("p3", false));

        assertTrue(c1.equals(c2));
    }

    public void testClone() {
        CallStack c1 = new CallStack();

        c1.push(new AtnState("p1", false));
        c1.push(new AtnState("p2", false));
        c1.push(new AtnState("p3", false));

        CallStack c2 = c1.clone();
        c2.pop();
        assertTrue(!c1.equals(c2));
    }
}

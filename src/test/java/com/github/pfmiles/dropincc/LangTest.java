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
        assertTrue(3389 == Calculator.compute("1+2+3+(4+5*6*7*(64/8/2/(2/1)/1)*8+9)+10"));
    }
}

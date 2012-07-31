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
package com.github.pfmiles.dropincc.impl.syntactical.codegen;

import junit.framework.TestCase;

/**
 * @author pf-miles
 * 
 */
public class CodeGenTest extends TestCase {
    public void testRender() {
        TestTemplate t = new TestTemplate();
        assertTrue(t.render(null).equals("This is a hello {test world template.}"));
    }

    public void testParserClsRender() {
        ParserClsTemplate t = new ParserClsTemplate();
        String rst = t.render(null);
        // System.out.println(rst);
        assertTrue(rst != null);
    }
}

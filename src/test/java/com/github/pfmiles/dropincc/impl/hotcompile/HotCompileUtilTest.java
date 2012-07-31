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
package com.github.pfmiles.dropincc.impl.hotcompile;

import junit.framework.TestCase;

/**
 * @author pf-miles
 * 
 */
public class HotCompileUtilTest extends TestCase {
    public void testCompile() throws Exception {
        String code = "package test;\n" + "public class Test {\n" + "public static void main(String... args) throws Throwable {\n"
                + "System.out.println(\"Hello World.\");\n" + "}\n" + "};\n";
        CompilationResult rst = HotCompileUtil.compile("test.Test", code);
        if (!rst.isSucceed()) {
            assertTrue(false);
        } else {
            assertTrue(rst.getCls() != null);
        }
    }
}

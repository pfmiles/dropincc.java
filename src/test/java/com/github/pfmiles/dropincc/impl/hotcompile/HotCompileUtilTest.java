package com.github.pfmiles.dropincc.impl.hotcompile;

import junit.framework.TestCase;

/**
 * @author pf-miles
 * 
 */
public class HotCompileUtilTest extends TestCase {
    public void testCompile() {
        String code = "package test;\n" + "public class Test {\n" + "public static void main(String... args) throws Throwable {\n"
                + "System.out.println(\"Hello World.\");\n" + "}\n" + "};\n";
        CompilationResult rst = HotCompileUtil.compile("test.Test", code);
        if (rst != null) {
            System.out.println(rst.getErrMsg());
        }
    }
}

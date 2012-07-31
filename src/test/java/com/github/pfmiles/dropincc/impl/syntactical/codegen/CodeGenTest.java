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

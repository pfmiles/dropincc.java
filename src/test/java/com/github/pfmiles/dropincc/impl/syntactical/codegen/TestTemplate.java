package com.github.pfmiles.dropincc.impl.syntactical.codegen;

/**
 * @author pf-miles
 * 
 */
public class TestTemplate extends CodeGen {

    @SuppressWarnings("unchecked")
    public String render(CodeGenContext context) {
        return this.getTemplate("testTemp.dt").format(new String[] { "hello", "world" });
    }

}

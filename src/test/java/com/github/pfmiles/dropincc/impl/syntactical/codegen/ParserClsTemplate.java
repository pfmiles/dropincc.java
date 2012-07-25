package com.github.pfmiles.dropincc.impl.syntactical.codegen;

/**
 * @author pf-miles
 * 
 */
public class ParserClsTemplate extends CodeGen {

    public String render(CodeGenContext context) {
        return this.getTemplate("parserCls.dt")
                .format(new String[] { "className", "tokenTypes", "alts' actions", "preds", "startRule", "ruleMethods", "alts' predicting methods",
                        "kleene predicting methods" });
    }

}

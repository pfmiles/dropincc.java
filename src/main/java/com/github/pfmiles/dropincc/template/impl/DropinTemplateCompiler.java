package com.github.pfmiles.dropincc.template.impl;

import com.github.pfmiles.dropincc.template.impl.ast.TemplateNode;
import com.github.pfmiles.dropincc.template.impl.visitor.DropinTemplateToJavaVisitor;

/**
 * Compile template string to java source string.
 * 
 * @author pf-miles
 * 
 */
public class DropinTemplateCompiler {

    /**
     * Compile template string to java source string.
     * 
     * @param tempStr
     * @return
     */
    public static String compile(String tempStr) {
        TemplateNode temp = new DropinTemplateParser(new DropinTemplateLexer(tempStr)).parse();
        return temp.accept(new DropinTemplateToJavaVisitor(), null);
    }

}

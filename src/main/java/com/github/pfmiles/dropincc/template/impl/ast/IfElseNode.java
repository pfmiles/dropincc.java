package com.github.pfmiles.dropincc.template.impl.ast;

import com.github.pfmiles.dropincc.template.impl.DtVisitor;

/**
 * A if-else code block in a template.OF
 * 
 * @author pf-miles
 * 
 */
public class IfElseNode implements RenderableNode<Object> {

    public <T> T accept(DtVisitor visitor, Object param) {
        return visitor.visitIfElse(this, param);
    }

}

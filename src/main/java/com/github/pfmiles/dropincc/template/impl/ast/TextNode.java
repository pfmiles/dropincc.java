package com.github.pfmiles.dropincc.template.impl.ast;

import com.github.pfmiles.dropincc.template.impl.DtVisitor;

/**
 * A plain text block in a template.
 * 
 * @author pf-miles
 * 
 */
public class TextNode implements RenderableNode<Object> {

    public <T> T accept(DtVisitor visitor, Object param) {
        return visitor.visitText(this, param);
    }

}

package com.github.pfmiles.dropincc.template.impl.ast;

import com.github.pfmiles.dropincc.template.impl.DtVisitor;

/**
 * A 'for-each' code block in a template
 * 
 * @author pf-miles
 * 
 */
public class ForEachNode implements RenderableNode<Object> {

    public <T> T accept(DtVisitor visitor, Object param) {
        return visitor.visitForEach(this, param);
    }

}

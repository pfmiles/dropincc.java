package com.github.pfmiles.dropincc.template.impl.ast;

import com.github.pfmiles.dropincc.template.impl.DtVisitor;

/**
 * A reference in the template which is about to render a value from the
 * context.
 * 
 * @author pf-miles
 * 
 */
public class RefOutputNode implements RenderableNode<Object> {

    public <T> T accept(DtVisitor visitor, Object param) {
        return visitor.visitRefOutput(this, param);
    }

}

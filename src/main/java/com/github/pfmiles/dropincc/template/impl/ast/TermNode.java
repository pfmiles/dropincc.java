package com.github.pfmiles.dropincc.template.impl.ast;

import com.github.pfmiles.dropincc.template.impl.DtNode;
import com.github.pfmiles.dropincc.template.impl.DtVisitor;

/**
 * One of the terms of a '&&' combination in a bool expression.
 * 
 * @author pf-miles
 * 
 */
public class TermNode implements DtNode<Object> {

    public <T> T accept(DtVisitor visitor, Object param) {
        return visitor.visitTerm(this, param);
    }

}

package com.github.pfmiles.dropincc.template.impl.ast;

import com.github.pfmiles.dropincc.template.impl.DtNode;
import com.github.pfmiles.dropincc.template.impl.DtVisitor;

/**
 * One of the items of a '||' combination in a bool expression.
 * 
 * @author pf-miles
 * 
 */
public class AndExprNode implements DtNode<Object> {

    public <T> T accept(DtVisitor visitor, Object param) {
        return visitor.visitAndExpr(this, param);
    }

}

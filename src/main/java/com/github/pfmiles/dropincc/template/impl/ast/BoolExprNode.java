package com.github.pfmiles.dropincc.template.impl.ast;

import com.github.pfmiles.dropincc.template.impl.DtNode;
import com.github.pfmiles.dropincc.template.impl.DtVisitor;

/**
 * A bool expression node in a if-else statement.
 * 
 * @author pf-miles
 * 
 */
public class BoolExprNode implements DtNode<Object> {

    public <T> T accept(DtVisitor visitor, Object param) {
        return visitor.visitBoolExpr(this, param);
    }

}

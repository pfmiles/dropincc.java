package com.github.pfmiles.dropincc.example.boolexpr.operator;

import com.github.pfmiles.dropincc.example.boolexpr.Operator;

/**
 * @author pf-miles Aug 21, 2012 9:53:00 AM
 */
public class NotInclude implements Operator {

    public Boolean compute(Object left, Object right) {
        return !new Include().compute(left, right);
    }

}

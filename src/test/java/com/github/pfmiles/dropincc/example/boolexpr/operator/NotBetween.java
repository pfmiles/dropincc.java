package com.github.pfmiles.dropincc.example.boolexpr.operator;

import com.github.pfmiles.dropincc.example.boolexpr.Operator;

/**
 * @author pf-miles Aug 20, 2012 5:46:55 PM
 */
public class NotBetween implements Operator {

    public Boolean compute(Object left, Object right) {
        return !new Between().compute(left, right);
    }

}

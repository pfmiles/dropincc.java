package com.github.pfmiles.dropincc.example.boolexpr.operator;

import com.github.pfmiles.dropincc.example.boolexpr.Operator;

/**
 * @author pf-miles Aug 20, 2012 4:32:36 PM
 */
public class NotEquals implements Operator {

    private Equals e = new Equals();

    public Boolean compute(Object left, Object right) {
        return !e.compute(left, right);
    }

}

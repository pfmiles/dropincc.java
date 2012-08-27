package com.github.pfmiles.dropincc.example.boolexpr.operator;

import com.github.pfmiles.dropincc.example.boolexpr.Operator;

/**
 * @author pf-miles Aug 20, 2012 4:23:27 PM
 */
public class Equals implements Operator {

    public Boolean compute(Object left, Object right) {
        if (left == null) {
            if (right == null) {
                return true;
            } else {
                return false;
            }
        } else {
            if (right == null) {
                return false;
            } else {
                if (left instanceof Number && right instanceof Number) {
                    return ((Number) left).doubleValue() == ((Number) right).doubleValue();
                } else {
                    return left.equals(right);
                }
            }
        }
    }

}

package com.github.pfmiles.dropincc.impl.llstar;

/**
 * Constants used in LL(*) analyzing
 * 
 * @author pf-miles
 * 
 */
public interface Constants {
    /**
     * The epsilon transition of ATN
     */
    Object epsilon = new Object() {
        public String toString() {
            return "ε";
        }
    };

    /**
     * maximun recursion depth when doing 'closure' operation in ATN
     */
    int MAX_REC_DEPTH = 2;

}

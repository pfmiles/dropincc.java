package com.github.pfmiles.dropincc.impl.llstar;

/**
 * Utility class to generate integer number sequence...
 * 
 * @author pf-miles
 * 
 */
public class SeqGen {
    private int seed = 0;

    public int next() {
        int ret = this.seed;
        this.seed++;
        return ret;
    }
}

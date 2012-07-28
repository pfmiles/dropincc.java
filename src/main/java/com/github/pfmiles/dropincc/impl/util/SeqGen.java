package com.github.pfmiles.dropincc.impl.util;

/**
 * Utility class to generate integer number sequence...
 * 
 * @author pf-miles
 * 
 */
public class SeqGen {
    private int seed = 0;

    public SeqGen() {
    }

    /**
     * Sequence with a initial start value, the initial value would be the first
     * return value of the 'next' method call
     * 
     * @param init
     */
    public SeqGen(int init) {
        this.seed = init;
    }

    public int next() {
        int ret = this.seed;
        this.seed++;
        return ret;
    }
}

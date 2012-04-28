package com.github.pfmiles.dropincc.impl.util;

/**
 * Do countings... To solve the problem of keep passing a counting integer
 * through multiple methods invocations(primitive integer could not be changed
 * inside a calling method).
 * 
 * @author pf-miles
 * 
 */
public class Counter {
    private int count = 0;

    public Counter() {
    }

    public Counter(int start) {
        this.count = start;
    }

    public void countByOne() {
        this.count++;
    }

    public int getCount() {
        return count;
    }

    public String toString() {
        return String.valueOf(count);
    }
}

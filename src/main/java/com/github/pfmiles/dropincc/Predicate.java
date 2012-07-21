package com.github.pfmiles.dropincc;

/**
 * @author pf-miles
 * 
 */
public interface Predicate {
    /**
     * Predicate logic
     * 
     * @param seemTokens
     * @return true or false
     */
    boolean pred(Object... seemTokens);

    /**
     * In order to produce better human-readable DFA transition images, we'd
     * better also define a proper toString method for each predicate, this
     * won't take much time, right?
     * 
     * @return
     */
    String toString();
}

package com.github.pfmiles.dropincc;

import com.github.pfmiles.dropincc.impl.runtime.impl.Lexer;

/**
 * @author pf-miles
 * 
 */
public interface Predicate {
    /**
     * Predicate logic
     * 
     * @return true or false
     */
    <T> boolean pred(T arg, Lexer lexer);// TODO lexer could do too much

    /**
     * In order to produce better human-readable DFA transition images, we'd
     * better also define a proper toString method for each predicate, this
     * won't take much time, right?
     * 
     * @return
     */
    String toString();
}

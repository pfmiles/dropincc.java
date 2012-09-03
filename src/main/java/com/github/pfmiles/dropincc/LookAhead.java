package com.github.pfmiles.dropincc;

/**
 * Used in semantic predicates, to look-ahead any further token.
 * 
 * @author pf-miles
 * 
 */
public interface LookAhead {

    /**
     * Find the token lexeme ahead by count specified.
     * 
     * @param count
     * @return
     */
    public <T> T ahead(int count);
}

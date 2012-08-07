package com.github.pfmiles.dropincc.impl.llstar;

/**
 * Exception thrown when the LL(*) analysis algorithm detects the analyzing
 * grammar rule is not LL-regular. This kind of rule would do backtracking when
 * parsing.
 * 
 * @author pf-miles
 * 
 */
public class NonLlRegularException extends Exception {

    private static final long serialVersionUID = -237505078099008792L;

}

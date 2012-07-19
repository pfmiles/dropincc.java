package com.github.pfmiles.dropincc.impl;

/**
 * @author pf-miles
 * 
 */
public class TokenType extends EleType {

    // just for print only
    private String name;

    /**
     * user defined token defIndex starts from 0, special built-in tokens'
     * defIndex is below 0
     * 
     * @param index
     */
    public TokenType(int index, String name) {
        super(index);
        this.name = name;
    }

    public String toString() {
        return name;
    }

}

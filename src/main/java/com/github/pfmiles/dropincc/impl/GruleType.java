package com.github.pfmiles.dropincc.impl;

/**
 * @author pf-miles
 * 
 */
public class GruleType extends EleType {

    /**
     * valid defIndex starts from 0
     * 
     * @param index
     */
    public GruleType(int index) {
        super(index);
    }

    /**
     * Generate a string representation in code generation process.
     * 
     * @return
     */
    public String toCodeGenStr() {
        return "r" + this.defIndex;
    }

}

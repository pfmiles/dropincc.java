package com.github.pfmiles.dropincc.impl.kleene;

/**
 * @author pf-miles
 * 
 */
public class OptionalType extends KleeneType {
    /**
     * valid defIndex starts from 0
     * 
     * @param defIndex
     */
    public OptionalType(int defIndex) {
        super(defIndex);
    }

    public String toCodeGenStr() {
        return "optional" + this.defIndex;
    }

}

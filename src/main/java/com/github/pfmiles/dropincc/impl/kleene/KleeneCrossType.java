package com.github.pfmiles.dropincc.impl.kleene;

/**
 * @author pf-miles
 * 
 */
public class KleeneCrossType extends KleeneType {

    /**
     * valid defIndex starts from 0
     * 
     * @param defIndex
     */
    public KleeneCrossType(int defIndex) {
        super(defIndex);
    }

    public String toCodeGenStr() {
        return "kleeneCross" + this.defIndex;
    }

}

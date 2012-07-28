package com.github.pfmiles.dropincc.impl.kleene;

/**
 * @author pf-miles
 * 
 */
public class KleeneStarType extends KleeneType {

    /**
     * valid defIndex starts from 0
     * 
     * @param defIndex
     */
    public KleeneStarType(int defIndex) {
        super(defIndex);
    }

    public String toCodeGenStr() {
        return "kleeneStar" + this.defIndex;
    }

}

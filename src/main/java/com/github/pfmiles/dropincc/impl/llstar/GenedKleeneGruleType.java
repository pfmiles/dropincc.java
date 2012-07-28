package com.github.pfmiles.dropincc.impl.llstar;

import com.github.pfmiles.dropincc.impl.GruleType;

/**
 * Generated grule type while LL(*) analysis.
 * 
 * @author pf-miles
 * 
 */
public class GenedKleeneGruleType extends GruleType {

    public GenedKleeneGruleType(int index) {
        super(index);
    }

    public String toCodeGenStr() {
        return "kr" + this.defIndex;
    }
}

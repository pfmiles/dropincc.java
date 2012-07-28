package com.github.pfmiles.dropincc.impl.kleene;

import com.github.pfmiles.dropincc.impl.EleType;

/**
 * @author pf-miles
 * 
 */
public abstract class KleeneType extends EleType {
    public KleeneType(int defIndex) {
        super(defIndex);
    }

    /**
     * Generate a string representation in code generation process.
     * 
     * @return
     */
    public abstract String toCodeGenStr();
}

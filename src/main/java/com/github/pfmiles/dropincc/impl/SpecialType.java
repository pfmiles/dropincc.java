package com.github.pfmiles.dropincc.impl;

/**
 * Element type for special grammar elements. Special typed elements are not
 * allowed to be in any grammar rule's productions
 * 
 * @author pf-miles
 * 
 */
public class SpecialType extends EleType {

    /**
     * Internal 'nothing' element type
     */
    public static final SpecialType NOTHING = new SpecialType(0);

    /**
     * valid defIndex starts from 0
     * 
     * @param index
     */
    public SpecialType(int index) {
        super(index);
    }

}

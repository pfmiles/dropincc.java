/*******************************************************************************
 * Copyright (c) 2012 pf_miles.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     pf_miles - initial API and implementation
 ******************************************************************************/
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

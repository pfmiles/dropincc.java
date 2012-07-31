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

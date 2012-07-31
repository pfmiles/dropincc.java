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
package com.github.pfmiles.dropincc.impl.syntactical;

import com.github.pfmiles.dropincc.impl.GruleType;

/**
 * 
 * Represents a generated grule's type
 * 
 * @author pf-miles
 * 
 */
public final class GenedGruleType extends GruleType {

    GenedGruleType(int index) {
        super(index);
    }

    // set GenedGruleType's def index to the merged type's def index
    void setDefIndex(int newIndex) {
        super.defIndex = newIndex;
    }

    public String toCodeGenStr() {
        return "gr" + this.defIndex;
    }

}

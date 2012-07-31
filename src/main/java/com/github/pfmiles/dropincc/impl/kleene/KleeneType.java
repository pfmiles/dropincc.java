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

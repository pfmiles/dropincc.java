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
package com.github.pfmiles.dropincc.impl.util;

/**
 * Utility class to generate integer number sequence...
 * 
 * @author pf-miles
 * 
 */
public class SeqGen {
    private int seed = 0;

    public SeqGen() {
    }

    /**
     * Sequence with a initial start value, the initial value would be the first
     * return value of the 'next' method call
     * 
     * @param init
     */
    public SeqGen(int init) {
        this.seed = init;
    }

    public int next() {
        int ret = this.seed;
        this.seed++;
        return ret;
    }
}

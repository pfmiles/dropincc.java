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
 * Do countings... To solve the problem of keep passing a counting integer
 * through multiple methods invocations(primitive integer could not be changed
 * inside a calling method).
 * 
 * @author pf-miles
 * 
 */
public class Counter {
    private int count = 0;

    public Counter() {
    }

    public Counter(int start) {
        this.count = start;
    }

    public void countByOne() {
        this.count++;
    }

    public int getCount() {
        return count;
    }

    public String toString() {
        return String.valueOf(count);
    }
}

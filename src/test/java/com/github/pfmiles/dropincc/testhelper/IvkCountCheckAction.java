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
package com.github.pfmiles.dropincc.testhelper;

import com.github.pfmiles.dropincc.Action;

/**
 * @author pf-miles
 * 
 */
public class IvkCountCheckAction implements Action {
    private int limit;
    private int count;

    public IvkCountCheckAction(int limit) {
        this.limit = limit;
    }

    public Object act(Object matched) {
        this.count++;
        if (this.count > this.limit)
            throw new RuntimeException("Action ivk count exceeds the limit, limit: " + this.limit + ", actual: " + this.count);
        return matched;
    }

    public void checkFinalCount() {
        if (count != limit)
            throw new RuntimeException("Final count does not equal to expected, expected: " + this.limit + ", actual: " + this.count);
    }

}

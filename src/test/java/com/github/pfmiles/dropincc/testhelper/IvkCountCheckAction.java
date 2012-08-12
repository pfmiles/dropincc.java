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

import java.lang.reflect.Array;

import com.github.pfmiles.dropincc.Action;

/**
 * @author pf-miles
 * 
 */
public class IvkCountCheckAction implements Action<Object> {
    private int limit;// invoke time limit
    private int count;
    private int matchedLength;// matched sequence length

    public IvkCountCheckAction(int limit, int mlength) {
        this.limit = limit;
        if (mlength < -2)
            throw new RuntimeException("Illegal matched length param. -2 for null, -1 for single object, other non-negative values for length of matched array.");
        this.matchedLength = mlength;
    }

    public Object act(Object matched) {
        this.count++;
        if (this.count > this.limit)
            throw new RuntimeException("Action ivk count exceeds the limit, limit: " + this.limit + ", actual: " + this.count);
        if (this.matchedLength == -2) {
            // null
            if (matched != null)
                throw new RuntimeException("The matched sequence is expected to be null but it isn't.");
        } else if (this.matchedLength == -1) {
            // single object (not an array)
            if (matched.getClass().isArray())
                throw new RuntimeException("The matched thing is expected to be a non-array single object but it is an array.");
        } else {
            // array
            if (Array.getLength(matched) != this.matchedLength)
                throw new RuntimeException("The length of matched sequence is not equal to expected. Exp: " + this.matchedLength + ", actual: "
                        + Array.getLength(matched));
        }
        return matched;
    }

    public void checkFinalCount() {
        if (count != limit)
            throw new RuntimeException("Final count does not equal to expected, expected: " + this.limit + ", actual: " + this.count);
    }

}

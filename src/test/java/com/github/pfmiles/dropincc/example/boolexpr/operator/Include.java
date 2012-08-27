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
package com.github.pfmiles.dropincc.example.boolexpr.operator;

import java.util.Set;

import com.github.pfmiles.dropincc.example.boolexpr.Operator;

/**
 * @author pf-miles Aug 21, 2012 9:46:14 AM
 */
public class Include implements Operator {

    public Boolean compute(Object left, Object right) {
        if (left == null)
            return false;
        if (!(left instanceof Set))
            throw new RuntimeException("Left operand of 'include' operator must be of type Set.");
        return ((Set<?>) left).contains(right);
    }

}

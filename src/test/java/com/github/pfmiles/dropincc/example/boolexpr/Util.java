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
package com.github.pfmiles.dropincc.example.boolexpr;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Utility class to hold all business logic of the bool expr.
 * 
 * @author pf-miles Aug 20, 2012 2:42:33 PM
 */
public class Util {

    public static Boolean reduceOrExprs(Boolean b0, Object[] others) {
        for (Object other : others) {
            Boolean b = (Boolean) ((Object[]) other)[1];
            b0 |= b;
        }
        return b0;
    }

    public static Boolean reduceAndExprs(Boolean b0, Object[] others) {
        for (Object other : others) {
            b0 &= (Boolean) ((Object[]) other)[1];
        }
        return b0;
    }

    public static Interval createInterval(boolean leftOpen, Object lower, Object upper, boolean rightOpen) {
        if (lower instanceof Number) {
            if (upper instanceof Number) {
                return new Interval(leftOpen, (Number) lower, (Number) upper, rightOpen);
            } else {
                throw new RuntimeException("Lower and upper bounds of an interval must be of the same type.");
            }
        } else {
            if (upper instanceof Number) {
                throw new RuntimeException("Lower and upper bounds of an interval must be of the same type.");
            } else {
                if (!(lower instanceof Date) || !(upper instanceof Date))
                    throw new RuntimeException("Bounds of an interval must be either of type number or date.");
                return new Interval(leftOpen, (Date) lower, (Date) upper, rightOpen);
            }
        }
    }

    public static Set<Object> buildCollection(Object first, Object[] others) {
        Set<Object> ret = new HashSet<Object>();
        ret.add(first);
        for (Object other : others) {
            Object[] o = (Object[]) other;
            ret.add(o[1]);
        }
        return ret;
    }
}

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

import java.util.Date;

import com.github.pfmiles.dropincc.example.boolexpr.Interval;
import com.github.pfmiles.dropincc.example.boolexpr.Operator;

/**
 * @author pf-miles Aug 20, 2012 4:39:35 PM
 */
public class Between implements Operator {

    public Boolean compute(Object left, Object right) {
        if (!(right instanceof Interval))
            throw new RuntimeException("Right operand of 'between' operator must be an Interval.");
        Interval in = (Interval) right;
        if (left instanceof Number) {
            if (!in.isNumberInterval())
                throw new RuntimeException("Number value could not be compared to non-number intervals.");
            return between((Comparable<Object>) left, in);
        } else if (left instanceof Date) {
            if (!in.isDateInterval())
                throw new RuntimeException("Date value could not be compared to non-date intervals.");
            return between((Comparable<Object>) left, in);
        } else {
            throw new RuntimeException("Left operand of 'between' operator must be a number or date.");
        }
    }

    private static Boolean between(Comparable<Object> left, Interval in) {
        double lowerComp = 0;
        double upperComp = 0;
        if (in.isNumberInterval()) {
            lowerComp = ((Number) left).doubleValue() - in.getNlower().doubleValue();
            upperComp = ((Number) left).doubleValue() - in.getNupper().doubleValue();
        } else {
            lowerComp = left.compareTo(in.getDlower());
            upperComp = left.compareTo(in.getDupper());
        }
        if (in.isLeftOpen()) {
            if (in.isRightOpen()) {
                return lowerComp > 0 && upperComp < 0;
            } else {
                return lowerComp > 0 && upperComp <= 0;
            }
        } else {
            if (in.isRightOpen()) {
                return lowerComp >= 0 && upperComp < 0;
            } else {
                return lowerComp >= 0 && upperComp <= 0;
            }
        }
    }

}

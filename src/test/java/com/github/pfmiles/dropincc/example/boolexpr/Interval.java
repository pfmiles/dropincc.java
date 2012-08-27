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

/**
 * Date or num interval.
 * 
 * @author pf-miles Aug 20, 2012 4:55:02 PM
 */
public class Interval {

    private boolean leftOpen;
    private boolean rightOpen;
    private Number nlower;
    private Number nupper;
    private Date dlower;
    private Date dupper;

    public Interval(boolean leftOpen, Number nlower, Number nupper, boolean rightOpen) {
        this.leftOpen = leftOpen;
        this.nlower = nlower;
        this.nupper = nupper;
        this.rightOpen = rightOpen;
    }

    public Interval(boolean leftOpen, Date dlower, Date dupper, boolean rightOpen) {
        this.leftOpen = leftOpen;
        this.dlower = dlower;
        this.dupper = dupper;
        this.rightOpen = rightOpen;
    }

    public boolean isNumberInterval() {
        return nlower != null;
    }

    public boolean isDateInterval() {
        return dlower != null;
    }

    public boolean isLeftOpen() {
        return leftOpen;
    }

    public boolean isRightOpen() {
        return rightOpen;
    }

    public Number getNlower() {
        return nlower;
    }

    public Number getNupper() {
        return nupper;
    }

    public Date getDlower() {
        return dlower;
    }

    public Date getDupper() {
        return dupper;
    }

}

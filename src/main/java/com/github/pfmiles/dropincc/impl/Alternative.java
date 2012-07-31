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
package com.github.pfmiles.dropincc.impl;

import java.util.ArrayList;
import java.util.List;

import com.github.pfmiles.dropincc.DropinccException;
import com.github.pfmiles.dropincc.Element;
import com.github.pfmiles.dropincc.Predicate;

/**
 * A rule alternative, for internal implementation usage only.
 * 
 * @author pf-miles
 * 
 */
public class Alternative {

    private List<Element> elements = new ArrayList<Element>();
    private Object action = null;
    // semantic predicate
    private Predicate pred;

    public Alternative(Element[] eles) {
        switch (checkNull(eles)) {
        case 0:
            // empty alternative, add nothing
            break;
        case 1:
            // illegal
            throw new DropinccException("Null elements among non-null ones, something must be wrong.");
        case 2:
            for (Element e : eles)
                this.elements.add(e);
            break;
        default:
            throw new DropinccException("Impossible.");
        }
    }

    /*
     * 切克闹... Returns 0 if all elements are null; 1 if some of them are, 2 if
     * none of them...
     */
    private int checkNull(Element[] eles) {
        int nullCount = 0;
        for (Element ele : eles)
            if (ele == null)
                nullCount++;
        if (nullCount == eles.length)
            return 0;
        if (nullCount != 0 && nullCount < eles.length)
            return 1;
        if (nullCount == 0)
            return 2;
        return -1;
    }

    public Object getAction() {
        return action;
    }

    public void setAction(Object action) {
        this.action = action;
    }

    public List<Element> getElements() {
        return elements;
    }

    // same hashCode method as Object.class needed
    public int hashCode() {
        return super.hashCode();
    }

    // same equals method as Object.class needed
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    public Predicate getPred() {
        return pred;
    }

    public void setPred(Predicate pred) {
        this.pred = pred;
    }
}

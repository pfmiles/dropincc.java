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
import com.github.pfmiles.dropincc.impl.util.Util;

/**
 * 
 * An alternative sequence of grammar elements. Internal implementation usage
 * only.
 * 
 * @author pf-miles
 * 
 */
public class OrSubRule implements Element {

    private static final long serialVersionUID = -4844644881965256903L;

    private List<Alternative> alts = new ArrayList<Alternative>();

    /**
     * @param ele
     *            this element
     * @param objs
     *            new alt's elements
     */
    public OrSubRule(Element ele, Object[] objs) {
        if (objs == null || objs.length == 0)
            throw new DropinccException("Could not construct empty alternative.");
        this.alts.add(new Alternative(new Element[] { ele }));
        Element[] eles = Util.filterProductionEles(objs);
        this.alts.add(new Alternative(eles));
    }

    public OrSubRule or(Object ele) {
        if (ele == null)
            throw new DropinccException("Could not construct empty alternative.");
        Element[] eles = Util.filterProductionEles(new Object[] { ele });
        if (eles == null || eles.length == 0)
            throw new DropinccException("Could not construct empty alternative.");
        this.alts.add(new Alternative(eles));
        return this;
    }

    public List<Alternative> getAlts() {
        return alts;
    }

    public AndSubRule and(Object ele) {
        return new AndSubRule(this, ele);
    }

    public int hashCode() {
        return super.hashCode();
    }

    public boolean equals(Object obj) {
        return super.equals(obj);
    }

}

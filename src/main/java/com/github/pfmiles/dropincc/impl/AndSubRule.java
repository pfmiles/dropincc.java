package com.github.pfmiles.dropincc.impl;

import java.util.ArrayList;
import java.util.List;

import com.github.pfmiles.dropincc.DropinccException;
import com.github.pfmiles.dropincc.Element;
import com.github.pfmiles.dropincc.impl.util.Util;

/**
 * @author pf-miles
 * 
 */
public class AndSubRule implements Element {

    private static final long serialVersionUID = 1916850762941337426L;

    private List<Alternative> alts = new ArrayList<Alternative>();

    public AndSubRule(Element ele, Object other) {
        if (other == null)
            throw new DropinccException("Could not add null elements.");
        Element[] es = Util.filterProductionEles(new Object[] { other });
        if (es == null || es.length == 0)
            throw new DropinccException("Could not add null elements.");
        this.alts.add(new Alternative(new Element[] { ele, es[0] }));
    }

    public AndSubRule and(Object ele) {
        if (ele == null)
            throw new DropinccException("Could not add null elements.");
        Element[] es = Util.filterProductionEles(new Object[] { ele });
        if (es == null || es.length == 0)
            throw new DropinccException("Could not add null elements.");
        this.alts.get(0).getElements().add(es[0]);
        return this;
    }

    public OrSubRule or(Object... ele) {
        return new OrSubRule(this, ele);
    }

    public List<Alternative> getAlts() {
        return alts;
    }

    public int hashCode() {
        return super.hashCode();
    }

    public boolean equals(Object obj) {
        return super.equals(obj);
    }

}

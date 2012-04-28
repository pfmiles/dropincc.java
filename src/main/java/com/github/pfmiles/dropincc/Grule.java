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
package com.github.pfmiles.dropincc;

import java.util.ArrayList;
import java.util.List;

import com.github.pfmiles.dropincc.impl.Alternative;
import com.github.pfmiles.dropincc.impl.AndSubRule;
import com.github.pfmiles.dropincc.impl.ConstructingGrule;
import com.github.pfmiles.dropincc.impl.OrSubRule;
import com.github.pfmiles.dropincc.impl.util.Util;

/**
 * Grammar rule
 * 
 * @author pf-miles
 * 
 */
public class Grule implements Element {

    private static final long serialVersionUID = 2584938374078652301L;

    // just for display when debuging...
    private String name;

    // protected access privilege makes Grule could only be created via
    // Lang.newGrule() or subClassOfGrule.new Grule()
    protected Grule(int defIndex) {
        this.name = String.valueOf(defIndex);
    }

    private List<Alternative> alts = new ArrayList<Alternative>();

    public AndSubRule and(Element e) {
        if (e == null)
            throw new DropinccException("Could not construct empty alternative.");
        return new AndSubRule(this, e);
    }

    public OrSubRule or(Element e) {
        if (e == null)
            throw new DropinccException("Could not construct empty alternative.");
        return new OrSubRule(this, e);
    }

    public ConstructingGrule fillGrammarRule(Element... eles) {
        if (eles == null || eles.length == 0)
            throw new DropinccException("Could not add empty grammar rule, if you want to add a rule alternative that matches nothing, use CC.NOTHING.");
        eles = Util.filterConstructingGrules(eles);
        this.alts.add(new Alternative(eles));
        return new ConstructingGrule(this);
    }

    /**
     * get all alternatives for this rule
     * 
     * @return
     */
    public List<Alternative> getAlts() {
        return this.alts;
    }

    public void setAlts(List<Alternative> alts) {
        this.alts = alts;
    }

    /**
     * Grule needs exactly the same 'hashCode' method as Object.class has, for
     * 'every individual grule is different from each other even if their
     * containing alts are the same.'
     */
    public int hashCode() {
        return super.hashCode();
    }

    /**
     * Grule needs exactly the same 'equals' method as Object.class has, for
     * 'every individual grule is different from each other even if their
     * containing alts are the same.'
     */
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    public String toString() {
        return "Grule[" + name + "]";
    }

}

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

import java.util.List;

import com.github.pfmiles.dropincc.DropinccException;
import com.github.pfmiles.dropincc.Element;
import com.github.pfmiles.dropincc.Grule;
import com.github.pfmiles.dropincc.Predicate;
import com.github.pfmiles.dropincc.impl.util.Util;

/**
 * 
 * Internal implementation usage only. Represents a grule be on its construction
 * progress.
 * 
 * @author pf-miles
 * 
 */
public class ConstructingGrule implements Element {

    private static final long serialVersionUID = 2757268497271642042L;

    private Grule grule;

    public ConstructingGrule(Grule grule) {
        this.grule = grule;
    }

    public Grule getGrule() {
        return grule;
    }

    /**
     * Add an alternative to current grule.
     * 
     * @param eles
     * @return
     */
    public ConstructingGrule alt(Object... eles) {
        if (eles == null || eles.length == 0)
            throw new DropinccException(
                    "Could not add empty grammar rule, if you want to add a rule alternative that matches nothing, use CC.NOTHING.");
        Element[] elements = Util.filterProductionEles(eles);
        this.grule.getAlts().add(new Alternative(elements));
        return this;
    }

    /**
     * add action to the last alternative of the grule. If the last alternative
     * already has an action, exception thrown.Thus, any alternative could have
     * been added one and only one action.
     * 
     * @param action
     * @return
     */
    public ConstructingGrule action(Object action) {
        List<Alternative> alts = this.grule.getAlts();
        Alternative alt = alts.get(alts.size() - 1);
        if (alt.getAction() != null)
            throw new DropinccException("Any alternative could have one and only one action.");
        alt.setAction(action);
        return this;
    }

    /**
     * Add predicate to the current alternative of this grule. Each alternative
     * could have only one predicate. The predicate is invoked when the
     * look-ahead DFA of this rule could not determine which alternative
     * production should being expanded just looking at the input tokens. Any
     * logic code could be woven in a predicate include the context-sensitive
     * ones(like lookup in the symbol table).
     * 
     * @param pred
     * @return
     */
    public ConstructingGrule pred(Predicate<?> pred) {
        List<Alternative> alts = this.grule.getAlts();
        Alternative alt = alts.get(alts.size() - 1);
        if (alt.getPred() != null) {
            throw new DropinccException("Any alternative could have one and only one predicate.");
        }
        alt.setPred(pred);
        return this;
    }

}

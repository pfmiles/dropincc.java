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

/**
 * Grammar rule
 * 
 * @author pf-miles
 * 
 */
public class Grule implements Element {

	private static final long serialVersionUID = 2584938374078652301L;

	private List<Alternative> alts = new ArrayList<Alternative>();

	public AndSubRule and(Element e) {
		if (e == null)
			throw new DropinccException(
					"Could not construct empty alternative.");
		return new AndSubRule(e);
	}

	public OrSubRule or(Element e) {
		if (e == null)
			throw new DropinccException(
					"Could not construct empty alternative.");
		return new OrSubRule(e);
	}

	public ConstructingGrule fillGrammarRule(Element... eles) {
		if (eles == null || eles.length == 0)
			throw new DropinccException(
					"Could not add empty grammar rule, if you want to add a rule alternative that matches nothing, use CC.NOTHING.");
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
}

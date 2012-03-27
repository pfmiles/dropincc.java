/**
 * 
 */
package com.github.pfmiles.dropincc.impl;

import java.util.ArrayList;
import java.util.List;

import com.github.pfmiles.dropincc.DropinccException;
import com.github.pfmiles.dropincc.Element;

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

	public OrSubRule(Element... ele) {
		if (ele == null)
			throw new DropinccException(
					"Could not construct empty alternative.");
		for (Element e : ele)
			this.alts.add(new Alternative(new Element[] { e }));
	}

	public OrSubRule or(Element ele) {
		if (ele == null)
			throw new DropinccException(
					"Could not construct empty alternative.");
		this.alts.add(new Alternative(new Element[] { ele }));
		return this;
	}

	public List<Alternative> getAlts() {
		return alts;
	}

	public AndSubRule and(Element ele) {
		return new AndSubRule(this, ele);
	}

}

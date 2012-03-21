package com.github.pfmiles.dropincc.impl;

import java.util.ArrayList;
import java.util.List;

import com.github.pfmiles.dropincc.DropinccException;
import com.github.pfmiles.dropincc.Element;

/**
 * @author pf-miles
 * 
 */
public class AndSubRule implements Element {

	private static final long serialVersionUID = 1916850762941337426L;

	private List<Alternative> alts = new ArrayList<Alternative>();

	public AndSubRule(Element ele) {
		if (ele == null)
			throw new DropinccException(
					"Could not construct empty alternative.");
		this.alts.add(new Alternative(new Element[] { ele }));
	}

	public AndSubRule and(Element ele) {
		if (ele == null)
			throw new DropinccException(
					"Could not construct empty alternative.");
		this.alts.add(new Alternative(new Element[] { ele }));
		return this;
	}

}

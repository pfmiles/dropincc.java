/**
 * 
 */
package com.github.pfmiles.dropincc.impl;

import java.util.ArrayList;
import java.util.List;

import com.github.pfmiles.dropincc.Action;
import com.github.pfmiles.dropincc.DropinccException;
import com.github.pfmiles.dropincc.Element;

/**
 * A rule alternative, for internal implementation usage only.
 * 
 * @author pf-miles
 * 
 */
public class Alternative {

	private List<Element> elements = new ArrayList<Element>();
	private Action action = null;

	public Alternative(Element[] eles) {
		if (eles == null || eles.length == 0)
			throw new DropinccException(
					"Could not add empty grammar rule, if you want to add a rule alternative that matches nothing, use CC.NOTHING.");
		for (Element e : eles)
			this.elements.add(e);
	}

	public Action getAction() {
		return action;
	}

	public void setAction(Action action) {
		this.action = action;
	}

	public List<Element> getElements() {
		return elements;
	}

}

package com.github.pfmiles.dropincc.impl;

import java.util.List;

import com.github.pfmiles.dropincc.Action;
import com.github.pfmiles.dropincc.DropinccException;
import com.github.pfmiles.dropincc.Element;
import com.github.pfmiles.dropincc.Grule;
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
	public ConstructingGrule alt(Element... eles) {
		if (eles == null || eles.length == 0)
			throw new DropinccException(
					"Could not add empty grammar rule, if you want to add a rule alternative that matches nothing, use CC.NOTHING.");
		eles = Util.filterConstructingGrules(eles);
		this.grule.getAlts().add(new Alternative(eles));
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
	public ConstructingGrule action(Action action) {
		List<Alternative> alts = this.grule.getAlts();
		Alternative alt = alts.get(alts.size() - 1);
		if (alt.getAction() != null)
			throw new DropinccException(
					"Any alternative could have one and only one action.");
		alt.setAction(action);
		return this;
	}

}

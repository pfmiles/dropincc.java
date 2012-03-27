package com.github.pfmiles.dropincc.impl;

import com.github.pfmiles.dropincc.Grule;

/**
 * 
 * In order to not expose the constructor of Grule to the end user, it's defined
 * as protected. But dropincc.java need to create Grules while rewriting grammar
 * nodes, so GruleCreator is defined to invoking constructor of Grule,
 * internally.
 * 
 * @author pf-miles
 * 
 */
final class GruleCreator extends Grule {

	private static final long serialVersionUID = 7844640022778630030L;

	private GruleCreator() {
	}

	static Grule createGrule() {
		return new GruleCreator();
	}

}

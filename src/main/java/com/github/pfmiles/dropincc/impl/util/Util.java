package com.github.pfmiles.dropincc.impl.util;

import com.github.pfmiles.dropincc.Element;
import com.github.pfmiles.dropincc.impl.ConstructingGrule;

/**
 * @author pf-miles
 * 
 */
public class Util {

	/**
	 * test if the str is null or 0-length string
	 * 
	 * @param regexp
	 * @return
	 */
	public static boolean isEmpty(String str) {
		return null == str || "".equals(str);
	}

	/**
	 * accepts an element array contains ConstructingGrule possibly, returns an
	 * array with no containing ConstructingGrule.
	 * 
	 * @param eles
	 * @return
	 */
	public static Element[] filterConstructingGrules(Element[] eles) {
		if (eles == null)
			return null;
		Element[] eleNoCon = new Element[eles.length];
		for (int i = 0; i < eles.length; i++) {
			if (eles[i].getClass().equals(ConstructingGrule.class)) {
				eleNoCon[i] = ((ConstructingGrule) eles[i]).getGrule();
			} else {
				eleNoCon[i] = eles[i];
			}
		}
		return eleNoCon;
	}

}

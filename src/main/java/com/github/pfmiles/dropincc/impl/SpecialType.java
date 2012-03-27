/**
 * 
 */
package com.github.pfmiles.dropincc.impl;

import com.github.pfmiles.dropincc.DropinccException;

/**
 * Element type for special grammar elements.
 * 
 * @author pf-miles
 * 
 */
public class SpecialType implements EleType {
	private int eleDefIndex = 0; // starts from -1

	public SpecialType(int index) {
		if (index >= 0)
			throw new DropinccException(
					"DefIndex of special element must be less than 0.");
		this.eleDefIndex = index;
	}

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + eleDefIndex;
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SpecialType other = (SpecialType) obj;
		if (eleDefIndex != other.eleDefIndex)
			return false;
		return true;
	}

	public String toString() {
		return "SpecialType [eleDefIndex=" + eleDefIndex + "]";
	}

}

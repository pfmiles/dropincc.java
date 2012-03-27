/**
 * 
 */
package com.github.pfmiles.dropincc.impl;

/**
 * @author pf-miles
 * 
 */
public class GruleType implements EleType {

	private int gruleDefIndex = -1;

	public GruleType(int index) {
		this.gruleDefIndex = index;
	}

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + gruleDefIndex;
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GruleType other = (GruleType) obj;
		if (gruleDefIndex != other.gruleDefIndex)
			return false;
		return true;
	}

	public String toString() {
		return "GruleType [gruleDefIndex=" + gruleDefIndex + "]";
	}

}

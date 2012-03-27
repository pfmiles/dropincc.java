/**
 * 
 */
package com.github.pfmiles.dropincc.impl;

/**
 * @author pf-miles
 * 
 */
public class TokenType implements EleType {

	private int tokenDefIndex = -1;

	public TokenType(int index) {
		this.tokenDefIndex = index;
	}

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + tokenDefIndex;
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TokenType other = (TokenType) obj;
		if (tokenDefIndex != other.tokenDefIndex)
			return false;
		return true;
	}

	public String toString() {
		return "TokenType [tokenDefIndex=" + tokenDefIndex + "]";
	}

}

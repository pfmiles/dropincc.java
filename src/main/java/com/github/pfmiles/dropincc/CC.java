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

/**
 * 
 * This is a utility class that contains a handful of static methods or
 * constants to use.
 * 
 * @author pf-miles
 * 
 */
public class CC {
	/**
	 * end of parsing file(or character stream) token
	 */
	public static final Element EOF = new Element() {
		private static final long serialVersionUID = 9194950534504326943L;
	};

	/**
	 * represents a 'blank' alternative of a grammar rule
	 */
	public static final Element NOTHING = new Element() {
		private static final long serialVersionUID = -897759698260072002L;
	};
}

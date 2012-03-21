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
 * Exception thrown from dropincc APIs.
 * 
 * @author pf-miles
 * 
 */
public class DropinccException extends RuntimeException {

	private static final long serialVersionUID = 7866817922724066070L;

	public DropinccException() {
		super();
	}

	public DropinccException(String message, Throwable cause) {
		super(message, cause);
	}

	public DropinccException(String message) {
		super(message);
	}

	public DropinccException(Throwable cause) {
		super(cause);
	}

}

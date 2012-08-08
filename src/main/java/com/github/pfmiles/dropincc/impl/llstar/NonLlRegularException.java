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
package com.github.pfmiles.dropincc.impl.llstar;

/**
 * Exception thrown when the LL(*) analysis algorithm detects the analyzing
 * grammar rule is not LL-regular. This kind of rule would do backtracking when
 * parsing.
 * 
 * @author pf-miles
 * 
 */
public class NonLlRegularException extends Exception {

    private static final long serialVersionUID = -237505078099008792L;

}

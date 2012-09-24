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
 * Used in semantic predicates, to look-ahead any further token.
 * 
 * @author pf-miles
 * 
 */
public interface LookAhead {

    /**
     * Find the token lexeme ahead by count specified.
     * 
     * @param count
     * @return
     */
    public <T> T ahead(int count);
}

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
 * Parameterized action, it could receive the parameter passed from the outside.
 * 
 * @author pf-miles
 * 
 * @param <T>
 *            the type of the parameter
 */
public interface ParamedAction<T> {
    /**
     * The same as the 'act' metod in com.github.pfmiles.dropincc.Action
     * interface, except that there is an additional 'arg' argument which is
     * pass in from outside when parsing
     * 
     * @param arg
     * @param matched
     * @see com.github.pfmiles.dropincc.Action#act(Object)
     * @return
     */
    public Object act(T arg, Object matched);
}

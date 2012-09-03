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
 * Semantic predicate
 * 
 * @author pf-miles
 * 
 * @param <T>
 *            The type of the passed-in argument
 */
public interface Predicate<T> {
    /**
     * Predicate logic
     * 
     * @return true or false
     */
    boolean pred(T arg, LookAhead la);

    /**
     * In order to produce better human-readable DFA transition images, we'd
     * better also define a proper toString method for each predicate, this
     * won't take much time, right?
     * 
     * @return
     */
    String toString();
}

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
package com.github.pfmiles.dropincc.impl.runtime.impl;

/**
 * 
 * The lexer prototype for runtime copy & use.
 * 
 * @author pf-miles
 * 
 */
public interface LexerPrototype {

    /**
     * Create a enumerable lexer from this prototype.
     * 
     * @param args
     *            any optional args needed by concrete prototype
     *            implementations.
     * @return
     */
    Lexer create(Object... args);

}

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

import com.github.pfmiles.dropincc.LookAhead;

/**
 * @author pf-miles
 * 
 */
public class LookAheadImpl implements LookAhead {
    private Lexer lexer;
    
    public LookAheadImpl(Lexer lexer) {
        this.lexer = lexer;
    }

    @SuppressWarnings("unchecked")
    public <T> T ahead(int count) {
        return (T) lexer.LT(count).getLexeme();
    }

}

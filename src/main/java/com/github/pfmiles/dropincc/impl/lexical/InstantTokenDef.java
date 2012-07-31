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
package com.github.pfmiles.dropincc.impl.lexical;

import com.github.pfmiles.dropincc.TokenDef;

/**
 * Internal use only. Created for instant tokens added while grule definition.
 * 
 * @author pf-miles
 * 
 */
public class InstantTokenDef extends TokenDef {
    private static final long serialVersionUID = -2790101632738907129L;

    public InstantTokenDef(String regexp) {
        super(regexp);
    }

}

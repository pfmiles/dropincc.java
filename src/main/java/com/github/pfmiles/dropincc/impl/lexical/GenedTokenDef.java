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
 * used internally to create additional tokens.
 * 
 * @author pf-miles
 * 
 */
public final class GenedTokenDef extends TokenDef {

    private static final long serialVersionUID = 7972632057092553906L;

    public GenedTokenDef(String regexp) {
        super(regexp);
    }

}

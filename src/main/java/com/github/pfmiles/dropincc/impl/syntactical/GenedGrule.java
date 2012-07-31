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
package com.github.pfmiles.dropincc.impl.syntactical;

import com.github.pfmiles.dropincc.Grule;

/**
 * 
 * In order to not expose the constructor of Grule to the end user, it's defined
 * as protected. But dropincc.java need to create Grules while rewriting grammar
 * nodes, so GruleCreator is defined to invoking constructor of Grule,
 * internally.
 * 
 * @author pf-miles
 * 
 */
final class GenedGrule extends Grule {

    private static final long serialVersionUID = 7844640022778630030L;

    GenedGrule() {
        super(-1);
    }

    public String toString() {
        return "GenedGrule";
    }

}

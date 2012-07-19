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

import com.github.pfmiles.dropincc.impl.kleene.KleeneCrossNode;
import com.github.pfmiles.dropincc.impl.kleene.KleeneStarNode;
import com.github.pfmiles.dropincc.impl.kleene.OptionalNode;
import com.github.pfmiles.dropincc.impl.util.Util;

/**
 * 
 * This is a utility class that contains a handful of static methods or
 * constants to use.
 * 
 * @author pf-miles
 * 
 */
public abstract class CC {
    private CC() {
    }

    /**
     * represents a 'blank' alternative of a grammar rule
     */
    public static final Element NOTHING = new Element() {
        private static final long serialVersionUID = -897759698260072002L;
    };

    /**
     * end of parsing file(or character stream) token
     */
    public static final TokenDef EOF = new TokenDef("EOF") {
        private static final long serialVersionUID = 9194950534504326943L;

        public String toString() {
            return "EOF";
        }
    };

    /**
     * The kleene star syntactic sugar creator
     * 
     * @param elements
     * @return
     */
    public static KleeneStarNode ks(Object... eles) {
        if (eles == null || eles.length == 0)
            throw new DropinccException("Could not create empty kleene star node.");
        Element[] elements = Util.filterProductionEles(eles);
        return new KleeneStarNode(elements);
    }

    /**
     * The kleene cross node creator
     * 
     * @param elements
     * @return
     */
    public static KleeneCrossNode kc(Object... eles) {
        if (eles == null || eles.length == 0)
            throw new DropinccException("Could not create empty kleene cross node.");
        Element[] elements = Util.filterProductionEles(eles);
        return new KleeneCrossNode(elements);
    }

    /**
     * The optional node creator
     * 
     * @param elements
     * @return
     */
    public static OptionalNode op(Object... eles) {
        if (eles == null || eles.length == 0)
            throw new DropinccException("Could not create empty optional node.");
        Element[] elements = Util.filterProductionEles(eles);
        return new OptionalNode(elements);
    }

}

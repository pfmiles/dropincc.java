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
package com.github.pfmiles.dropincc.impl.kleene;

import java.util.Iterator;

import com.github.pfmiles.dropincc.Element;

/**
 * @author pf-miles
 * 
 */
public class KleeneCrossNode extends AbstractKleeneNode {
    private static final long serialVersionUID = -7584875025338381952L;

    public KleeneCrossNode(Element... elements) {
        super(elements);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        for (Iterator<Element> iter = this.elements.iterator(); iter.hasNext();) {
            Element e = iter.next();
            sb.append(e.toString());
            if (iter.hasNext())
                sb.append(", ");
        }
        sb.append(")+");
        return sb.toString();
    }
}

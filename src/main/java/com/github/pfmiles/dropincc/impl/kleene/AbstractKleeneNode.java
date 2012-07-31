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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.github.pfmiles.dropincc.DropinccException;
import com.github.pfmiles.dropincc.Element;
import com.github.pfmiles.dropincc.impl.AndSubRule;
import com.github.pfmiles.dropincc.impl.OrSubRule;

/**
 * @author pf-miles
 * 
 */
@SuppressWarnings("serial")
public abstract class AbstractKleeneNode implements Element {

    protected List<Element> elements = new ArrayList<Element>();

    protected AbstractKleeneNode(Element... elements) {
        if (elements == null || elements.length == 0)
            throw new DropinccException("Could not create empty kleene closure node.");
        if (!allNotNull(elements))
            throw new DropinccException("Null element contained in kleene node, illegal!");
        Collections.addAll(this.elements, elements);
    }

    private boolean allNotNull(Element[] eles) {
        for (Element ele : eles)
            if (ele == null)
                return false;
        return true;
    }

    public AndSubRule and(Object obj) {
        return new AndSubRule(this, obj);
    }

    public OrSubRule or(Object... objs) {
        return new OrSubRule(this, objs);
    }

    // same hashCode method implementation as Object.class needed
    public int hashCode() {
        return super.hashCode();
    }

    // same equals method implementation as Object.class needed
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    public abstract String toString();

    public List<Element> getElements() {
        return elements;
    }
}

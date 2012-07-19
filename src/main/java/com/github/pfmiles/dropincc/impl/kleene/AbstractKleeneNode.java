package com.github.pfmiles.dropincc.impl.kleene;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.github.pfmiles.dropincc.DropinccException;
import com.github.pfmiles.dropincc.Element;

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

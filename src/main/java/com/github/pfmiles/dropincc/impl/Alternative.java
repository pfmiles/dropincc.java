package com.github.pfmiles.dropincc.impl;

import java.util.ArrayList;
import java.util.List;

import com.github.pfmiles.dropincc.Action;
import com.github.pfmiles.dropincc.Element;

/**
 * A rule alternative, for internal implementation usage only.
 * 
 * @author pf-miles
 * 
 */
public class Alternative {

    private List<Element> elements = new ArrayList<Element>();
    private Action action = null;

    public Alternative(Element[] eles) {
        if (!allNull(eles))
            for (Element e : eles)
                this.elements.add(e);
    }

    private boolean allNull(Element[] eles) {
        for (Element e : eles)
            if (e != null)
                return false;
        return true;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public List<Element> getElements() {
        return elements;
    }

    // same hashCode method as Object.class needed
    public int hashCode() {
        return super.hashCode();
    }

    // same equals method as Object.class needed
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}

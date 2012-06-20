package com.github.pfmiles.dropincc.impl.kleene;

import java.util.Iterator;

import com.github.pfmiles.dropincc.Element;

/**
 * This is not a kleene closure node indeed... But it's quite a convenience to
 * put it together with kleene star and kleene cross since they have similar
 * appearance(lexically) when building grammar rules. 'Optional' node is a
 * syntactic sugar for 'one or nothing' rules.
 * 
 * @author pf-miles
 * 
 */
public class OptionalNode extends AbstractKleeneNode {

    private static final long serialVersionUID = -8548403483632864587L;

    public OptionalNode(Element... elements) {
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
        sb.append(")?");
        return sb.toString();
    }
}

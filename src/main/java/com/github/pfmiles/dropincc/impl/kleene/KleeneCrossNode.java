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

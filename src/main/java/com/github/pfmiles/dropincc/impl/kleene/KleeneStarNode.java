package com.github.pfmiles.dropincc.impl.kleene;

import java.util.Iterator;

import com.github.pfmiles.dropincc.Element;

/**
 * @author pf-miles
 * 
 */
public class KleeneStarNode extends AbstractKleeneNode {
    private static final long serialVersionUID = 5411680444631703856L;

    public KleeneStarNode(Element... elements) {
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
        sb.append(")*");
        return sb.toString();
    }

}

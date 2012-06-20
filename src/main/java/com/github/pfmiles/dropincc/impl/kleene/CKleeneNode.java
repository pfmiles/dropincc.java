package com.github.pfmiles.dropincc.impl.kleene;

import java.io.Serializable;
import java.util.List;

import com.github.pfmiles.dropincc.DropinccException;
import com.github.pfmiles.dropincc.impl.EleType;

/**
 * @author pf-miles
 * 
 */
public class CKleeneNode implements Serializable {
    private static final long serialVersionUID = 2432393334358498343L;
    private List<EleType> contents;

    public CKleeneNode(List<EleType> contents) {
        if (contents == null || contents.isEmpty()) {
            throw new DropinccException("Cannot create empty kleene node.");
        }
        this.contents = contents;
    }

    public List<EleType> getContents() {
        return contents;
    }

    // this class requires the same hashCode method to the Object class
    public int hashCode() {
        return super.hashCode();
    }

    // this class requires the same equals method to the Object class
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    public String toString() {
        return "CKleeneNode(" + contents + ")";
    }

}

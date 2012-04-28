package com.github.pfmiles.dropincc.impl.lookahead.links;

import java.util.HashSet;
import java.util.Set;

import com.github.pfmiles.dropincc.impl.EleType;
import com.github.pfmiles.dropincc.impl.lookahead.LALink;

/**
 * A normal look-ahead link, containing look-aheads of the corresponding
 * alternative itself.
 * 
 * @author pf-miles
 * 
 */
public class NormalLink implements LALink {
    private Set<EleType> lookAheads = new HashSet<EleType>();

    public Set<EleType> getLookAheads() {
        return this.lookAheads;
    }
}

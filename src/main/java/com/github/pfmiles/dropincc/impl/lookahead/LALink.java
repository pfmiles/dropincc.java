package com.github.pfmiles.dropincc.impl.lookahead;

import java.util.Set;

import com.github.pfmiles.dropincc.impl.EleType;

/**
 * 
 * a look-ahead link
 * 
 * @author pf-miles
 * 
 */
public interface LALink {
    /**
     * fetch look-aheads held by this link
     * 
     * @return
     */
    Set<EleType> getLookAheads();
}

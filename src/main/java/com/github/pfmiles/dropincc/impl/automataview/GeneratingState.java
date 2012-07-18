package com.github.pfmiles.dropincc.impl.automataview;

import java.util.List;

import com.github.pfmiles.dropincc.impl.util.Pair;

/**
 * 
 * A state which is processing by DotGenerator, it defines three methods
 * required by DotGenerator.
 * 
 * @author pf-miles
 * 
 */
public interface GeneratingState {

    /**
     * The name label of this state.
     * 
     * @return
     */
    String getId();

    /**
     * Return all transitions of this state, each state is represented as a
     * string(transition edge) and state(transition destination) pair.
     * 
     * @return
     */
    List<Pair<String, GeneratingState>> getTransitions();

    /**
     * Tell if this state is a final state.
     * 
     * @return
     */
    boolean isFinal();
}

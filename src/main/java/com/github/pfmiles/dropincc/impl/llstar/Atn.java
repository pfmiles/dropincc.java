package com.github.pfmiles.dropincc.impl.llstar;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.github.pfmiles.dropincc.impl.GruleType;

/**
 * The whole ATN network for the analyzing grammar
 * 
 * @author pf-miles
 * 
 */
public class Atn {
    // all states contained in this ATN network
    private Set<AtnState> states = new HashSet<AtnState>();
    // grule type to its corresponding start state mapping
    private Map<GruleType, AtnState> gruleTypeStartStateMapping = new HashMap<GruleType, AtnState>();
    // atn state to its belonging grule type mapping
    private Map<AtnState, GruleType> stateGruleTypeMapping = new HashMap<AtnState, GruleType>();

    /**
     * Return all target AtnState of the specified transition edge.
     * 
     * @param edge
     * @return
     */
    public Set<AtnState> getAllDestinationsOf(Object edge) {
        Set<AtnState> ret = new HashSet<AtnState>();
        for (AtnState state : states) {
            if (state.getTransitions().containsKey(edge))
                ret.addAll(state.getTransitions().get(edge));
        }
        return ret;
    }

    /**
     * Return the start state of the specified grule type.
     * 
     * @param gruleType
     * @return
     */
    public AtnState getStartState(GruleType gruleType) {
        return this.gruleTypeStartStateMapping.get(gruleType);
    }

    /**
     * turn this ATN network to a dot file
     * 
     * @return
     */
    public String toDot() {
        // TODO to be implemented
        return toString();
    }

    public String toString() {
        return "Atn(" + this.states + ")";
    }

    /**
     * Return the gruleType the specified atnState belongs to
     * 
     * @param state
     * @return
     */
    public GruleType getGruleTypeByAtnState(AtnState state) {
        return this.stateGruleTypeMapping.get(state);
    }
}

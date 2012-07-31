/*******************************************************************************
 * Copyright (c) 2012 pf_miles.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     pf_miles - initial API and implementation
 ******************************************************************************/
package com.github.pfmiles.dropincc.impl.llstar;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.github.pfmiles.dropincc.DropinccException;
import com.github.pfmiles.dropincc.impl.util.SeqGen;

/**
 * @author pf-miles
 * 
 */
public class LookAheadDfa {
    // all dfa states
    private Set<DfaState> states = new HashSet<DfaState>();
    // final states of this dfa
    private Map<Integer, DfaState> finalStates = new HashMap<Integer, DfaState>();
    // dfa state name sequence gen
    private SeqGen dfaStatesSeq = new SeqGen();
    // the start state
    private DfaState start;

    public DfaState getStart() {
        return start;
    }

    public void setStart(DfaState start) {
        if (this.start != null)
            throw new DropinccException("A look-ahead dfa could have only one start state!");
        this.start = start;
    }

    /**
     * remove all states specified
     * 
     * @param tbds
     */
    public void removeStates(Collection<DfaState> tbds) {
        this.states.removeAll(tbds);
    }

    /**
     * Add a new state to this dfa
     * 
     * @param state
     */
    public void addState(DfaState state) {
        state.setName("d" + this.dfaStatesSeq.next());
        this.states.add(state);
    }

    /**
     * Test if this dfa contains the specified state
     * 
     * @param s
     * @return
     */
    public boolean containState(DfaState s) {
        return this.states.contains(s);
    }

    /**
     * Add a auto-generated final state for allternative index 'alt'
     * 
     * @param alt
     *            the alternative index for which to generate final state
     */
    public void addDummyFinalState(int alt) {
        DfaState state = new DfaState();
        // add a special dummy final atn_config, to satisfy dfa state's equality
        // definition when replacing old final state
        state.addConf(new AtnConfig(new AtnState("_dummy_final_" + alt), alt));
        state.setAlt(alt);
        state.setStopTransit(true);
        this.addState(state);
        this.finalStates.put(alt, state);
    }

    /**
     * Replace the old final state with the specified one
     * 
     * @param alt
     * @param state
     */
    public DfaState overrideFinalState(int alt, DfaState state) {
        DfaState oldFinal = this.finalStates.put(alt, state);
        for (DfaState s : this.states) {
            for (Map.Entry<Object, DfaState> trans : s.getTransitions().entrySet()) {
                if (trans.getValue().equals(oldFinal)) {
                    trans.setValue(state);
                }
            }
        }
        if (!oldFinal.equals(state))
            // if two states are equal, the old one is already removed while
            // previous state adding, need not remove again here
            this.states.remove(oldFinal);
        return oldFinal;
    }

    public String toString() {
        return this.states.toString();
    }

    /**
     * Turn this dfa into dot formatted string, for later image generation.
     * 
     * @return
     */
    public String toDot() {
        // TODO
        return this.toString();
    }

    /**
     * Return the state in this dfa which is logically equals to the specified
     * state(they may NOT be referentially equal)
     * 
     * @param state
     * @return
     */
    public DfaState getSameState(DfaState state) {
        if (!this.states.contains(state))
            return null;
        for (DfaState s : this.states) {
            if (s.equals(state))
                return s;
        }
        throw new DropinccException("Impossible!");
    }

    /**
     * Return the final dfa state corresponding to the specified alt number
     * 
     * @param alt
     * @return
     */
    public DfaState getFinalStateOfAlt(int alt) {
        return this.finalStates.get(alt);
    }

    public Set<DfaState> getStates() {
        return states;
    }
}

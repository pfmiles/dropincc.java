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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.github.pfmiles.dropincc.DropinccException;
import com.github.pfmiles.dropincc.Predicate;
import com.github.pfmiles.dropincc.impl.TokenType;

/**
 * DfaState in look-ahead DFA
 * 
 * @author pf-miles
 * 
 */
public class DfaState {
    // AtnConfigs contained in this state
    private Set<AtnConfig> confs = new HashSet<AtnConfig>();

    // transition mappings to other DfaStates, edges could only be TokenType or
    // Predicate
    private Map<Object, DfaState> transitions = new HashMap<Object, DfaState>();

    // to prevent infinite loop, put atnConfigs which is closuring into this set
    private Set<AtnConfig> busy = new HashSet<AtnConfig>();
    // alts which is recursing, to detect non-LL-regular grammar and stop the
    // analysis algorithm
    private Set<Integer> recursiveAlts = new HashSet<Integer>();
    // predicting alternative index of this state, only final state has alt
    // number which is greater than -1
    private int alt = -1;
    // mark if this state overflowed
    private boolean overflowed;

    // if true, no more closure or move steps need to be performed
    private boolean stopTransit;

    // for printing
    private String name;

    /**
     * Find all resolved ATN configs in this dfa state
     * 
     * @return
     */
    public List<AtnConfig> getResolvedConfs() {
        List<AtnConfig> ret = new ArrayList<AtnConfig>();
        for (AtnConfig c : this.confs) {
            if (c.isResolved())
                ret.add(c);
        }
        return ret;
    }

    /**
     * Remove all transitions with edges specified
     * 
     * @param trans
     */
    public void removeTransitions(Set<TokenType> trans) {
        this.transitions.keySet().removeAll(trans);
    }

    /**
     * Add conf to busy set
     * 
     * @param conf
     */
    public void addToBusy(AtnConfig conf) {
        this.busy.add(conf);
    }

    /**
     * Test if the specified configuration is in the
     * 
     * @param conf
     * @return
     */
    public boolean inBusy(AtnConfig conf) {
        return this.busy.contains(conf);
    }

    /**
     * Add recAlt to the 'recursiveAlts' set
     * 
     * @param recAlt
     */
    public void addRecursiveAlt(int recAlt) {
        this.recursiveAlts.add(recAlt);
    }

    /**
     * clean busy set
     */
    public void releaseBusy() {
        this.busy.clear();
    }

    /**
     * Doing transit by a terminal edge on all of its containing AtnConfigs,
     * returns destination set of AtnConfigs.
     * 
     * @param t
     *            the terminal transition edge
     * @return
     */
    public Set<AtnConfig> move(TokenType t) {
        Set<AtnConfig> ret = new HashSet<AtnConfig>();
        for (AtnConfig c : this.confs) {
            Set<AtnState> dests = c.getState().transit(t);
            if (dests.size() == 0)
                continue;
            for (AtnState d : dests)
                ret.add(new AtnConfig(d, c.getAlt(), c.getStack().clone(), c.getPred()));
        }
        return ret;
    }

    /**
     * Return all terminal transition edges comes out from all the AtnStates
     * contained in this DfaState
     * 
     * @return
     */
    public Set<TokenType> getAllTerminalEdgesOfContainingAtnStates() {
        Set<TokenType> ret = new HashSet<TokenType>();
        for (AtnConfig c : this.confs) {
            for (Object edge : c.getState().getTransitions().keySet()) {
                if (edge instanceof TokenType)
                    ret.add((TokenType) edge);
            }
        }
        return ret;
    }

    /**
     * Return all alts predicted by all AtnConfigs in this state.
     * 
     * @return
     */
    public Set<Integer> getAllPredictingAlts() {
        Set<Integer> ret = new HashSet<Integer>();
        if (this.isFinal()) {
            ret.add(this.alt);
        } else {
            for (AtnConfig c : this.confs)
                ret.add(c.getAlt());
        }
        return ret;
    }

    /**
     * Test if this DfaState is a final state
     * 
     * @return
     */
    public boolean isFinal() {
        return this.alt > -1;
    }

    /**
     * Add all AtnConfigs specified
     * 
     * @param confs
     */
    public void addAllConfs(Collection<AtnConfig> confs) {
        this.confs.addAll(confs);
    }

    /**
     * Remove a AtnConfig from this state
     * 
     * @param conf
     */
    public void removeConf(AtnConfig conf) {
        this.confs.remove(conf);
    }

    /**
     * Return all AtnConfigs which has a Predicate and the same alt number as
     * specified.
     * 
     * @param alt
     * @return
     */
    public Set<AtnConfig> getConfsWithPredsOfAlt(int alt) {
        Set<AtnConfig> ret = new HashSet<AtnConfig>();
        for (AtnConfig c : this.confs) {
            if (c.getPred() != null && c.getAlt() == alt)
                ret.add(c);
        }
        return ret;
    }

    public String toString() {
        return this.confs.toString();
    }

    /**
     * Add AtnConfig to this state
     * 
     * @param conf
     */
    public void addConf(AtnConfig conf) {
        this.confs.add(conf);
    }

    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((confs == null) ? 0 : confs.hashCode());
        return result;
    }

    /**
     * Two DfaStates are equal if their AtnConfig sets are equal.
     */
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        DfaState other = (DfaState) obj;
        if (confs == null) {
            if (other.confs != null)
                return false;
        } else if (!confs.equals(other.confs))
            return false;
        return true;
    }

    /**
     * Add a new transition mapping for this state
     * 
     * @param edge
     * @param target
     */
    public void addTransition(Object edge, DfaState target) {
        if (!(edge instanceof TokenType) && !(edge instanceof Predicate))
            throw new DropinccException("Illegal transition edge for DfaState, only TokenType or Predicates are permitted, edge: " + edge);
        this.transitions.put(edge, target);
    }

    public boolean isOverflowed() {
        return overflowed;
    }

    public void setOverflowed(boolean overflowed) {
        this.overflowed = overflowed;
    }

    public boolean isStopTransit() {
        return stopTransit;
    }

    public void setStopTransit(boolean stopTransit) {
        this.stopTransit = stopTransit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<Object, DfaState> getTransitions() {
        return transitions;
    }

    public Set<AtnConfig> getConfs() {
        return confs;
    }

    public Set<Integer> getRecursiveAlts() {
        return recursiveAlts;
    }

    public int getAlt() {
        return alt;
    }

    public void setAlt(int alt) {
        this.alt = alt;
    }

}

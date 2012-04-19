package com.github.pfmiles.dropincc.impl;

import java.util.ArrayList;
import java.util.List;

import com.github.pfmiles.dropincc.Action;

/**
 * Compiled rule alternative, with matching element sequence and look-aheads,
 * action
 * 
 * @author pf-miles
 * 
 */
public class CAlternative {

    private List<EleType> matchSequence = new ArrayList<EleType>();
    private Action action = null;

    // TODO LL look aheads

    public CAlternative(List<EleType> ms, Action action) {
        this.matchSequence = ms;
        this.action = action;
    }

    public List<EleType> getMatchSequence() {
        return matchSequence;
    }

    public void setMatchSequence(List<EleType> matchSequence) {
        this.matchSequence = matchSequence;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    // same hashCode method as Object.class needed
    public int hashCode() {
        return super.hashCode();
    }

    // same equals method as Object.class needed
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}

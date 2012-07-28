package com.github.pfmiles.dropincc.impl.runtime.impl;

import java.util.Map;

import com.github.pfmiles.dropincc.Predicate;
import com.github.pfmiles.dropincc.impl.TokenType;

/**
 * 
 * Look ahead dfa state used in the runtime parser.
 * 
 * @author pf-miles
 * 
 */
public class RunningDfaState {
    public int state;
    public boolean isFinal;
    public boolean isPredTransitionState;
    public Map<TokenType, RunningDfaState> transitions;
    public Map<Predicate, RunningDfaState> predTrans;
    public int alt = -1;

    public int hashCode() {
        return state;
    }

    public boolean equals(Object obj) {
        return state == ((RunningDfaState) obj).state;
    }
}

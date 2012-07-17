package com.github.pfmiles.dropincc.impl.llstar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.github.pfmiles.dropincc.DropinccException;
import com.github.pfmiles.dropincc.impl.GruleType;
import com.github.pfmiles.dropincc.impl.TokenType;
import com.github.pfmiles.dropincc.impl.util.Pair;

/**
 * State of ATN
 * 
 * @author pf-miles
 * 
 */
public class AtnState {
    private String name;
    // transitions to other states
    private Map<Object, Set<AtnState>> transitions = new HashMap<Object, Set<AtnState>>();
    // if this is a final state
    private boolean _final;

    /**
     * Return all transitions as (edge, state) pairs
     * 
     * @return
     */
    public List<Pair<Object, AtnState>> getTransitionsAsPairs() {
        List<Pair<Object, AtnState>> ret = new ArrayList<Pair<Object, AtnState>>();
        for (Map.Entry<Object, Set<AtnState>> e : this.transitions.entrySet()) {
            Object edge = e.getKey();
            for (AtnState s : e.getValue()) {
                ret.add(new Pair<Object, AtnState>(edge, s));
            }
        }
        return ret;
    }

    /**
     * Create a new AtnState
     * 
     * @param name
     *            name of this state
     * @param _final
     *            if this state final
     */
    public AtnState(String name, boolean _final) {
        super();
        this.name = name;
        this._final = _final;
    }

    public AtnState(String name) {
        this(name, false);
    }

    public void addTransition(Object edge, AtnState otherState) {
        if (!edge.equals(Constants.epsilon) && !(edge instanceof TokenType) && !(edge instanceof GruleType))
            throw new DropinccException("Illegal ATN transition edge: " + edge);
        if (this.transitions.containsKey(edge)) {
            this.transitions.get(edge).add(otherState);
        } else {
            Set<AtnState> set = new HashSet<AtnState>();
            set.add(otherState);
            this.transitions.put(edge, set);
        }
    }

    public Set<AtnState> transit(Object edge) {
        if (this.transitions.containsKey(edge)) {
            return this.transitions.get(edge);
        } else {
            return Collections.emptySet();
        }
    }

    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        AtnState other = (AtnState) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }

    public String toString() {
        return "AtnState(" + this.name + ")";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<Object, Set<AtnState>> getTransitions() {
        return transitions;
    }

    public boolean isFinal() {
        return _final;
    }

    public void setFinal(boolean f) {
        this._final = f;
    }

    /**
     * Return num of transitions
     * 
     * @return
     */
    public int getTransitionCount() {
        int ret = 0;
        for (Set<AtnState> dests : this.transitions.values()) {
            ret += dests.size();
        }
        return ret;
    }

}

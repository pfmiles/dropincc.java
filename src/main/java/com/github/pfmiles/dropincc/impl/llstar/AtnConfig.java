package com.github.pfmiles.dropincc.impl.llstar;

/**
 * AtnConfig needed in LL(*) analyzing, as described in the paper.
 * 
 * @author pf-miles
 * 
 */
public class AtnConfig {
    private AtnState state;
    private int alt;
    private CallStack stack;
    private Predicate pred;

    // if this atnConfig is resolved by semantic predicate
    private boolean wasResolved;

    public AtnConfig(AtnState state, int alt, CallStack stack, Predicate pred) {
        this.state = state;
        this.alt = alt;
        this.stack = stack;
        this.pred = pred;
    }

    public AtnConfig(AtnState state, int alt) {
        this(state, alt, new CallStack(), null);
    }

    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + alt;
        result = prime * result + ((pred == null) ? 0 : pred.hashCode());
        result = prime * result + ((stack == null) ? 0 : stack.hashCode());
        result = prime * result + ((state == null) ? 0 : state.hashCode());
        return result;
    }

    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        AtnConfig other = (AtnConfig) obj;
        if (alt != other.alt)
            return false;
        if (pred == null) {
            if (other.pred != null)
                return false;
        } else if (!pred.equals(other.pred))
            return false;
        if (stack == null) {
            if (other.stack != null)
                return false;
        } else if (!stack.equals(other.stack))
            return false;
        if (state == null) {
            if (other.state != null)
                return false;
        } else if (!state.equals(other.state))
            return false;
        return true;
    }

    public String toString() {
        return "(" + this.state + ", " + this.alt + ", " + this.stack + ", " + this.pred + ")";
    }

    public boolean isWasResolved() {
        return wasResolved;
    }

    public void setWasResolved(boolean wasResolved) {
        this.wasResolved = wasResolved;
    }

    public AtnState getState() {
        return state;
    }

    public void setState(AtnState state) {
        this.state = state;
    }

    public int getAlt() {
        return alt;
    }

    public void setAlt(int alt) {
        this.alt = alt;
    }

    public CallStack getStack() {
        return stack;
    }

    public void setStack(CallStack stack) {
        this.stack = stack;
    }

    public Predicate getPred() {
        return pred;
    }

    public void setPred(Predicate pred) {
        this.pred = pred;
    }

}

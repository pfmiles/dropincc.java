package com.github.pfmiles.dropincc.impl;

import java.util.List;

import com.github.pfmiles.dropincc.impl.llstar.LookAheadDfa;

/**
 * Grammar rule with lookAhead DFA and alternatives.
 * 
 * @author pf-miles
 * 
 */
public class PredictingGrule {

    private GruleType gruleType;
    // the LL(*) look-ahead DFA
    private LookAheadDfa dfa;
    // all alternative productions
    private List<CAlternative> alts;

    public PredictingGrule(GruleType gruleType, LookAheadDfa dfa, List<CAlternative> alts) {
        super();
        this.gruleType = gruleType;
        this.dfa = dfa;
        this.alts = alts;
    }

    public GruleType getGruleType() {
        return gruleType;
    }

    public void setGruleType(GruleType gruleType) {
        this.gruleType = gruleType;
    }

    public LookAheadDfa getDfa() {
        return dfa;
    }

    public void setDfa(LookAheadDfa dfa) {
        this.dfa = dfa;
    }

    public List<CAlternative> getAlts() {
        return alts;
    }

    public void setAlts(List<CAlternative> alts) {
        this.alts = alts;
    }
}

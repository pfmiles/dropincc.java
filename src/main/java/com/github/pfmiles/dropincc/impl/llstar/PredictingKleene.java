package com.github.pfmiles.dropincc.impl.llstar;

import com.github.pfmiles.dropincc.impl.kleene.KleeneType;

/**
 * Kleene node with predicting DFA
 * 
 * @author pf-miles
 * 
 */
public class PredictingKleene {

    private KleeneType kleeneType;
    private LookAheadDfa dfa;
    private boolean backtrack;

    /**
     * Construct a predicting kleene with look-ahead DFA
     * 
     * @param type
     * @param dfa
     */
    public PredictingKleene(KleeneType type, LookAheadDfa dfa) {
        this.kleeneType = type;
        this.dfa = dfa;
    }

    /**
     * Construct a predicting kleene marked backtrack
     * 
     * @param type
     */
    public PredictingKleene(KleeneType type) {
        this.kleeneType = type;
        this.backtrack = true;
    }

    public KleeneType getKleeneType() {
        return kleeneType;
    }

    public LookAheadDfa getDfa() {
        return dfa;
    }

    public boolean isBacktrack() {
        return backtrack;
    }
}

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

import java.util.List;

import com.github.pfmiles.dropincc.impl.CAlternative;
import com.github.pfmiles.dropincc.impl.GruleType;

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

    // Non LL-regular grammar, no valid look-ahead dfa found, fallback to back
    // tracking
    private boolean backtrack;

    // if this rule is on the path of backtracking
    private boolean onBacktrackPath;

    /**
     * Create a predicting grule with look-ahead DFA
     * 
     * @param gruleType
     * @param dfa
     * @param alts
     */
    public PredictingGrule(GruleType gruleType, LookAheadDfa dfa, List<CAlternative> alts, boolean onBacktrackPath) {
        super();
        this.gruleType = gruleType;
        this.dfa = dfa;
        this.alts = alts;
        this.onBacktrackPath = onBacktrackPath;
    }

    /**
     * Create a non-LL regular predicting grule, this kind of rule would do
     * backtracking at runtime
     * 
     * @param grule
     * @param alts
     */
    public PredictingGrule(GruleType grule, List<CAlternative> alts, boolean onBacktrackPath) {
        this.gruleType = grule;
        this.alts = alts;
        this.backtrack = true;
        this.onBacktrackPath = onBacktrackPath;
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

    public boolean isBacktrack() {
        return backtrack;
    }

    public boolean isOnBacktrackPath() {
        return onBacktrackPath;
    }

}

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
    // if this kleene node is on the path of backtracking
    private boolean onBacktrackPath;

    /**
     * Construct a predicting kleene with look-ahead DFA
     * 
     * @param type
     * @param dfa
     */
    public PredictingKleene(KleeneType type, LookAheadDfa dfa, boolean onBacktrackPath) {
        this.kleeneType = type;
        this.dfa = dfa;
        this.onBacktrackPath = onBacktrackPath;
    }

    /**
     * Construct a predicting kleene marked backtrack
     * 
     * @param type
     */
    public PredictingKleene(KleeneType type, boolean onBacktrackPath) {
        this.kleeneType = type;
        this.backtrack = true;
        this.onBacktrackPath = onBacktrackPath;
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

    public boolean isOnBacktrackPath() {
        return onBacktrackPath;
    }

}

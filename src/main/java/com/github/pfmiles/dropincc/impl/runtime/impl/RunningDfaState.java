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
    public Map<Predicate<?>, RunningDfaState> predTrans;
    public int alt = -1;

    public int hashCode() {
        return state;
    }

    public boolean equals(Object obj) {
        return state == ((RunningDfaState) obj).state;
    }
}

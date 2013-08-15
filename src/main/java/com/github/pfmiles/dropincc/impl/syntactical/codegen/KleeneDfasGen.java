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
package com.github.pfmiles.dropincc.impl.syntactical.codegen;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.pfmiles.dropincc.impl.TokenType;
import com.github.pfmiles.dropincc.impl.llstar.DfaState;
import com.github.pfmiles.dropincc.impl.llstar.LookAheadDfa;
import com.github.pfmiles.dropincc.impl.llstar.PredictingKleene;
import com.github.pfmiles.dropincc.impl.runtime.impl.RunningDfaState;
import com.github.pfmiles.dropincc.impl.util.SeqGen;

/**
 * @author pf-miles
 * 
 */
public class KleeneDfasGen extends CodeGen {

    // kleene name {0}
    private static final String fmt = "public RunningDfaState {0}DfaStart;";

    private List<PredictingKleene> pks;

    public KleeneDfasGen(List<PredictingKleene> kleeneTypeToDfa) {
        this.pks = kleeneTypeToDfa;
    }

    @SuppressWarnings("unchecked")
    public String render(CodeGenContext context) {
        StringBuilder sb = new StringBuilder();
        for (PredictingKleene pk : this.pks) {
            // backtrack kleene have no DFA
            if (pk.isBacktrack())
                continue;
            sb.append(MessageFormat.format(fmt, pk.getKleeneType().toCodeGenStr())).append('\n');
            String fieldName = pk.getKleeneType().toCodeGenStr() + "DfaStart";
            context.fieldKleeneDfaMapping.put(fieldName, toPredictingDfa(pk.getDfa()));
        }
        return sb.toString();
    }

    private RunningDfaState toPredictingDfa(LookAheadDfa d) {
        Map<DfaState, Integer> stateNumMapping = new HashMap<DfaState, Integer>();
        SeqGen seq = new SeqGen();
        // start state is always zero
        DfaState s0 = d.getStart();
        stateNumMapping.put(s0, seq.next());
        for (DfaState s : d.getStates()) {
            if (s.equals(s0))
                continue;
            stateNumMapping.put(s, seq.next());
        }
        RunningDfaState start = null;
        Map<RunningDfaState, RunningDfaState> ds = new HashMap<RunningDfaState, RunningDfaState>();
        for (Map.Entry<DfaState, Integer> e : stateNumMapping.entrySet()) {
            DfaState s = e.getKey();
            RunningDfaState rstate = resolveRunningState(e.getValue(), ds);
            if (rstate.state == 0)
                start = rstate;
            if (s.isFinal()) {
                rstate.isFinal = true;
                rstate.alt = s.getAlt();
                continue;
            }
            Map<TokenType, RunningDfaState> trans = new HashMap<TokenType, RunningDfaState>();
            for (Map.Entry<Object, DfaState> t : s.getTransitions().entrySet()) {
                int destNum = stateNumMapping.get(t.getValue());
                RunningDfaState destRstate = resolveRunningState(destNum, ds);
                trans.put((TokenType) t.getKey(), destRstate);
            }
            rstate.transitions = trans;
        }
        return start;
    }

    private RunningDfaState resolveRunningState(Integer state, Map<RunningDfaState, RunningDfaState> ds) {
        RunningDfaState s = new RunningDfaState();
        s.state = state;
        if (ds.containsKey(s)) {
            return ds.get(s);
        } else {
            ds.put(s, s);
            return s;
        }
    }
}

package com.github.pfmiles.dropincc.impl.syntactical.codegen;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.pfmiles.dropincc.Predicate;
import com.github.pfmiles.dropincc.impl.PredictingGrule;
import com.github.pfmiles.dropincc.impl.TokenType;
import com.github.pfmiles.dropincc.impl.llstar.DfaState;
import com.github.pfmiles.dropincc.impl.llstar.LookAheadDfa;
import com.github.pfmiles.dropincc.impl.runtime.impl.RunningDfaState;
import com.github.pfmiles.dropincc.impl.util.SeqGen;

/**
 * @author pf-miles
 * 
 */
public class RuleDfasGen extends CodeGen {

    // ruleName {0}
    private static final MessageFormat fmt = new MessageFormat("public RunningDfaState {0}DfaStart;// {0} rule look ahead dfa start state");

    private List<PredictingGrule> pgs;

    public RuleDfasGen(List<PredictingGrule> pgs) {
        this.pgs = pgs;
    }

    @SuppressWarnings("unchecked")
    public String render(CodeGenContext context) {
        StringBuilder sb = new StringBuilder();
        for (PredictingGrule p : pgs) {
            if (p.getDfa() == null)
                continue;
            String ruleName = p.getGruleType().toCodeGenStr();
            sb.append(fmt.format(new String[] { ruleName })).append('\n');
            String dfaName = ruleName + "DfaStart";
            RunningDfaState start = toPredictingDfa(p.getDfa());
            context.fieldRuleDfaMapping.put(dfaName, start);
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
            RunningDfaState rs = resolveRunningState(e.getValue(), ds);
            if (rs.state == 0)
                start = rs;
            if (s.isFinal()) {
                rs.isFinal = true;
                rs.alt = s.getAlt();
                continue;
            }
            rs.isPredTransitionState = isPredicateTransState(s.getTransitions());
            if (rs.isPredTransitionState) {
                Map<Predicate, RunningDfaState> trans = new HashMap<Predicate, RunningDfaState>();
                for (Map.Entry<Object, DfaState> t : s.getTransitions().entrySet()) {
                    int destNum = stateNumMapping.get(t.getValue());
                    RunningDfaState destRstate = resolveRunningState(destNum, ds);
                    trans.put((Predicate) t.getKey(), destRstate);
                }
                rs.predTrans = trans;
            } else {
                Map<TokenType, RunningDfaState> trans = new HashMap<TokenType, RunningDfaState>();
                for (Map.Entry<Object, DfaState> t : s.getTransitions().entrySet()) {
                    int destNum = stateNumMapping.get(t.getValue());
                    RunningDfaState destRstate = resolveRunningState(destNum, ds);
                    trans.put((TokenType) t.getKey(), destRstate);
                }
                rs.transitions = trans;
            }
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

    private boolean isPredicateTransState(Map<Object, DfaState> transitions) {
        for (Object t : transitions.keySet()) {
            if (!(t instanceof Predicate))
                return false;
        }
        return true;
    }
}

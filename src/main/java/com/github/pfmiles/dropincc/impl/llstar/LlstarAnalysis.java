package com.github.pfmiles.dropincc.impl.llstar;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.github.pfmiles.dropincc.DropinccException;
import com.github.pfmiles.dropincc.impl.GruleType;
import com.github.pfmiles.dropincc.impl.util.Pair;
import com.github.pfmiles.dropincc.impl.util.Util;

/**
 * A 'session' of an analysis progress. Some global context variables needed
 * when analyzing.And defined the major algorithms described in Prof. Parr's
 * LL(*) paper: "LL(*): the foundation of the ANTLR parser generator" published
 * in 2011. And... with some subtle modifications in order to make it run
 * smoothly.
 * 
 * @author pf-miles
 * 
 */
public class LlstarAnalysis {

    // errors or warning messages generated while analyzing
    private StringBuilder warnings = new StringBuilder();

    // the whole ATN network for the analyzing grammar
    private Atn atn;

    /**
     * Trying to resolve conflicts with predicates.
     * 
     * @param state
     *            the state to be resolved
     * @param conflicts
     *            conflicting alternative indexes
     * @return
     */
    public static boolean resolveWithPreds(DfaState state, Set<Integer> conflicts) {
        Map<Integer, Set<AtnConfig>> altToConfs = new HashMap<Integer, Set<AtnConfig>>();
        for (int i : conflicts) {
            Set<AtnConfig> confsWithPreds = state.getConfsWithPredsOfAlt(i);
            if (confsWithPreds != null && !confsWithPreds.isEmpty())
                altToConfs.put(i, confsWithPreds);
        }
        if (altToConfs.size() < conflicts.size())
            return false;
        for (Set<AtnConfig> cs : altToConfs.values()) {
            for (AtnConfig c : cs) {
                c.setWasResolved(true);
            }
        }
        return true;
    }

    /**
     * Try to resolve when a dfa state may overflow
     * 
     * @param state
     */
    public void resolveOverflow(DfaState state) {
        if (!state.isOverflowed())
            return;
        String stateStr = state.toString();
        // should stop compute when overflowed
        state.setStopTransit(true);
        // overflowed, treat all predicting alts as conflicts
        Set<Integer> conflicts = state.getAllPredictingAlts();
        if (resolveWithPreds(state, conflicts))
            // if successfully being resolved by predicates, return
            return;
        // otherwise, resolve by removing alts except the min one
        int minAlt = Util.minInt(conflicts);
        for (Iterator<AtnConfig> iter = state.getConfs().iterator(); iter.hasNext();) {
            AtnConfig c = iter.next();
            if (c.getAlt() != minAlt)
                iter.remove();
        }
        this.warnings.append("State: ").append(stateStr)
                .append(" overflowed, resolved by removing all competing alternatives except the one defined first, remaining alt: " + minAlt).append('\n');
    }

    /**
     * Resolve conflict configurations in the specified dfa state
     * 
     * @param state
     */
    public void resolveConflicts(DfaState state) {
        // build (state, callStack) -> alts mapping
        Map<Pair<AtnState, CallStack>, Set<Integer>> ssToAlt = new HashMap<Pair<AtnState, CallStack>, Set<Integer>>();
        for (AtnConfig conf : state.getConfs()) {
            Pair<AtnState, CallStack> k = new Pair<AtnState, CallStack>(conf.getState(), conf.getStack());
            if (!ssToAlt.containsKey(k))
                ssToAlt.put(k, new HashSet<Integer>());
            ssToAlt.get(k).add(conf.getAlt());
        }
        // retain the ones conflicts into 'conflictsKvs'
        Map<Pair<AtnState, CallStack>, Set<Integer>> conflictsKvs = new HashMap<Pair<AtnState, CallStack>, Set<Integer>>();
        for (Map.Entry<Pair<AtnState, CallStack>, Set<Integer>> entry : ssToAlt.entrySet()) {
            Pair<AtnState, CallStack> k = entry.getKey();
            Set<Integer> v = entry.getValue();
            if (v.size() > 1)
                conflictsKvs.put(k, v);
        }
        // if no conflicts detected, return
        if (conflictsKvs.size() == 0)
            return;

        // if not all the conflict sets contain all predicting alts, this
        // indicates that more transitions should be performed, it's not the
        // time to resolve by removing competing alts , return
        Set<Integer> allPredictingAlts = state.getAllPredictingAlts();
        for (Set<Integer> confAlts : conflictsKvs.values()) {
            if (!confAlts.equals(allPredictingAlts)) {
                return;
            }
        }
        state.setStopTransit(true);
        // if conflicts could be resolved by preds, return
        if (resolveWithPreds(state, allPredictingAlts))
            return;
        // otherwise, resolve by removing all competing alts except the one
        // defined first
        String stateStr = state.toString();
        int minAlt = Util.minInt(allPredictingAlts);
        for (Iterator<AtnConfig> iter = state.getConfs().iterator(); iter.hasNext();) {
            AtnConfig c = iter.next();
            if (c.getAlt() != minAlt)
                iter.remove();
        }
        this.warnings.append("State: ").append(stateStr).append(" has conflict predicting alternatives: ").append(allPredictingAlts)
                .append(", resolved by selecting the first alt.").append('\n');
    }

    /**
     * Closure operation described in the paper.
     * 
     * @param state
     *            the source dfa state of the previous transition
     * @param conf
     *            the destination atn config of the previous transition
     * @return set containing the specified conf and all its epsilon/subrule
     *         closure confs
     */
    public Set<AtnConfig> closure(DfaState state, AtnConfig conf) {
        if (state.inBusy(conf)) {
            return Collections.emptySet();
        } else {
            state.addToBusy(conf);
        }
        Set<AtnConfig> ret = new HashSet<AtnConfig>();
        ret.add(conf);

        AtnState p = conf.getState();
        int i = conf.getAlt();
        CallStack y = conf.getStack();
        Predicate pi = conf.getPred();

        if (p.isFinal()) {
            if (y.isEmpty()) {
                for (AtnState p2 : this.atn.getAllDestinationsOf(this.atn.getGruleTypeByAtnState(p)))
                    ret.addAll(closure(state, new AtnConfig(p2, i, new CallStack(), pi)));
            } else {
                Pair<AtnState, CallStack> ss = y.copyAndPop();
                ret.addAll(closure(state, new AtnConfig(ss.getLeft(), i, ss.getRight(), pi)));
            }
        }
        for (Pair<Object, AtnState> transPairs : p.getTransitionsAsPairs()) {
            Object edge = transPairs.getLeft();
            AtnState s = transPairs.getRight();
            /*
             * closure only cares about non-terminal(GruleType) and epsilon
             * transitions
             */
            if (edge instanceof GruleType) {
                // is a non-terminal edge transition
                int depth = y.computeRecurseDepth(s);
                if (depth == 1) {
                    state.addRecursiveAlt(i);
                    if (state.getRecursiveAlts().size() > 1) {
                        throw new DropinccException("Likely non-LL regular grammar, recursive alts: " + state.getRecursiveAlts() + ", rule: "
                                + this.atn.getGruleTypeByAtnState(p));
                    }
                }
                if (depth >= Constants.MAX_REC_DEPTH) {
                    state.setOverflowed(true);
                    return ret;
                }
                // push destination state onto the copied stack and doing
                // closure with the starting state of edge(a grule type)
                CallStack stk = y.clone();
                stk.push(s);
                ret.addAll(closure(state, new AtnConfig(this.atn.getStartState((GruleType) edge), i, stk, pi)));
            } else if (edge instanceof Predicate || edge.equals(Constants.epsilon)) {
                // is predicate or epsilon transition
                ret.addAll(closure(state, new AtnConfig(s, i, y.clone(), pi)));
            }
        }
        return ret;
    }
}

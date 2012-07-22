package com.github.pfmiles.dropincc.impl.llstar;

import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.github.pfmiles.dropincc.DropinccException;
import com.github.pfmiles.dropincc.Predicate;
import com.github.pfmiles.dropincc.impl.CAlternative;
import com.github.pfmiles.dropincc.impl.EleType;
import com.github.pfmiles.dropincc.impl.GruleType;
import com.github.pfmiles.dropincc.impl.TokenType;
import com.github.pfmiles.dropincc.impl.kleene.CKleeneNode;
import com.github.pfmiles.dropincc.impl.kleene.KleeneCrossType;
import com.github.pfmiles.dropincc.impl.kleene.KleeneStarType;
import com.github.pfmiles.dropincc.impl.kleene.KleeneType;
import com.github.pfmiles.dropincc.impl.kleene.OptionalType;
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

    // warning messages generated while analyzing
    private StringBuilder warnings = new StringBuilder();

    // the whole ATN network for the analyzing grammar
    private Atn atn;

    // the resulting grule to look-ahead dfa mapping
    private Map<GruleType, LookAheadDfa> gruleDfaMapping = new HashMap<GruleType, LookAheadDfa>();

    /**
     * Do analysis
     * 
     * @param ruleTypeToAlts
     * @param kleeneTypeToNode
     */
    public LlstarAnalysis(Map<GruleType, List<CAlternative>> ruleTypeToAlts, Map<KleeneType, CKleeneNode> kleeneTypeToNode) {
        this.atn = new Atn();
        // process rules one by one to create a whole ATN network
        for (Map.Entry<GruleType, List<CAlternative>> e : ruleTypeToAlts.entrySet()) {
            GruleType grule = e.getKey();
            List<CAlternative> calts = e.getValue();
            AtnState pa = this.atn.newStartStateForGrule(grule);
            AtnState pa1 = this.atn.newEndStateForGrule(grule);
            for (int i = 0; i < calts.size(); i++) {
                CAlternative calt = calts.get(i);
                List<EleType> alt = calt.getMatchSequence();
                AtnState pai = this.atn.newAltStateForGrule(grule, i);
                pa.addTransition(Constants.epsilon, pai);
                if (alt != null && !alt.isEmpty()) {
                    AtnState p0 = this.atn.newAtnState(grule);
                    Predicate pred = calt.getPredicate();
                    if (pred != null) {
                        pai.addTransition(pred, p0);
                    } else {
                        pai.addTransition(Constants.epsilon, p0);
                    }
                    // AtnState last = this.atn.genTransitions(p0, alt, grule,
                    // kleeneTypeToNode);
                    AtnState curState = p0;
                    for (EleType edge : alt) {
                        if (edge instanceof TokenType || edge instanceof GruleType) {
                            AtnState nextState = this.atn.newAtnState(grule);
                            curState.addTransition(edge, nextState);
                            curState = nextState;
                        } else if (edge instanceof KleeneStarType) {
                            this.atn.genTransitions(curState, kleeneTypeToNode.get((KleeneStarType) edge).getContents(), curState, grule, kleeneTypeToNode);
                        } else if (edge instanceof KleeneCrossType) {
                            List<EleType> contents = kleeneTypeToNode.get((KleeneCrossType) edge).getContents();
                            AtnState nextState = this.atn.newAtnState(grule);
                            this.atn.genTransitions(curState, contents, nextState, grule, kleeneTypeToNode);
                            curState = nextState;
                            this.atn.genTransitions(curState, contents, curState, grule, kleeneTypeToNode);
                        } else if (edge instanceof OptionalType) {
                            List<EleType> contents = kleeneTypeToNode.get((OptionalType) edge).getContents();
                            AtnState nextState = this.atn.newAtnState(grule);
                            this.atn.genTransitions(curState, contents, nextState, grule, kleeneTypeToNode);
                            curState.addTransition(Constants.epsilon, nextState);
                            curState = nextState;
                        } else {
                            throw new DropinccException("Illegal transition edge of ATN: " + edge);
                        }
                    }
                    curState.addTransition(Constants.epsilon, pa1);
                } else {
                    pai.addTransition(Constants.epsilon, pa1);
                }
            }
        }
        // create look-ahead DFAs
        for (Map.Entry<GruleType, List<CAlternative>> e : ruleTypeToAlts.entrySet()) {
            if (e.getValue().size() > 1) {
                // there's no need to compute look-ahead dfa for a rule which
                // has only one single alternative production
                GruleType grule = e.getKey();
                LookAheadDfa dfa = this.createAfa(this.atn.getStartState(grule), grule);
                this.gruleDfaMapping.put(grule, dfa);
            }
        }
    }

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
                c.setResolved(true);
            }
        }
        return true;
    }

    /**
     * Try to resolve when a dfa state may overflow
     * 
     * @param state
     */
    public void resolveOverflow(DfaState state, GruleType grule) {
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
                .append(" overflowed, resolved by removing all competing alternatives except the one defined first, remaining alt: ").append(minAlt).append(". Grule: ")
                .append(grule).append('\n');
    }

    /**
     * Resolve conflict configurations in the specified dfa state
     * 
     * @param state
     */
    public void resolveConflicts(DfaState state, GruleType grule) {
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
                .append(", resolved by selecting the first alt: ").append(minAlt).append(". Grule: ").append(grule).append('\n');
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

    public LookAheadDfa createAfa(AtnState atnStartState, GruleType gruleType) {
        LookAheadDfa ret = new LookAheadDfa();
        Deque<DfaState> work = new ArrayDeque<DfaState>();
        DfaState D0 = new DfaState();
        for (int alt = 0; alt < atnStartState.getTransitionCount(); alt++) {
            ret.addDummyFinalState(alt);
        }
        List<Pair<Object, AtnState>> transitions = atnStartState.getTransitionsAsPairs();
        for (int i = 0; i < transitions.size(); i++) {
            AtnState pa_i = transitions.get(i).getRight();
            Predicate pi = null;
            // pa_i could have only one transition
            Object first_t = pa_i.getTransitions().entrySet().iterator().next().getKey();
            if (first_t instanceof Predicate)
                pi = (Predicate) first_t;
            D0.addAllConfs(closure(D0, new AtnConfig(pa_i, extractAltFromAltState(pa_i), new CallStack(), pi)));
            D0.releaseBusy();
        }
        work.push(D0);
        ret.addState(D0);
        while (!work.isEmpty()) {
            DfaState state = work.pop();
            // memorize transitions newly added
            Set<TokenType> newTrans = new HashSet<TokenType>();
            // count newly pushed works
            int newWorkCount = 0;
            // memorize newly added states
            Set<DfaState> newStates = new HashSet<DfaState>();
            // old final states backup
            Map<Integer, DfaState> finalsBack = new HashMap<Integer, DfaState>();
            moving: for (TokenType a : state.getAllTerminalEdgesOfContainingAtnStates()) {
                // move & closure
                DfaState newState = new DfaState();
                for (AtnConfig conf : state.move(a)) {
                    newState.addAllConfs(closure(state, conf));
                    state.releaseBusy();
                    // state may be marked overflowed while closuring, so it
                    // must be resolved
                    resolveOverflow(state, gruleType);
                    checkIfFinalAndReplace(state, ret, finalsBack, false);
                    if (state.isStopTransit())
                        break moving;
                }
                if (!ret.containState(newState)) {
                    // resolve conflicts and add to dfa network
                    resolveConflicts(newState, gruleType);
                    if (!checkIfFinalAndReplace(newState, ret, finalsBack, true)) {
                        work.push(newState);
                        newWorkCount++;
                    }
                    ret.addState(newState);
                    newStates.add(newState);
                    state.addTransition(a, newState);
                    newTrans.add(a);
                } else {
                    // add the same state already in the dfa(not the newly
                    // created one)
                    state.addTransition(a, ret.getSameState(newState));
                    newTrans.add(a);
                }
            }
            if (state.isStopTransit()) {
                // rollback newly added states, transitions, final states and
                // works if stoppedTransit
                ret.removeStates(newStates);
                state.removeTransitions(newTrans);
                for (Map.Entry<Integer, DfaState> e : finalsBack.entrySet()) {
                    DfaState s = e.getValue();
                    ret.addState(s);
                    ret.overrideFinalState(e.getKey(), s);
                }
                for (int i = 0; i < newWorkCount; i++) {
                    work.pop();
                }
            }
            // add predicate transitions, use hashset here to de-duplicate
            // transitions
            Set<Pair<Predicate, DfaState>> predTrans = new HashSet<Pair<Predicate, DfaState>>();
            for (AtnConfig conf : state.getResolvedConfs()) {
                predTrans.add(new Pair<Predicate, DfaState>(conf.getPred(), ret.getFinalStateOfAlt(conf.getAlt())));
            }
            for (Pair<Predicate, DfaState> p : predTrans) {
                state.addTransition(p.getLeft(), p.getRight());
            }
        }
        return ret;
    }

    /**
     * Check if the specified dfa state is a final state, and if it is, replace
     * the old final state of the same alternative number with the new one
     * 
     */
    private boolean checkIfFinalAndReplace(DfaState state, LookAheadDfa dnet, Map<Integer, DfaState> finalsBack, boolean backup) {
        Set<Integer> predictingAlts = state.getAllPredictingAlts();
        if (predictingAlts.size() == 1) {
            // is final
            int alt = predictingAlts.iterator().next();
            state.setAlt(alt);
            DfaState oldFinal = dnet.overrideFinalState(alt, state);
            if (backup && !finalsBack.containsKey(alt))
                finalsBack.put(alt, oldFinal);
            state.setStopTransit(true);
            return true;
        }
        return false;
    }

    /**
     * Resolve predicting alt number from 'alt state', according to alt state
     * naming convention
     * 
     * @param pai
     *            alt state, has naming convention 'pRx_x', the second 'x' is
     *            the predicting alt number...
     * 
     * @return
     */
    private static int extractAltFromAltState(AtnState pai) {
        String name = pai.getName();
        return Integer.parseInt(name.substring(name.indexOf('_') + 1));
    }

    public String getWarnings() {
        return warnings.toString();
    }

    public Atn getAtn() {
        return atn;
    }

    /**
     * Get the generated look-ahead dfa for the specified gruleType
     * 
     * @param grule
     * @return
     */
    public LookAheadDfa getLookAheadDfa(GruleType grule) {
        return this.gruleDfaMapping.get(grule);
    }
}

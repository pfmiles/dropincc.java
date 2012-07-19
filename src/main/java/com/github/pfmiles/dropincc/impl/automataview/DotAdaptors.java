package com.github.pfmiles.dropincc.impl.automataview;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.github.pfmiles.dropincc.impl.llstar.AtnState;
import com.github.pfmiles.dropincc.impl.llstar.DfaState;
import com.github.pfmiles.dropincc.impl.util.Pair;
import com.github.pfmiles.dropincc.impl.util.SeqGen;

/**
 * Some adapting methods to convert ATN or DFA states to GeneratingStates which
 * is required by DotGenerator.
 * 
 * @author pf-miles
 * 
 */
public final class DotAdaptors {
    private DotAdaptors() {
    }

    /**
     * Adapt ATN states to GeneratingStates, for later dot generation use.
     * 
     * @param states
     * @return
     */
    public static Collection<GeneratingState> adaptAtnStates(Set<AtnState> states) {
        List<GeneratingState> ret = new ArrayList<GeneratingState>();
        for (AtnState state : states) {
            ret.add(new DotAtnState(state));
        }
        return ret;
    }

    private static final class DotAtnState implements GeneratingState {
        private AtnState state;

        public DotAtnState(AtnState state) {
            this.state = state;
        }

        public String getId() {
            return this.state.getName();
        }

        public List<Pair<String, GeneratingState>> getTransitions() {
            List<Pair<String, GeneratingState>> ret = new ArrayList<Pair<String, GeneratingState>>();
            for (Pair<Object, AtnState> p : this.state.getTransitionsAsPairs()) {
                ret.add(new Pair<String, GeneratingState>(p.getLeft().toString(), new DotAtnState(p.getRight())));
            }
            return ret;
        }

        public boolean isFinal() {
            return this.state.isFinal();
        }

    }

    /**
     * Adapting look ahead dfa states to GeneratingStates
     * 
     * @param states
     * @return
     */
    public static Collection<GeneratingState> adaptLookAheadDfaStates(Set<DfaState> states) {
        List<GeneratingState> ret = new ArrayList<GeneratingState>();
        for (DfaState state : states) {
            ret.add(new DotDfaState(state));
        }
        return ret;
    }

    private static final class DotDfaState implements GeneratingState {
        private DfaState state;
        private int seqNum = -1;
        private SeqGen seq = new SeqGen();

        public DotDfaState(DfaState state) {
            this.state = state;
        }

        public String getId() {
            if (this.state.isFinal()) {
                return "d" + resolveSeqNum() + "_" + this.state.getAlt();
            } else {
                return "d" + resolveSeqNum();
            }
        }

        private int resolveSeqNum() {
            if (this.seqNum == -1)
                this.seqNum = this.seq.next();
            return this.seqNum;
        }

        public List<Pair<String, GeneratingState>> getTransitions() {
            List<Pair<String, GeneratingState>> ret = new ArrayList<Pair<String, GeneratingState>>();
            for (Map.Entry<Object, DfaState> e : this.state.getTransitions().entrySet()) {
                ret.add(new Pair<String, GeneratingState>(e.getKey().toString(), new DotDfaState(e.getValue())));
            }
            return ret;
        }

        public boolean isFinal() {
            return this.state.isFinal();
        }
    }
}

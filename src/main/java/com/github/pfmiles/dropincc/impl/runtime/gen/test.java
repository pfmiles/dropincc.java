package com.github.pfmiles.dropincc.impl.runtime.gen;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.github.pfmiles.dropincc.DropinccException;
import com.github.pfmiles.dropincc.Predicate;
import com.github.pfmiles.dropincc.impl.TokenType;
import com.github.pfmiles.dropincc.impl.runtime.Parser;
import com.github.pfmiles.dropincc.impl.runtime.impl.RunningDfaState;

/**
 * Auto generated parser class by dropincc.java
 */
@SuppressWarnings("unchecked")
public class test extends Parser {
    // token types start
    public TokenType tokenType7;// j
    public TokenType tokenType6;// i
    public TokenType tokenType9;// l
    public TokenType tokenType8;// k
    public TokenType tokenType10;// m
    public TokenType tokenType1;// b
    public TokenType tokenType0;// a
    public TokenType tokenType12;// d
    public TokenType tokenType2;// c
    public TokenType tokenType4;// f
    public TokenType tokenType3;// e
    public TokenType tokenType11;// h
    public TokenType tokenType5;// g
    public TokenType specialTokenType2;// WHITESPACE
    public TokenType specialTokenType1;// EOF

    // token types end

    // alternative's actions start

    // alternative's actions end

    // semantic predicates start

    // semantic predicates end

    // rule predicting dfa start
    public RunningDfaState r3DfaStart;// r3 rule look ahead dfa start state
    public RunningDfaState r4DfaStart;// r4 rule look ahead dfa start state
    public RunningDfaState r2DfaStart;// r2 rule look ahead dfa start state
    public RunningDfaState r0DfaStart;// r0 rule look ahead dfa start state

    // rule predicting dfa end

    // kleene predicting dfa start

    // kleene predicting dfa end

    // parse method
    public <T> T parse(Object arg) {
        return (T) r0(arg);
    }

    // rule methods start
    public Object r3(Object arg) {
        switch (rulePredict(r3DfaStart, arg, "r3")) {
        case 0:
            Object p0 = this.match(tokenType3);
            Object p1 = this.match(tokenType4);
            Object p2 = this.match(tokenType5);
            Object p3 = r4(arg);
            Object[] p4 = new Object[] { p0, p1, p2, p3 };

            return p4;
        case 1:
            Object p5 = this.match(tokenType3);
            Object p6 = this.match(tokenType4);
            Object p7 = this.match(tokenType5);
            Object p8 = this.match(tokenType11);
            Object[] p9 = new Object[] { p5, p6, p7, p8 };

            return p9;

        default:
            throw new DropinccException("No viable alternative found in rule: r3, at position: " + lexer.getCurrentPosition() + ", upcoming sequence: "
                    + lexer.getAheadTokensRepr() + "...");
        }
    }

    public Object r4(Object arg) {
        switch (rulePredict(r4DfaStart, arg, "r4")) {
        case 0:
            Object p0 = this.match(tokenType6);
            Object p1 = this.match(tokenType7);
            Object p2 = this.match(tokenType8);
            Object p3 = this.match(tokenType9);
            Object[] p4 = new Object[] { p0, p1, p2, p3 };

            return p4;
        case 1:
            Object p5 = this.match(tokenType6);
            Object p6 = this.match(tokenType7);
            Object p7 = this.match(tokenType8);
            Object p8 = this.match(tokenType10);
            Object[] p9 = new Object[] { p5, p6, p7, p8 };

            return p9;

        default:
            throw new DropinccException("No viable alternative found in rule: r4, at position: " + lexer.getCurrentPosition() + ", upcoming sequence: "
                    + lexer.getAheadTokensRepr() + "...");
        }
    }

    public Object r1(Object arg) {
        Object p0 = r0(arg);
        Object p1 = this.match(specialTokenType1);
        Object[] p2 = new Object[] { p0, p1 };
        return p2;
    }

    public Object r2(Object arg) {
        switch (rulePredict(r2DfaStart, arg, "r2")) {
        case 0:
            Object p0 = this.match(tokenType0);
            Object p1 = this.match(tokenType1);
            Object p2 = this.match(tokenType2);
            Object p3 = r3(arg);
            Object[] p4 = new Object[] { p0, p1, p2, p3 };

            return p4;
        case 1:
            Object p5 = this.match(tokenType0);
            Object p6 = this.match(tokenType1);
            Object p7 = this.match(tokenType2);
            Object p8 = r4(arg);
            Object[] p9 = new Object[] { p5, p6, p7, p8 };

            return p9;
        case 2:
            Object p10 = this.match(tokenType12);

            return p10;

        default:
            throw new DropinccException("No viable alternative found in rule: r2, at position: " + lexer.getCurrentPosition() + ", upcoming sequence: "
                    + lexer.getAheadTokensRepr() + "...");
        }
    }

    public Object r0(Object arg) {
        switch (rulePredict(r0DfaStart, arg, "r0")) {
        case 0:
            Object p0 = r2(arg);
            Object p1 = new ArrayList<Object>();
            while (kleenePredicting(kleeneStar0PredSet)) {
                Object p2 = this.match(tokenType0);

                ((List<Object>) p1).add(p2);
            }
            p1 = ((List<Object>) p1).toArray();
            Object[] p3 = new Object[] { p0, p1 };

            return p3;
        case 1:
            Object p4 = r3(arg);
            Object p5 = new ArrayList<Object>();
            do {
                Object p6 = this.match(tokenType0);

                ((List<Object>) p5).add(p6);
            } while (kleenePredicting(kleeneCross1PredSet));
            p5 = ((List<Object>) p5).toArray();
            Object[] p7 = new Object[] { p4, p5 };

            return p7;
        case 2:
            Object p8 = r4(arg);
            Object p9 = null;
            if (kleenePredicting(optional2PredSet)) {
                Object p10 = this.match(tokenType0);

                p9 = p10;
            }
            Object[] p11 = new Object[] { p8, p9 };

            return p11;

        default:
            throw new DropinccException("No viable alternative found in rule: r0, at position: " + lexer.getCurrentPosition() + ", upcoming sequence: "
                    + lexer.getAheadTokensRepr() + "...");
        }
    }

    // rule methods end

    // rule alts predicting method
    private int rulePredict(RunningDfaState start, Object arg, String ruleName) {
        RunningDfaState s = start;
        int ahead = 1;
        while (true) {
            if (s.isFinal) {
                return s.alt;
            } else if (s.isPredTransitionState) {
                for (Map.Entry<Predicate, RunningDfaState> pt : s.predTrans.entrySet()) {
                    Predicate pred = pt.getKey();
                    if (pred.pred(arg, lexer)) {
                        return pt.getValue().alt;
                    }
                }
                throw new DropinccException("No viable alternative found in rule: " + ruleName + ", at position: " + lexer.getCurrentPosition() + ", upcoming sequence: "
                        + lexer.getAheadTokensRepr() + "... Tried all predicates, but cannot determine any viable alternative production.");
            } else {
                TokenType t = lexer.LA(ahead);
                if (s.transitions.containsKey(t)) {
                    s = s.transitions.get(t);
                } else {
                    throw new DropinccException("No viable alternative found in rule: " + ruleName + ", at position: " + lexer.getCurrentPosition()
                            + ", upcoming sequence: " + lexer.getAheadTokensRepr() + "...");
                }
                ahead++;
            }
        }
    }

    // kleene predicting method
    private boolean kleenePredicting(RunningDfaState start, String ruleName) {
        RunningDfaState s = start;
        int ahead = 1;
        while (true) {
            if (s.isFinal) {
                return s.alt == 0;
            } else {
                TokenType t = lexer.LA(ahead);
                if (s.transitions.containsKey(t)) {
                    s = s.transitions.get(t);
                } else {
                    throw new DropinccException("Cannot continue parsing in kleene node matching, can't determine whether to go on matching this kleene node. Rule: "
                            + ruleName + ", at position: " + lexer.getCurrentPosition() + ", upcoming sequence: " + lexer.getAheadTokensRepr() + "...");
                }
            }
            ahead++;
        }
    }

}

package com.github.pfmiles.dropincc.impl.llstar;

import com.github.pfmiles.dropincc.CC;
import com.github.pfmiles.dropincc.Grule;
import com.github.pfmiles.dropincc.Lang;
import com.github.pfmiles.dropincc.impl.GruleType;
import com.github.pfmiles.dropincc.impl.automataview.DotAdaptors;
import com.github.pfmiles.dropincc.impl.automataview.DotGenerator;
import com.github.pfmiles.dropincc.impl.util.TestUtil;
import com.github.pfmiles.dropincc.testhelper.AnalyzedLangForTest;
import com.github.pfmiles.dropincc.testhelper.TestHelper;

/**
 * @author pf-miles
 * 
 */
public class LlstarAnalysisExperiments {
    // experiments bench, generate images to see ATN or DFAs
    public static void main(String... args) throws Throwable {
        /*
         * # S ::= A $
# A ::= B a*
#     | C a+
#     | D a?
# B ::= a b c C
#     | a b c D
#     | d
# C ::= e f g D
#     | e f g h
# D ::= i j k l
#     | i j k m
         */
        Lang ll3 = new Lang();
        Grule A = ll3.newGrule();
        
        ll3.defineGrule(A, CC.EOF);
        
        Grule B = ll3.newGrule();
        Grule C = ll3.newGrule();
        Grule D = ll3.newGrule();
        
        A.define(B, CC.ks("a"))
            .alt(C, CC.kc("a"))
            .alt(D, CC.op("a"));
        
        B.define("a", "b", "c", C)
            .alt("a", "b", "c", D)
            .alt("d");
        
        C.define("e", "f", "g", D)
            .alt("e", "f", "g", "h");
        
        D.define("i", "j", "k", "l")
            .alt("i", "j", "k", "m");
        
        genImages(ll3);
    }

    private static void genImages(Lang lang) throws Throwable {
        AnalyzedLangForTest al = TestHelper.resolveAnalyzedLangForTest(lang);
        LlstarAnalysis llstar = new LlstarAnalysis(al.ruleTypeToAlts, al.kleeneTypeToNode);
        System.out.println(llstar.getWarnings());
        DotGenerator dotGen = new DotGenerator(DotAdaptors.adaptAtnStates(llstar.getAtn().getStates()));
        TestUtil.createPng(dotGen, "atn");

        for (GruleType g : al.ruleTypeToAlts.keySet()) {
            LookAheadDfa dfa = llstar.getLookAheadDfa(g);
            if (dfa != null) {
                DotGenerator dfaDot = new DotGenerator(DotAdaptors.adaptLookAheadDfaStates(dfa.getStates()));
                TestUtil.createPng(dfaDot, "R" + g.getDefIndex() + "_dfa");
            }
        }
    }

}

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
         * # conflicts mixed with overflow, resolved by predicates
# S ::= A $
# A ::= B a
#     | b* c
#     | b* c
# B ::= b+ B
         */
        Lang lang = new Lang();
        Grule A = lang.newGrule();
        lang.defineGrule(A, CC.EOF);
        Grule B = lang.newGrule();
        A.define(B, "a")
        .alt(CC.ks("b"), "c")
        .alt(CC.ks("b"), "c");
        B.define(CC.kc("b"), B);

        genImages(lang);
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

package com.github.pfmiles.dropincc.impl.llstar;

import com.github.pfmiles.dropincc.CC;
import com.github.pfmiles.dropincc.Grule;
import com.github.pfmiles.dropincc.Lang;
import com.github.pfmiles.dropincc.TokenDef;
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
         * S ::= A $
         * A ::= ((a b)|((c|d) g)|(e f)) h
         *     | B g
         * B ::= ((a b)|((c|d) g)|(e f)) i
         *     | h
         */
        Lang lang = new Lang();
        Grule A = lang.newGrule();
        lang.defineGrule(A, CC.EOF);
        TokenDef a = lang.newToken("a");
        TokenDef c = lang.newToken("c");
        TokenDef e = lang.newToken("e");
        Grule B = lang.newGrule();
        A.define(a.and("b").or(c.or("d"), "g").or(e.and("f")), "h")
        .alt(B, "g");
        B.define(a.and("b").or(c.or("d"), "g").or(e.and("f")), "i")
        .alt("h");
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

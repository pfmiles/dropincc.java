package com.github.pfmiles.dropincc.impl.llstar;

import com.github.pfmiles.dropincc.CC;
import com.github.pfmiles.dropincc.Grule;
import com.github.pfmiles.dropincc.Lang;
import com.github.pfmiles.dropincc.TokenDef;
import com.github.pfmiles.dropincc.impl.GruleType;
import com.github.pfmiles.dropincc.impl.automataview.DotAdaptors;
import com.github.pfmiles.dropincc.impl.automataview.DotGenerator;
import com.github.pfmiles.dropincc.impl.kleene.KleeneType;
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
         * S ::= L $
         * L ::= (A (+|-))* A
         * A ::= (F (*|/))* F
         * F ::= '(' L ')'
         *     | '[0-9]'+
         */
        Lang lang = new Lang("Calculator");
        Grule L = lang.newGrule();
        TokenDef a = lang.newToken("\\+");
        lang.defineGrule(L, CC.EOF);
        Grule A = lang.newGrule();
        L.define(CC.ks(A, a.or("\\-")), A);
        TokenDef m = lang.newToken("\\*");
        Grule F = lang.newGrule();
        A.define(CC.ks(F, m.or("/")), F);
        F.define("\\(", L, "\\)")
        .alt("[0-9]+");
        
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
        for (KleeneType k : al.kleeneTypeToNode.keySet()) {
            LookAheadDfa dfa = llstar.getKleenDfaMapping().get(k);
            if (dfa != null) {
                DotGenerator dfaDot = new DotGenerator(DotAdaptors.adaptLookAheadDfaStates(dfa.getStates()));
                TestUtil.createPng(dfaDot, "KR" + k.getDefIndex() + "_dfa");
            }
        }
    }

}

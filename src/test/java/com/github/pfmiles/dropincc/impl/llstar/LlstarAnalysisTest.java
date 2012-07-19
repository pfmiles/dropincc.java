package com.github.pfmiles.dropincc.impl.llstar;

import junit.framework.TestCase;

import com.github.pfmiles.dropincc.CC;
import com.github.pfmiles.dropincc.Element;
import com.github.pfmiles.dropincc.Grule;
import com.github.pfmiles.dropincc.Lang;
import com.github.pfmiles.dropincc.Tokens;
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
public class LlstarAnalysisTest extends TestCase {
    // experiments bench, generate images to see ATN or DFAs
    public static void main(String... args) throws Throwable {
        Lang ll1 = new Lang();
        Element a = ll1.addToken("a");
        Element b = ll1.addToken("b");
        Element c = ll1.addToken("c");
        Grule A = ll1.newGrule();
        ll1.addGrammarRule(A, Tokens.EOF);
        A.fillGrammarRule(a, CC.ks(c)).alt(b, CC.ks(c));
        genImages(ll1);
    }

    private static void genImages(Lang lang) throws Throwable {
        AnalyzedLangForTest al = TestHelper.resolveAnalyzedLangForTest(lang);
        LlstarAnalysis llstar = new LlstarAnalysis(al.ruleTypeToAlts, al.kleeneTypeToNode);
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

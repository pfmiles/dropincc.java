package com.github.pfmiles.dropincc.testhelper;

import java.util.List;
import java.util.Map;

import com.github.pfmiles.dropincc.impl.CAlternative;
import com.github.pfmiles.dropincc.impl.GruleType;
import com.github.pfmiles.dropincc.impl.kleene.CKleeneNode;
import com.github.pfmiles.dropincc.impl.kleene.KleeneType;

/**
 * @author pf-miles
 * 
 */
public class AnalyzedLangForTest {

    public Map<KleeneType, CKleeneNode> kleeneTypeToNode;
    public Map<GruleType, List<CAlternative>> ruleTypeToAlts;

}

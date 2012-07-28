package com.github.pfmiles.dropincc.impl.syntactical.codegen;

import java.util.Map;

import com.github.pfmiles.dropincc.impl.kleene.KleeneType;
import com.github.pfmiles.dropincc.impl.llstar.LookAheadDfa;

/**
 * @author pf-miles
 * 
 */
public class KleenePredsGen extends CodeGen {

    private Map<KleeneType, LookAheadDfa> kleeneTypeToDfa;

    public KleenePredsGen(Map<KleeneType, LookAheadDfa> kleeneTypeToDfa) {
        this.kleeneTypeToDfa = kleeneTypeToDfa;
    }

    @SuppressWarnings("unchecked")
    public String render(CodeGenContext context) {
        // TODO Auto-generated method stub
        return "";
    }

}

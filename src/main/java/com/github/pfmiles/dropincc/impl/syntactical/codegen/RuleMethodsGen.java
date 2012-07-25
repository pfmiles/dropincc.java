package com.github.pfmiles.dropincc.impl.syntactical.codegen;

import java.text.MessageFormat;
import java.util.List;

import com.github.pfmiles.dropincc.impl.PredictingGrule;
import com.github.pfmiles.dropincc.impl.syntactical.codegen.rulemethods.AltSwitches;
import com.github.pfmiles.dropincc.impl.util.SeqGen;

/**
 * Responsible for recursive descent rule matching methods generation.
 * 
 * @author pf-miles
 * 
 */
public class RuleMethodsGen extends CodeGen {
    // grule {0}
    // altSwitches {1}
    private List<PredictingGrule> pgs;

    public RuleMethodsGen(List<PredictingGrule> pgs) {
        this.pgs = pgs;
    }

    @SuppressWarnings("unchecked")
    public String render(CodeGenContext context) {
        StringBuilder sb = new StringBuilder();
        // [ruleName, switches]
        MessageFormat fmt = this.getTemplate("ruleMethod.dt");
        for (PredictingGrule p : pgs) {
            // the 'varSeq' is method scoped
            context.varSeq = new SeqGen();
            sb.append(fmt.format(new String[] { p.getGruleType().toCodeGenStr(), new AltSwitches(p.getAlts()).render(context) })).append('\n');
        }
        return sb.toString();
    }

}

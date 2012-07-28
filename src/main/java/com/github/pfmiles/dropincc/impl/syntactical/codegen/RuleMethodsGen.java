package com.github.pfmiles.dropincc.impl.syntactical.codegen;

import java.text.MessageFormat;
import java.util.List;

import com.github.pfmiles.dropincc.Action;
import com.github.pfmiles.dropincc.ParamedAction;
import com.github.pfmiles.dropincc.impl.CAlternative;
import com.github.pfmiles.dropincc.impl.PredictingGrule;
import com.github.pfmiles.dropincc.impl.syntactical.codegen.rulemethods.AltSwitches;
import com.github.pfmiles.dropincc.impl.syntactical.codegen.rulemethods.code.ElementsCodeGen;
import com.github.pfmiles.dropincc.impl.util.Pair;
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
    private final MessageFormat fmt = this.getTemplate("ruleMethod.dt");
    // ruleName {0}
    // matchCode {1}
    // retVar {2}
    private final MessageFormat fmtSingleAlt = this.getTemplate("ruleMethodSingleAlt.dt");
    // actionName {0}
    // paramName {1}
    private MessageFormat actIvk = new MessageFormat("{0}.act({1})");

    // actionName {0}
    // paramName {1}
    private MessageFormat actIvkWithArg = new MessageFormat("{0}.act(arg, {1})");

    private List<PredictingGrule> pgs;

    public RuleMethodsGen(List<PredictingGrule> pgs) {
        this.pgs = pgs;
    }

    @SuppressWarnings("unchecked")
    public String render(CodeGenContext context) {
        StringBuilder sb = new StringBuilder();
        // [ruleName, switches]

        for (PredictingGrule p : pgs) {
            // the 'varSeq' is method scoped
            context.varSeq = new SeqGen();
            String ruleName = p.getGruleType().toCodeGenStr();
            if (p.getAlts().size() == 1) {
                // only one alt, need not predict
                CAlternative alt = p.getAlts().get(0);
                Pair<String, String> varAndCode = new ElementsCodeGen(alt.getMatchSequence()).render(context);
                String retVal = varAndCode.getLeft();
                if (alt.getAction() != null) {
                    // action invocation format
                    Object action = alt.getAction();
                    String actionName = context.actionFieldMapping.get(action);
                    if (action instanceof Action) {
                        retVal = actIvk.format(new String[] { actionName, retVal });
                    } else if (action instanceof ParamedAction) {
                        retVal = actIvkWithArg.format(new String[] { actionName, retVal });
                    }
                }
                sb.append(fmtSingleAlt.format(new String[] { ruleName, varAndCode.getRight(), retVal }));
            } else {
                sb.append(fmt.format(new String[] { ruleName, new AltSwitches(p.getAlts()).render(context) })).append('\n');
            }
        }
        return sb.toString();
    }
}

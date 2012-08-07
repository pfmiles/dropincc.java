package com.github.pfmiles.dropincc.impl.syntactical.codegen.rulemethods;

import java.text.MessageFormat;
import java.util.List;

import com.github.pfmiles.dropincc.Action;
import com.github.pfmiles.dropincc.ParamedAction;
import com.github.pfmiles.dropincc.impl.CAlternative;
import com.github.pfmiles.dropincc.impl.syntactical.codegen.CodeGen;
import com.github.pfmiles.dropincc.impl.syntactical.codegen.CodeGenContext;
import com.github.pfmiles.dropincc.impl.syntactical.codegen.rulemethods.code.ElementsCodeGen;
import com.github.pfmiles.dropincc.impl.util.Pair;

/**
 * @author pf-miles
 * 
 */
public class AltBacktracks extends CodeGen {

    // grule name {0}
    // match code {1}
    // grule number {2}
    // retVal {3}
    private final MessageFormat backtrackFmt = this.getTemplate("altBacktrack.dt");

    // actionName {0}
    // paramName {1}
    private MessageFormat actIvk = new MessageFormat("{0}.act({1})");

    // actionName {0}
    // paramName {1}
    private MessageFormat actIvkWithArg = new MessageFormat("{0}.act(arg, {1})");

    private List<CAlternative> alts;

    public AltBacktracks(List<CAlternative> alts) {
        this.alts = alts;
    }

    @SuppressWarnings("unchecked")
    public String render(CodeGenContext context) {
        StringBuilder code = new StringBuilder();
        String gruleName = context.curGrule.toCodeGenStr();
        Integer gruleNum = context.curGrule.getDefIndex();
        for (CAlternative alt : alts) {
            Pair<String, String> varAndCode = new ElementsCodeGen(alt.getMatchSequence()).render(context);
            String retVal = varAndCode.getLeft();
            if (alt.getAction() != null) {
                // action invocation format
                Object action = alt.getAction();
                String actionName = context.actionFieldMapping.get(action);
                if (action instanceof Action) {
                    retVal = actIvk.format(new String[] { actionName, retVal == null ? "null" : retVal });
                } else if (action instanceof ParamedAction) {
                    retVal = actIvkWithArg.format(new String[] { actionName, retVal == null ? "null" : retVal });
                }
            }
            code.append(backtrackFmt.format(new String[] { gruleName, varAndCode.getRight(), String.valueOf(gruleNum), retVal == null ? "null" : retVal })).append('\n');
        }
        return code.toString();
    }

}

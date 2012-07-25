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
public class AltSwitches extends CodeGen {

    private List<CAlternative> calts;

    // case number {0}
    // code {1}
    // retval {2}
    private MessageFormat caseFmt = this.getTemplate("altSwitchCase.dt");

    // actionName {0}
    // paramName {1}
    private MessageFormat actIvk = new MessageFormat("{0}.act({1})");

    // actionName {0}
    // paramName {1}
    private MessageFormat actIvkWithArg = new MessageFormat("{0}.act(arg, {1})");

    public AltSwitches(List<CAlternative> alts) {
        this.calts = alts;
    }

    @SuppressWarnings("unchecked")
    public String render(CodeGenContext context) {
        StringBuilder sb = new StringBuilder();
        for (int caseNum = 0; caseNum < calts.size(); caseNum++) {
            CAlternative alt = calts.get(caseNum);
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
            sb.append(caseFmt.format(new String[] { String.valueOf(caseNum), varAndCode.getRight(), retVal })).append('\n');
        }
        return sb.toString();
    }
}

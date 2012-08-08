/*******************************************************************************
 * Copyright (c) 2012 pf_miles.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     pf_miles - initial API and implementation
 ******************************************************************************/
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
    // retval {2} (with action invoke)
    private MessageFormat caseFmt = this.getTemplate("altSwitchCase.dt");

    // case number {0}
    // code {1}
    // retval {2} (with action invoke)
    // actionName {3}
    // rawValName {4}
    private MessageFormat caseFmtAction = this.getTemplate("altSwitchCaseAction.dt");

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
            String rawRetVal = retVal;
            String actionName = "null";
            if (alt.getAction() != null) {
                // action invocation format
                Object action = alt.getAction();
                actionName = context.actionFieldMapping.get(action);
                if (action instanceof Action) {
                    retVal = actIvk.format(new String[] { actionName, retVal == null ? "null" : retVal });
                } else if (action instanceof ParamedAction) {
                    retVal = actIvkWithArg.format(new String[] { actionName, retVal == null ? "null" : retVal });
                }
                sb.append(
                        caseFmtAction.format(new String[] { String.valueOf(caseNum), varAndCode.getRight(), retVal == null ? "null" : retVal, actionName,
                                rawRetVal == null ? "null" : rawRetVal })).append('\n');
            } else {
                sb.append(caseFmt.format(new String[] { String.valueOf(caseNum), varAndCode.getRight(), retVal == null ? "null" : retVal })).append('\n');
            }
        }
        return sb.toString();
    }
}

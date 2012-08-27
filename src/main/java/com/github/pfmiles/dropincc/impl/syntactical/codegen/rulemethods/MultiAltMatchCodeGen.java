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

import com.github.pfmiles.dropincc.Action;
import com.github.pfmiles.dropincc.DropinccException;
import com.github.pfmiles.dropincc.ParamedAction;
import com.github.pfmiles.dropincc.impl.CAlternative;
import com.github.pfmiles.dropincc.impl.llstar.PredictingGrule;
import com.github.pfmiles.dropincc.impl.syntactical.codegen.CodeGen;
import com.github.pfmiles.dropincc.impl.syntactical.codegen.CodeGenContext;
import com.github.pfmiles.dropincc.impl.syntactical.codegen.rulemethods.code.ElementsCodeGen;
import com.github.pfmiles.dropincc.impl.util.Pair;

/**
 * @author pf-miles
 * 
 */
public class MultiAltMatchCodeGen extends CodeGen {

    // multi alt match code(not backtracking rule) -> only string code
    // 0: ruleName
    // 1: altsSwitchCode
    private static final MessageFormat fmt = getTemplate("multiAltMatchCode.dt", MultiAltMatchCodeGen.class);
    // altCase -> only string code
    // 0: altNum
    // 1: elements code
    // 2: actionIvk
    private static final MessageFormat altCase = getTemplate("altCase.dt", MultiAltMatchCodeGen.class);
    // altCase on backtrack path -> only string code
    // 0: altNum
    // 1: elements code
    // 2: elements var
    // 3: actionName
    // 4: ruleNum
    private static final MessageFormat altCaseOnBacktrackPath = getTemplate("altCaseOnBacktrackPath.dt", MultiAltMatchCodeGen.class);

    // 0: actionName
    // 1: matchedVar
    private static final MessageFormat actionIvkFormat = new MessageFormat("{0}.act({1})");
    // 0: actionName
    // 1: matchedVar
    private static final MessageFormat paramedActionIvkFormat = new MessageFormat("{0}.act(arg, {1})");

    private PredictingGrule pg;
    private boolean generatingBacktrackCode;

    public MultiAltMatchCodeGen(PredictingGrule pg, boolean generatingBacktrackCode) {
        this.pg = pg;
        this.generatingBacktrackCode = generatingBacktrackCode;
    }

    @SuppressWarnings("unchecked")
    public String render(CodeGenContext context) {
        StringBuilder altsSwitchCode = new StringBuilder();
        for (int i = 0; i < pg.getAlts().size(); i++) {
            CAlternative alt = pg.getAlts().get(i);
            String altNum = String.valueOf(i);
            Pair<String, String> varAndCode = new ElementsCodeGen(alt.getMatchSequence(), generatingBacktrackCode).render(context);
            String elementsCode = varAndCode.getRight();
            String elementsVar = varAndCode.getLeft();
            String actionIvk = elementsVar;
            String actionName = "null";
            if (alt.getAction() != null) {
                actionName = context.actionFieldMapping.get(alt.getAction());
                if (alt.getAction() instanceof ParamedAction) {
                    actionIvk = paramedActionIvkFormat.format(new String[] { actionName, elementsVar });
                } else if (alt.getAction() instanceof Action) {
                    actionIvk = actionIvkFormat.format(new String[] { actionName, elementsVar });
                } else {
                    throw new DropinccException("Illegal action type: " + alt.getAction().getClass());
                }
            }
            if (generatingBacktrackCode) {
                altsSwitchCode.append(
                        altCaseOnBacktrackPath.format(new String[] { altNum, elementsCode, elementsVar, actionName, String.valueOf(pg.getGruleType().getDefIndex()) }))
                        .append('\n');
            } else {
                altsSwitchCode.append(altCase.format(new String[] { altNum, elementsCode, actionIvk })).append('\n');
            }
        }
        return fmt.format(new String[] { pg.getGruleType().toCodeGenStr(), altsSwitchCode.toString() });
    }

}

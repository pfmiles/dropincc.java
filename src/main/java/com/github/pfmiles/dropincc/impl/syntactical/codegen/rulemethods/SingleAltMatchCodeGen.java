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
public class SingleAltMatchCodeGen extends CodeGen {

    // not on backtrack path
    // 0: elements code
    // 1: actionIvk
    private static final String fmt = "{0}\nret = {1};";
    // on backtrack path:
    // 0: ruleNum
    // 1: elementsCode
    // 2: elementsVar
    // 3: actionName
    private static final String fmtOnBacktrackPath = getTemplate("singleAltMatchOnBacktrackPath.dt", SingleAltMatchCodeGen.class);

    // action invoke
    // 0: actionName
    // 1: matchedVar
    private static final String actionIvkFmt = "{0}.act({1})";
    // paramed action invoke
    // 0: actionName
    // 1: matchedVar
    private static final String paramedActionFmt = "{0}.act(arg, {1})";

    private PredictingGrule pg;
    // is generating 'on backtrack path' code
    private boolean onBacktrackPath;

    public SingleAltMatchCodeGen(PredictingGrule pg, boolean onBacktrackPath) {
        this.pg = pg;
        this.onBacktrackPath = onBacktrackPath;
    }

    @SuppressWarnings("unchecked")
    public String render(CodeGenContext context) {
        CAlternative alt = pg.getAlts().get(0);
        Pair<String, String> varAndCode = new ElementsCodeGen(alt.getMatchSequence(), onBacktrackPath).render(context);
        String elementsCode = varAndCode.getRight();
        String elementsVar = varAndCode.getLeft();
        String actionName = "null";
        String actionIvk = elementsVar;
        if (alt.getAction() != null) {
            actionName = context.actionFieldMapping.get(alt.getAction());
            if (alt.getAction() instanceof Action) {
                actionIvk = MessageFormat.format(actionIvkFmt, actionName, elementsVar);
            } else if (alt.getAction() instanceof ParamedAction) {
                actionIvk = MessageFormat.format(paramedActionFmt, actionName, elementsVar);
            } else {
                throw new DropinccException("Illegal action type: " + alt.getAction().getClass());
            }
        }
        if (onBacktrackPath) {
            return MessageFormat.format(fmtOnBacktrackPath, String.valueOf(pg.getGruleType().getDefIndex()), elementsCode, elementsVar, actionName);
        } else {
            return MessageFormat.format(fmt, elementsCode, actionIvk);
        }
    }

}

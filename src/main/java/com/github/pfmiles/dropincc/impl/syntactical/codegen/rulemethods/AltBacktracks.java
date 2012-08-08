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

    // grule number {0}
    // match code {1}
    // retVal {2}
    // actoinName {3}
    private final MessageFormat backtrackFmt = this.getTemplate("altBacktrack.dt");

    private List<CAlternative> alts;

    public AltBacktracks(List<CAlternative> alts) {
        this.alts = alts;
    }

    @SuppressWarnings("unchecked")
    public String render(CodeGenContext context) {
        StringBuilder code = new StringBuilder();
        for (CAlternative alt : alts) {
            Pair<String, String> varAndCode = new ElementsCodeGen(alt.getMatchSequence()).render(context);
            String retVal = varAndCode.getLeft();
            String actionName = "null";
            if (alt.getAction() != null) {
                // action invocation format
                Object action = alt.getAction();
                actionName = context.actionFieldMapping.get(action);
            }
            code.append(
                    backtrackFmt.format(new String[] { String.valueOf(context.curGrule.getDefIndex()), varAndCode.getRight(), retVal == null ? "null" : retVal,
                            actionName })).append('\n');
        }
        return code.toString();
    }

}

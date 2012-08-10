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
package com.github.pfmiles.dropincc.impl.syntactical.codegen;

import java.text.MessageFormat;
import java.util.List;

import com.github.pfmiles.dropincc.Action;
import com.github.pfmiles.dropincc.ParamedAction;
import com.github.pfmiles.dropincc.impl.CAlternative;
import com.github.pfmiles.dropincc.impl.llstar.PredictingGrule;
import com.github.pfmiles.dropincc.impl.syntactical.codegen.rulemethods.AltBacktracks;
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
    private static final MessageFormat fmt = getTemplate("ruleMethod.dt", RuleMethodsGen.class);
    // ruleName {0}
    // matchCode {1}
    // retVar {2} (with action invoke)
    private static final MessageFormat fmtSingleAlt = getTemplate("ruleMethodSingleAlt.dt", RuleMethodsGen.class);
    // ruleName {0}
    // matchCode {1}
    // retVar {2} (with action invoke)
    // actionName {3}
    // rawRetVar {4} (without actoin invoke)
    private static final MessageFormat fmtSingleAltAction = getTemplate("ruleMethodSingleAltAction.dt", RuleMethodsGen.class);
    // actionName {0}
    // paramName {1}
    private static final MessageFormat actIvk = new MessageFormat("{0}.act({1})");

    // actionName {0}
    // paramName {1}
    private static final MessageFormat actIvkWithArg = new MessageFormat("{0}.act(arg, {1})");

    // gruleName {0}
    // altBackTracks {1}
    private static final MessageFormat fmtBackTrack = getTemplate("ruleMethodBackTrack.dt", RuleMethodsGen.class);

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
            context.curGrule = p.getGruleType();
            String ruleName = p.getGruleType().toCodeGenStr();
            if (p.getAlts().size() == 1) {
                // only one alt, need not predict
                CAlternative alt = p.getAlts().get(0);
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
                            fmtSingleAltAction.format(new String[] { ruleName, varAndCode.getRight(), retVal == null ? "null" : retVal, actionName,
                                    rawRetVal == null ? "null" : rawRetVal })).append('\n');
                } else {
                    sb.append(fmtSingleAlt.format(new String[] { ruleName, varAndCode.getRight(), retVal == null ? "null" : retVal })).append('\n');
                }
            } else if (p.isBacktrack()) {
                sb.append(fmtBackTrack.format(new String[] { ruleName, new AltBacktracks(p.getAlts()).render(context) })).append('\n');
            } else {
                sb.append(fmt.format(new String[] { ruleName, new AltSwitches(p.getAlts()).render(context) })).append('\n');
            }
        }
        return sb.toString();
    }
}

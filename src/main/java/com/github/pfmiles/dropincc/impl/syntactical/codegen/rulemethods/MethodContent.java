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

import com.github.pfmiles.dropincc.impl.llstar.PredictingGrule;
import com.github.pfmiles.dropincc.impl.syntactical.codegen.CodeGen;
import com.github.pfmiles.dropincc.impl.syntactical.codegen.CodeGenContext;

/**
 * @author pf-miles
 * 
 */
public class MethodContent extends CodeGen {

    // method content on path of backtracking: (try to assign to ret) -> only
    // string code
    // 0: ruleNum
    // 1: matchCode
    // 2: matchCode is backtracking
    private static final MessageFormat onBacktrackPathFmt = getTemplate("methodContentOnBacktrackPath.dt", MethodContent.class);

    private PredictingGrule pg;

    public MethodContent(PredictingGrule p) {
        this.pg = p;
    }

    @SuppressWarnings("unchecked")
    public String render(CodeGenContext context) {
        String matchCode = null;
        if (this.pg.getAlts().size() == 1) {
            // single alt rule, same to elements match code
            matchCode = new SingleAltMatchCodeGen(pg, false).render(context);
        } else {
            if (this.pg.isBacktrack()) {
                // this rule itself backtracking
                // backtrack rule
                matchCode = new MultiAltMatchBacktrackGen(this.pg, false).render(context);
            } else {
                // normal alt switch rule
                matchCode = new MultiAltMatchCodeGen(this.pg, false).render(context);
            }
        }
        if (pg.isOnBacktrackPath()) {
            String matchCodeOnPath = null;
            if (this.pg.getAlts().size() == 1) {
                // single alt rule, same to elements match code
                matchCodeOnPath = new SingleAltMatchCodeGen(pg, true).render(context);
            } else {
                if (this.pg.isBacktrack()) {
                    // backtrack rule
                    matchCodeOnPath = new MultiAltMatchBacktrackGen(this.pg, true).render(context);
                } else {
                    // normal alt switch rule
                    matchCodeOnPath = new MultiAltMatchCodeGen(this.pg, true).render(context);
                }
            }
            return onBacktrackPathFmt.format(new String[] { String.valueOf(pg.getGruleType().getDefIndex()), matchCode, matchCodeOnPath });
        } else {
            return matchCode;
        }
    }
}

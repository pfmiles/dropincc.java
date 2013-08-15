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
package com.github.pfmiles.dropincc.impl.syntactical.codegen.rulemethods.code;

import java.text.MessageFormat;

import com.github.pfmiles.dropincc.DropinccException;
import com.github.pfmiles.dropincc.impl.kleene.KleeneCrossType;
import com.github.pfmiles.dropincc.impl.kleene.KleeneStarType;
import com.github.pfmiles.dropincc.impl.kleene.OptionalType;
import com.github.pfmiles.dropincc.impl.llstar.PredictingKleene;
import com.github.pfmiles.dropincc.impl.syntactical.codegen.CodeGen;
import com.github.pfmiles.dropincc.impl.syntactical.codegen.CodeGenContext;
import com.github.pfmiles.dropincc.impl.util.Pair;

/**
 * Responsible for kleene node code generation.
 * 
 * @author pf-miles
 * 
 */
public class KleeneEleGen extends CodeGen {

    // varName {0}
    // ksName {1}
    // elementsCode {2}
    // elementVar {3}
    // ruleName {4}
    private static final String ksFmt = getTemplate("kleeneStar.dt", KleeneEleGen.class);
    private static final String ksFmtBacktracking = getTemplate("kleeneStarBacktracking.dt", KleeneEleGen.class);
    private static final String kcFmt = getTemplate("kleeneCross.dt", KleeneEleGen.class);
    private static final String kcFmtBacktracking = getTemplate("kleeneCrossBacktracking.dt", KleeneEleGen.class);
    private static final String opFmt = getTemplate("optional.dt", KleeneEleGen.class);
    // varName {0}
    // ksNum {1}
    // elementsCode {2}
    // elementVar {3}
    private static final String ksBackFmt = getTemplate("kleeneStarBacktrack.dt", KleeneEleGen.class);
    private static final String ksBackFmtBacktracking = getTemplate("kleeneStarBacktrackBacktracking.dt", KleeneEleGen.class);
    // varName {0}
    // ksNum {1}
    // elementsCode {2}
    // elementVar {3}
    // elementsCodePlus {4}
    // elementVarPlus {5}
    private static final String kcBackFmt = getTemplate("kleeneCrossBacktrack.dt", KleeneEleGen.class);
    private static final String kcBackFmtBacktracking = getTemplate("kleeneCrossBacktrackBacktracking.dt", KleeneEleGen.class);
    // varName {0}
    // ksNum {1}
    // elementsCode {2}
    // elementVar {3}
    private static final String opBackFmt = getTemplate("optionalBacktrack.dt", KleeneEleGen.class);
    private static final String opBackFmtBacktracking = getTemplate("optionalBacktrackBacktracking.dt", KleeneEleGen.class);

    private PredictingKleene pk;
    private boolean genBacktrackCode;

    public KleeneEleGen(PredictingKleene pk, boolean generatingBacktrackCode) {
        this.pk = pk;
        this.genBacktrackCode = generatingBacktrackCode;
    }

    // returns [varName, code]
    @SuppressWarnings("unchecked")
    public Pair<String, String> render(CodeGenContext context) {
        String varName = "p" + context.varSeq.next();
        String kName = pk.getKleeneType().toCodeGenStr();
        // kleene defNum is based from 1000 when code gen, to distinguish from
        // normal grule's index
        // XXX this may not good...
        String knum = String.valueOf(pk.getKleeneType().getDefIndex() + 1000);
        String code = null;
        Pair<String, String> varAndCode = new ElementsCodeGen(pk.getMatchSequence(), this.genBacktrackCode || this.pk.isBacktrack()).render(context);
        String elementsCode = varAndCode.getRight();
        String elementsVar = varAndCode.getLeft();
        if (pk.getKleeneType() instanceof KleeneStarType) {
            if (pk.isBacktrack()) {
                if (this.genBacktrackCode) {
                    code = MessageFormat.format(ksBackFmtBacktracking, varName, knum, elementsCode, elementsVar);
                } else {
                    code = MessageFormat.format(ksBackFmt, varName, knum, elementsCode, elementsVar);
                }
            } else {
                if (this.genBacktrackCode) {
                    code = MessageFormat.format(ksFmtBacktracking, varName, kName, elementsCode, elementsVar, context.curGrule.toCodeGenStr());
                } else {
                    code = MessageFormat.format(ksFmt, varName, kName, elementsCode, elementsVar, context.curGrule.toCodeGenStr());
                }
            }
        } else if (pk.getKleeneType() instanceof KleeneCrossType) {
            if (pk.isBacktrack()) {
                Pair<String, String> varAndCodePlus = new ElementsCodeGen(pk.getMatchSequence(), this.genBacktrackCode).render(context);
                String elementsCodePlus = varAndCodePlus.getRight();
                String elementsVarPlus = varAndCodePlus.getLeft();
                if (this.genBacktrackCode) {
                    code = MessageFormat.format(kcBackFmtBacktracking, varName, knum, elementsCode, elementsVar, elementsCodePlus, elementsVarPlus);
                } else {
                    code = MessageFormat.format(kcBackFmt, varName, knum, elementsCode, elementsVar, elementsCodePlus, elementsVarPlus);
                }
            } else {
                if (this.genBacktrackCode) {
                    code = MessageFormat.format(kcFmtBacktracking, varName, kName, elementsCode, elementsVar, context.curGrule.toCodeGenStr());
                } else {
                    code = MessageFormat.format(kcFmt, varName, kName, elementsCode, elementsVar, context.curGrule.toCodeGenStr());
                }
            }
        } else if (pk.getKleeneType() instanceof OptionalType) {
            if (pk.isBacktrack()) {
                if (this.genBacktrackCode) {
                    code = MessageFormat.format(opBackFmtBacktracking, varName, knum, elementsCode, elementsVar);
                } else {
                    code = MessageFormat.format(opBackFmt, varName, knum, elementsCode, elementsVar);
                }
            } else {
                code = MessageFormat.format(opFmt, varName, kName, elementsCode, elementsVar, context.curGrule.toCodeGenStr());
            }
        } else {
            throw new DropinccException("Unhandled code generation kleene node type: " + pk.getKleeneType());
        }
        return new Pair<String, String>(varName, code);
    }

}

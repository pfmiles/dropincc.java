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
import com.github.pfmiles.dropincc.impl.kleene.KleeneType;
import com.github.pfmiles.dropincc.impl.kleene.OptionalType;
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
    private final MessageFormat ksFmt = this.getTemplate("kleeneStar.dt");
    private final MessageFormat kcFmt = this.getTemplate("kleeneCross.dt");
    private final MessageFormat opFmt = this.getTemplate("optional.dt");
    // varName {0}
    // ksNum {1}
    // elementsCode {2}
    // elementVar {3}
    private MessageFormat ksBackFmt = this.getTemplate("kleeneStarBacktrack.dt");
    // varName {0}
    // ksNum {1}
    // elementsCode {2}
    // elementVar {3}
    // elementsCodePlus {4}
    // elementVarPlus {5}
    private MessageFormat kcBackFmt = this.getTemplate("kleeneCrossBacktrack.dt");
    // varName {0}
    // ksNum {1}
    // elementsCode {2}
    // elementVar {3}
    private MessageFormat opBackFmt = this.getTemplate("optionalBacktrack.dt");

    private KleeneType ele;

    public KleeneEleGen(KleeneType ele) {
        this.ele = ele;
    }

    // returns [varName, code]
    @SuppressWarnings("unchecked")
    public Pair<String, String> render(CodeGenContext context) {
        String varName = "p" + context.varSeq.next();
        String kName = ele.toCodeGenStr();
        // kleene defNum is based from 1000 when code gen, to distinguish from
        // normal grule's index
        int knum = ele.getDefIndex() + 1000;
        Pair<String, String> varAndCode = new ElementsCodeGen(context.kleeneTypeToNode.get(ele)).render(context);
        if (this.ele instanceof KleeneStarType) {
            if (context.backtrackKleenes.contains(this.ele)) {
                return new Pair<String, String>(varName, ksBackFmt.format(new String[] { varName, String.valueOf(knum), varAndCode.getRight(), varAndCode.getLeft() }));
            } else {
                return new Pair<String, String>(varName, ksFmt.format(new String[] { varName, kName, varAndCode.getRight(), varAndCode.getLeft(),
                        context.curGrule.toCodeGenStr() }));
            }
        } else if (this.ele instanceof KleeneCrossType) {
            if (context.backtrackKleenes.contains(this.ele)) {
                Pair<String, String> varAndCodePlus = new ElementsCodeGen(context.kleeneTypeToNode.get(ele)).render(context);
                return new Pair<String, String>(varName, kcBackFmt.format(new String[] { varName, String.valueOf(knum), varAndCode.getRight(), varAndCode.getLeft(),
                        varAndCodePlus.getRight(), varAndCodePlus.getLeft() }));
            } else {
                return new Pair<String, String>(varName, kcFmt.format(new String[] { varName, kName, varAndCode.getRight(), varAndCode.getLeft(),
                        context.curGrule.toCodeGenStr() }));
            }
        } else if (this.ele instanceof OptionalType) {
            if (context.backtrackKleenes.contains(this.ele)) {
                return new Pair<String, String>(varName, opBackFmt.format(new String[] { varName, String.valueOf(knum), varAndCode.getRight(), varAndCode.getLeft() }));
            } else {
                return new Pair<String, String>(varName, opFmt.format(new String[] { varName, kName, varAndCode.getRight(), varAndCode.getLeft(),
                        context.curGrule.toCodeGenStr() }));
            }
        } else {
            throw new DropinccException("Unhandled code generation kleene node type: " + this.ele);
        }
    }

}

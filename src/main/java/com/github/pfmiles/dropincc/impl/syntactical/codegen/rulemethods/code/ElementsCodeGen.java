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
import java.util.ArrayList;
import java.util.List;

import com.github.pfmiles.dropincc.DropinccException;
import com.github.pfmiles.dropincc.impl.EleType;
import com.github.pfmiles.dropincc.impl.GruleType;
import com.github.pfmiles.dropincc.impl.TokenType;
import com.github.pfmiles.dropincc.impl.kleene.KleeneType;
import com.github.pfmiles.dropincc.impl.syntactical.codegen.CodeGen;
import com.github.pfmiles.dropincc.impl.syntactical.codegen.CodeGenContext;
import com.github.pfmiles.dropincc.impl.util.Pair;
import com.github.pfmiles.dropincc.impl.util.Util;

/**
 * List of elements code generation.
 * 
 * @author pf-miles
 * 
 */
public class ElementsCodeGen extends CodeGen {

    // varName {0}
    // varContent {1}
    private static final String multiVarFmt = getTemplate("multiVar.dt", ElementsCodeGen.class);
    private static final String multiVarBacktrackFmt = getTemplate("multiVarBacktrack.dt", ElementsCodeGen.class);

    // varName {0}
    // tokenTypeName {1}
    private static final String tokenMatchFmt = "Object {0} = this.match({1});";

    // varName {0}
    // ruleName {1}
    private static final String ruleIvkFmt = "Object {0} = {1}(arg);";

    private List<EleType> matchSeq;
    private boolean generatingBacktrackCode;

    public ElementsCodeGen(List<EleType> matchSequence, boolean generatingBacktrackCode) {
        this.matchSeq = matchSequence;
        this.generatingBacktrackCode = generatingBacktrackCode;
    }

    // returns pair[varName, codeBlock]
    @SuppressWarnings("unchecked")
    public Pair<String, String> render(CodeGenContext context) {
        StringBuilder sb = new StringBuilder();
        List<String> vars = new ArrayList<String>();
        for (EleType ele : this.matchSeq) {
            if (ele instanceof TokenType) {
                String varName = "p" + context.varSeq.next();
                sb.append(MessageFormat.format(tokenMatchFmt, varName, ((TokenType) ele).toCodeGenStr())).append('\n');
                vars.add(varName);
            } else if (ele instanceof GruleType) {
                String varName = "p" + context.varSeq.next();
                sb.append(MessageFormat.format(ruleIvkFmt, varName, ((GruleType) ele).toCodeGenStr())).append('\n');
                vars.add(varName);
            } else if (ele instanceof KleeneType) {
                Pair<String, String> varAndCode = new KleeneEleGen(context.kleeneTypeToPredicting.get((KleeneType) ele), this.generatingBacktrackCode)
                        .render(context);
                vars.add(varAndCode.getLeft());
                sb.append(varAndCode.getRight());
            } else {
                throw new DropinccException("Unsupported code generation element type: " + ele);
            }
        }
        String retVar = null;
        if (vars.size() == 1) {
            retVar = vars.get(0);
        } else if (vars.size() > 1) {
            String ctt = Util.join(", ", vars);
            retVar = "p" + context.varSeq.next();
            if (this.generatingBacktrackCode) {
                sb.append(MessageFormat.format(multiVarBacktrackFmt, retVar, ctt)).append('\n');
            } else {
                sb.append(MessageFormat.format(multiVarFmt, retVar, ctt)).append('\n');
            }
        }
        return new Pair<String, String>(retVar, sb.toString());
    }
}

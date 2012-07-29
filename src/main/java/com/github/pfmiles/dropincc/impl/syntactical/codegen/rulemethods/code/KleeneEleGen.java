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

    private KleeneType ele;

    public KleeneEleGen(KleeneType ele) {
        this.ele = ele;
    }

    // returns [varName, code]
    @SuppressWarnings("unchecked")
    public Pair<String, String> render(CodeGenContext context) {
        String varName = "p" + context.varSeq.next();
        String kName = ele.toCodeGenStr();
        Pair<String, String> varAndCode = new ElementsCodeGen(context.kleeneTypeToNode.get(ele).getContents()).render(context);
        if (this.ele instanceof KleeneStarType) {
            return new Pair<String, String>(varName, ksFmt.format(new String[] { varName, kName, varAndCode.getRight(), varAndCode.getLeft(),
                    context.curGrule.toCodeGenStr() }));
        } else if (this.ele instanceof KleeneCrossType) {
            return new Pair<String, String>(varName, kcFmt.format(new String[] { varName, kName, varAndCode.getRight(), varAndCode.getLeft(),
                    context.curGrule.toCodeGenStr() }));
        } else if (this.ele instanceof OptionalType) {
            return new Pair<String, String>(varName, opFmt.format(new String[] { varName, kName, varAndCode.getRight(), varAndCode.getLeft(),
                    context.curGrule.toCodeGenStr() }));
        } else {
            throw new DropinccException("Unhandled code generation kleene node type: " + this.ele);
        }
    }

}

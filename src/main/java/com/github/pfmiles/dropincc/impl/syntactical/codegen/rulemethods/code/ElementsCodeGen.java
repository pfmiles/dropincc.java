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

/**
 * List of elements code generation.
 * 
 * @author pf-miles
 * 
 */
public class ElementsCodeGen extends CodeGen {

    // varName {0}
    // varContent {1}
    private static final MessageFormat multiVarFmt = new MessageFormat("Object[] {0} = new Object[] '{' {1} '}';");

    // varName {0}
    // tokenTypeName {1}
    private static final MessageFormat tokenMatchFmt = new MessageFormat("Object {0} = this.match({1});");

    // varName {0}
    // ruleName {1}
    private static final MessageFormat ruleIvkFmt = new MessageFormat("Object {0} = {1}();");

    private List<EleType> matchSeq;

    public ElementsCodeGen(List<EleType> matchSequence) {
        this.matchSeq = matchSequence;
    }

    // returns pair[varName, codeBlock]
    @SuppressWarnings("unchecked")
    public Pair<String, String> render(CodeGenContext context) {
        StringBuilder sb = new StringBuilder();
        List<String> vars = new ArrayList<String>();
        for (EleType ele : this.matchSeq) {
            if (ele instanceof TokenType) {
                String varName = "p" + context.varSeq.next();
                sb.append(tokenMatchFmt.format(new String[] { varName, ((TokenType) ele).toCodeGenStr() })).append('\n');
                vars.add(varName);
            } else if (ele instanceof GruleType) {
                String varName = "p" + context.varSeq.next();
                sb.append(ruleIvkFmt.format(new String[] { varName, ((GruleType) ele).toCodeGenStr() })).append('\n');
                vars.add(varName);
            } else if (ele instanceof KleeneType) {
                Pair<String, String> varAndCode = new KleeneEleGen((KleeneType) ele).render(context);
                vars.add(varAndCode.getLeft());
                sb.append(varAndCode.getRight()).append('\n');
            } else {
                throw new DropinccException("Unsupported code generation element type: " + ele);
            }
        }
        String retVar = null;
        if (vars.size() == 1) {
            retVar = vars.get(0);
        } else if (vars.size() > 1) {
            StringBuilder ctt = new StringBuilder();
            for (String v : vars) {
                if (ctt.length() != 0)
                    ctt.append(", ");
                ctt.append(v);
            }
            retVar = "p" + context.varSeq.next();
            sb.append(multiVarFmt.format(new String[] { retVar, ctt.toString() })).append('\n');
        }
        return new Pair<String, String>(retVar, sb.toString());
    }
}

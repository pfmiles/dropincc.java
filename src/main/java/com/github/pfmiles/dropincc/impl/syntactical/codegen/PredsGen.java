package com.github.pfmiles.dropincc.impl.syntactical.codegen;

import java.text.MessageFormat;
import java.util.List;

import com.github.pfmiles.dropincc.Predicate;
import com.github.pfmiles.dropincc.impl.GruleType;

/**
 * @author pf-miles
 * 
 */
public class PredsGen extends CodeGen {

    // [ruleName, altIndex]
    private static final MessageFormat fmt = new MessageFormat("public Predicate {0}Pred{1};// {0} rule pred {1}");

    // list([grule, altInfex, predObj])
    private List<Object[]> predInfos;

    public PredsGen(List<Object[]> predInfos) {
        this.predInfos = predInfos;
    }

    @SuppressWarnings("unchecked")
    public String render(CodeGenContext context) {
        StringBuilder sb = new StringBuilder();
        for (Object[] predInfo : predInfos) {
            String ruleName = ((GruleType) predInfo[0]).toCodeGenStr();
            String altIndex = String.valueOf(predInfo[1]);
            context.fieldPredsMapping.put(ruleName + "Pred" + altIndex, (Predicate) predInfo[2]);
            sb.append(fmt.format(new String[] { ruleName, altIndex })).append("\n");
        }
        return sb.toString();
    }

}

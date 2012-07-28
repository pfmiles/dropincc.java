package com.github.pfmiles.dropincc.impl.syntactical.codegen;

import java.text.MessageFormat;
import java.util.List;

import com.github.pfmiles.dropincc.Action;
import com.github.pfmiles.dropincc.DropinccException;
import com.github.pfmiles.dropincc.ParamedAction;
import com.github.pfmiles.dropincc.impl.GruleType;

/**
 * @author pf-miles
 * 
 */
public class AltsActionsGen extends CodeGen {

    // [actionCls, ruleName, altIndex]
    private static final MessageFormat fmt = new MessageFormat("public {0} {1}Action{2};// {1} rule alt {2} action");

    // list([grule, altIndex, actionObj])
    private List<Object[]> actionInfos;

    public AltsActionsGen(List<Object[]> actionInfos) {
        this.actionInfos = actionInfos;
    }

    @SuppressWarnings("unchecked")
    public String render(CodeGenContext context) {
        StringBuilder sb = new StringBuilder();
        // [grule, altIndex, actionObj]
        for (Object[] actionInfo : actionInfos) {
            Object action = actionInfo[2];
            String actionCls = null;
            if (action instanceof Action) {
                actionCls = "Action";
            } else if (action instanceof ParamedAction) {
                actionCls = "ParamedAction";
            } else {
                throw new DropinccException("Invalid action object: " + action);
            }
            String ruleName = ((GruleType) actionInfo[0]).toCodeGenStr();
            String altIndex = String.valueOf(actionInfo[1]);
            String fname = ruleName + "Action" + altIndex;
            context.fieldAltsActionMapping.put(fname, action);
            context.actionFieldMapping.put(action, fname);
            sb.append(fmt.format(new String[] { actionCls, ruleName, altIndex })).append("\n");
        }
        return sb.toString();
    }
}

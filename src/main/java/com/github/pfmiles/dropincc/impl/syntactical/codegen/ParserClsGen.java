package com.github.pfmiles.dropincc.impl.syntactical.codegen;

import com.github.pfmiles.dropincc.impl.GruleType;

/**
 * Generates the whole parser class.
 * 
 * @author pf-miles
 * 
 */
public class ParserClsGen extends CodeGen {
    private String parserClsName;// {0}
    private CodeGen tokenTypes; // {1}
    private CodeGen actions;// {2}
    private CodeGen preds;// {3}
    private GruleType startRule;// {4}
    private CodeGen ruleMethods;// {5}
    private CodeGen ruleAltsPredictingMethods; // {6}
    private CodeGen kleenePredictingMethods; // {7}

    /**
     * Construct a parserCls using all the components.
     * 
     * @param parserClsName
     * @param tokenTypes
     * @param actions
     * @param preds
     * @param startRule
     * @param ruleMethods
     * @param ruleAltsPredictingMethods
     * @param kleenePredictingMethods
     */
    public ParserClsGen(String parserClsName, CodeGen tokenTypes, CodeGen actions, CodeGen preds, GruleType startRule, CodeGen ruleMethods,
            CodeGen ruleAltsPredictingMethods, CodeGen kleenePredictingMethods) {
        super();
        this.parserClsName = parserClsName;
        this.tokenTypes = tokenTypes;
        this.actions = actions;
        this.preds = preds;
        this.startRule = startRule;
        this.ruleMethods = ruleMethods;
        this.ruleAltsPredictingMethods = ruleAltsPredictingMethods;
        this.kleenePredictingMethods = kleenePredictingMethods;
    }

    @SuppressWarnings("unchecked")
    public String render(CodeGenContext context) {
        return this.getTemplate("parserCls.dt").format(
                new String[] { this.parserClsName, this.tokenTypes.render(context), this.actions.render(context), this.preds.render(context),
                        this.startRule.toCodeGenStr(), this.ruleMethods.render(context), this.ruleAltsPredictingMethods.render(context),
                        this.kleenePredictingMethods.render(context) });
    }
}

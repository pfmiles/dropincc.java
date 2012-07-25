package com.github.pfmiles.dropincc.impl.syntactical.codegen;

/**
 * @author pf-miles
 * 
 */
public class ParserCls extends CodeGen {
    private String parserClsName;// {0}
    private CodeGen tokenTypes; // {1}
    private CodeGen actions;// {2}
    private CodeGen preds;// {3}
    private CodeGen startRule;// {4}
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
    public ParserCls(String parserClsName, CodeGen tokenTypes, CodeGen actions, CodeGen preds, CodeGen startRule, CodeGen ruleMethods, CodeGen ruleAltsPredictingMethods,
            CodeGen kleenePredictingMethods) {
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

    public String render(CodeGenContext context) {
        return this.getTemplate("parserCls.dt").format(
                new String[] { this.parserClsName, this.tokenTypes.render(context), this.actions.render(context), this.preds.render(context),
                        this.startRule.render(context), this.ruleMethods.render(context), this.ruleAltsPredictingMethods.render(context),
                        this.kleenePredictingMethods.render(context) });
    }
}

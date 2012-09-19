package com.github.pfmiles.dropincc.template.impl;

import com.github.pfmiles.dropincc.template.impl.ast.AndExprNode;
import com.github.pfmiles.dropincc.template.impl.ast.BoolExprNode;
import com.github.pfmiles.dropincc.template.impl.ast.ForEachNode;
import com.github.pfmiles.dropincc.template.impl.ast.IfElseNode;
import com.github.pfmiles.dropincc.template.impl.ast.RefOutputNode;
import com.github.pfmiles.dropincc.template.impl.ast.TemplateNode;
import com.github.pfmiles.dropincc.template.impl.ast.TermNode;
import com.github.pfmiles.dropincc.template.impl.ast.TextNode;
import com.github.pfmiles.dropincc.template.impl.ast.ValueNode;

/**
 * Dropin template AST visitor
 * 
 * @author pf-miles
 * 
 */
public interface DtVisitor {

    <T> T visitTemplate(TemplateNode templateNode, Object param);

    <T> T visitIfElse(IfElseNode ifElseNode, Object param);

    <T> T visitText(TextNode textNode, Object param);

    <T> T visitForEach(ForEachNode forEachNode, Object param);

    <T> T visitRefOutput(RefOutputNode refOutputNode, Object param);

    <T> T visitBoolExpr(BoolExprNode boolExprNode, Object param);

    <T> T visitAndExpr(AndExprNode andExprNode, Object param);

    <T> T visitTerm(TermNode termNode, Object param);

    <T> T visitValue(ValueNode valueNode, Object param);

}

package com.github.pfmiles.dropincc.template.impl.visitor;

import com.github.pfmiles.dropincc.template.impl.DtVisitor;
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
 * @author pf-miles
 *
 */
public class DropinTemplateToJavaVisitor implements DtVisitor {

    /* (non-Javadoc)
     * @see com.github.pfmiles.dropincc.template.impl.DtVisitor#visitTemplate(com.github.pfmiles.dropincc.template.impl.ast.TemplateNode, java.lang.Object)
     */
    @Override
    public <T> T visitTemplate(TemplateNode templateNode, Object param) {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.github.pfmiles.dropincc.template.impl.DtVisitor#visitIfElse(com.github.pfmiles.dropincc.template.impl.ast.IfElseNode, java.lang.Object)
     */
    @Override
    public <T> T visitIfElse(IfElseNode ifElseNode, Object param) {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.github.pfmiles.dropincc.template.impl.DtVisitor#visitText(com.github.pfmiles.dropincc.template.impl.ast.TextNode, java.lang.Object)
     */
    @Override
    public <T> T visitText(TextNode textNode, Object param) {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.github.pfmiles.dropincc.template.impl.DtVisitor#visitForEach(com.github.pfmiles.dropincc.template.impl.ast.ForEachNode, java.lang.Object)
     */
    @Override
    public <T> T visitForEach(ForEachNode forEachNode, Object param) {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.github.pfmiles.dropincc.template.impl.DtVisitor#visitRefOutput(com.github.pfmiles.dropincc.template.impl.ast.RefOutputNode, java.lang.Object)
     */
    @Override
    public <T> T visitRefOutput(RefOutputNode refOutputNode, Object param) {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.github.pfmiles.dropincc.template.impl.DtVisitor#visitBoolExpr(com.github.pfmiles.dropincc.template.impl.ast.BoolExprNode, java.lang.Object)
     */
    @Override
    public <T> T visitBoolExpr(BoolExprNode boolExprNode, Object param) {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.github.pfmiles.dropincc.template.impl.DtVisitor#visitAndExpr(com.github.pfmiles.dropincc.template.impl.ast.AndExprNode, java.lang.Object)
     */
    @Override
    public <T> T visitAndExpr(AndExprNode andExprNode, Object param) {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.github.pfmiles.dropincc.template.impl.DtVisitor#visitTerm(com.github.pfmiles.dropincc.template.impl.ast.TermNode, java.lang.Object)
     */
    @Override
    public <T> T visitTerm(TermNode termNode, Object param) {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.github.pfmiles.dropincc.template.impl.DtVisitor#visitValue(com.github.pfmiles.dropincc.template.impl.ast.ValueNode, java.lang.Object)
     */
    @Override
    public <T> T visitValue(ValueNode valueNode, Object param) {
        // TODO Auto-generated method stub
        return null;
    }

}

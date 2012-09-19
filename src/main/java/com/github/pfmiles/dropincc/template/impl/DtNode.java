package com.github.pfmiles.dropincc.template.impl;

/**
 * Dropin template AST node interface
 * 
 * @author pf-miles
 * 
 */
public interface DtNode<P> {

    /**
     * Visit this node with the specified visitor and parameter.
     * 
     * @param visitor
     * @param param
     * @return
     */
    <T> T accept(DtVisitor visitor, P param);
}

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
package com.github.pfmiles.dropincc.impl;

import java.util.Map;

import com.github.pfmiles.dropincc.Element;
import com.github.pfmiles.dropincc.Grule;
import com.github.pfmiles.dropincc.TokenDef;
import com.github.pfmiles.dropincc.impl.kleene.AbstractKleeneNode;
import com.github.pfmiles.dropincc.impl.kleene.KleeneType;

/**
 * @author pf-miles
 * 
 */
public class TypeMappingParam {
    private Map<TokenDef, TokenType> tokenTypeMapping;
    private Map<Grule, GruleType> gruleTypeMapping;
    private Map<Element, SpecialType> specialTypeMapping;
    private Map<AbstractKleeneNode, KleeneType> kleeneTypeMapping;

    public TypeMappingParam(Map<TokenDef, TokenType> tokenTypeMapping, Map<Grule, GruleType> gruleTypeMapping,
            Map<Element, SpecialType> specialTypeMapping,
            Map<AbstractKleeneNode, KleeneType> kleeneTypeMapping) {
        super();
        this.tokenTypeMapping = tokenTypeMapping;
        this.gruleTypeMapping = gruleTypeMapping;
        this.specialTypeMapping = specialTypeMapping;
        this.kleeneTypeMapping = kleeneTypeMapping;
    }

    public Map<TokenDef, TokenType> getTokenTypeMapping() {
        return tokenTypeMapping;
    }

    public void setTokenTypeMapping(Map<TokenDef, TokenType> tokenTypeMapping) {
        this.tokenTypeMapping = tokenTypeMapping;
    }

    public Map<Grule, GruleType> getGruleTypeMapping() {
        return gruleTypeMapping;
    }

    public void setGruleTypeMapping(Map<Grule, GruleType> gruleTypeMapping) {
        this.gruleTypeMapping = gruleTypeMapping;
    }

    public Map<Element, SpecialType> getSpecialTypeMapping() {
        return specialTypeMapping;
    }

    public void setSpecialTypeMapping(Map<Element, SpecialType> specialTypeMapping) {
        this.specialTypeMapping = specialTypeMapping;
    }

    public Map<AbstractKleeneNode, KleeneType> getKleeneTypeMapping() {
        return kleeneTypeMapping;
    }

    public void setKleeneTypeMapping(Map<AbstractKleeneNode, KleeneType> kleeneTypeMapping) {
        this.kleeneTypeMapping = kleeneTypeMapping;
    }
}

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
package com.github.pfmiles.dropincc.impl.syntactical.codegen;

import java.util.HashMap;
import java.util.Map;

import com.github.pfmiles.dropincc.Predicate;
import com.github.pfmiles.dropincc.impl.TokenType;
import com.github.pfmiles.dropincc.impl.runtime.impl.RunningDfaState;

/**
 * Parser code generation result.
 * 
 * @author pf-miles
 * 
 */
public class ParserCodeGenResult {
    /*
     * generated parser code
     */
    private String parserCode;
    /*
     * generated parser class field's name to tokenType mapping
     */
    private Map<String, TokenType> fieldTokenTypeMapping = new HashMap<String, TokenType>();

    /*
     * generated parser class field's name to alts action mapping
     */
    private Map<String, Object> fieldAltsActionMapping = new HashMap<String, Object>();

    /*
     * generated parser class field's name to semantic predicate mapping
     */
    private Map<String, Predicate<?>> fieldPredsMapping = new HashMap<String, Predicate<?>>();

    /*
     * generated parser class field's name to rule dfa mapping
     */
    private Map<String, RunningDfaState> fieldRuleDfaMapping = new HashMap<String, RunningDfaState>();

    /*
     * generated parser class field's name to kleene nodes' look ahead dfa
     * mapping
     */
    private Map<String, RunningDfaState> fieldKleeneDfaMapping = new HashMap<String, RunningDfaState>();

    public ParserCodeGenResult(String code, CodeGenContext ctx) {
        this.parserCode = code;
        this.fieldAltsActionMapping = ctx.fieldAltsActionMapping;
        this.fieldKleeneDfaMapping = ctx.fieldKleeneDfaMapping;
        this.fieldPredsMapping = ctx.fieldPredsMapping;
        this.fieldRuleDfaMapping = ctx.fieldRuleDfaMapping;
        this.fieldTokenTypeMapping = ctx.fieldTokenTypeMapping;
    }

    public String getCode() {
        return parserCode;
    }

    public Map<String, TokenType> getFieldTokenTypeMapping() {
        return fieldTokenTypeMapping;
    }

    public Map<String, Object> getFieldAltsActionMapping() {
        return fieldAltsActionMapping;
    }

    public Map<String, Predicate<?>> getFieldPredsMapping() {
        return fieldPredsMapping;
    }

    public Map<String, RunningDfaState> getFieldRuleDfaMapping() {
        return fieldRuleDfaMapping;
    }

    public Map<String, RunningDfaState> getFieldKleeneDfaMapping() {
        return fieldKleeneDfaMapping;
    }

}

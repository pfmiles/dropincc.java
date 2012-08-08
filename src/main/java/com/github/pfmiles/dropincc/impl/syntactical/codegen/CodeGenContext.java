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
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.github.pfmiles.dropincc.Predicate;
import com.github.pfmiles.dropincc.impl.EleType;
import com.github.pfmiles.dropincc.impl.GruleType;
import com.github.pfmiles.dropincc.impl.TokenType;
import com.github.pfmiles.dropincc.impl.kleene.KleeneType;
import com.github.pfmiles.dropincc.impl.runtime.impl.RunningDfaState;
import com.github.pfmiles.dropincc.impl.util.SeqGen;

/**
 * Context during parser code generation. Put anything needed here.
 * 
 * @author pf-miles
 * 
 */
public class CodeGenContext {

    public CodeGenContext(Map<KleeneType, List<EleType>> kleeneTypeToNode, Set<KleeneType> backtrackKleenes) {
        this.kleeneTypeToNode = kleeneTypeToNode;
        this.backtrackKleenes = backtrackKleenes;
    }

    // TODO could be removed by make token types static
    /**
     * generated parser class field's name to tokenType mapping
     */
    public Map<String, TokenType> fieldTokenTypeMapping = new HashMap<String, TokenType>();

    /**
     * generated parser class field's name to alts action mapping
     */
    public Map<String, Object> fieldAltsActionMapping = new HashMap<String, Object>();

    /**
     * generated parser class field's name to semantic predicate mapping
     */
    public Map<String, Predicate> fieldPredsMapping = new HashMap<String, Predicate>();

    /**
     * generated parser class field's name to rule dfa mapping
     */
    public Map<String, RunningDfaState> fieldRuleDfaMapping = new HashMap<String, RunningDfaState>();

    /**
     * generated parser class field's name to kleene nodes' look ahead dfa
     * mapping
     */
    public Map<String, RunningDfaState> fieldKleeneDfaMapping = new HashMap<String, RunningDfaState>();

    /**
     * variable sequence generator for method local variables
     */
    public SeqGen varSeq;

    /**
     * current grule type which is generating code for
     */
    public GruleType curGrule;

    /**
     * alts' action obj to generated parser class field's name mapping
     */
    public Map<Object, String> actionFieldMapping = new HashMap<Object, String>();

    /**
     * KleeneType to kleene node mapping
     */
    public Map<KleeneType, List<EleType>> kleeneTypeToNode;

    /**
     * Kleene nodes which needs to backtrack
     */
    public Set<KleeneType> backtrackKleenes;

}

package com.github.pfmiles.dropincc.impl.syntactical.codegen;

import java.util.HashMap;
import java.util.Map;

import com.github.pfmiles.dropincc.Predicate;
import com.github.pfmiles.dropincc.impl.TokenType;
import com.github.pfmiles.dropincc.impl.kleene.CKleeneNode;
import com.github.pfmiles.dropincc.impl.kleene.KleeneType;
import com.github.pfmiles.dropincc.impl.util.SeqGen;

/**
 * Context during parser code generation. Put anything needed here.
 * 
 * @author pf-miles
 * 
 */
public class CodeGenContext {

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
     * variable sequence generator for method local variables
     */
    public SeqGen varSeq;

    /**
     * alts' action obj to generated parser class field's name mapping
     */
    public Map<Object, String> actionFieldMapping = new HashMap<Object, String>();

    /**
     * KleeneType to kleene node mapping
     */
    public Map<KleeneType, CKleeneNode> kleeneTypeToNode;

}

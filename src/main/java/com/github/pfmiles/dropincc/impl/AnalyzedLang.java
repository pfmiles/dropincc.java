package com.github.pfmiles.dropincc.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import com.github.pfmiles.dropincc.CC;
import com.github.pfmiles.dropincc.Element;
import com.github.pfmiles.dropincc.Grule;
import com.github.pfmiles.dropincc.Token;
import com.github.pfmiles.dropincc.impl.kleene.AbstractKleeneNode;
import com.github.pfmiles.dropincc.impl.kleene.CKleeneNode;
import com.github.pfmiles.dropincc.impl.kleene.KleeneCompiler;
import com.github.pfmiles.dropincc.impl.kleene.KleeneType;
import com.github.pfmiles.dropincc.impl.lexical.LexerCompiler;
import com.github.pfmiles.dropincc.impl.syntactical.ParserCompiler;
import com.github.pfmiles.dropincc.impl.util.Pair;

/**
 * 
 * A analyzed language structure
 * 
 * @author pf-miles
 * 
 */
public class AnalyzedLang {
    private List<Token> tokens;
    private List<Grule> grules;
    private Map<Token, TokenType> tokenTypeMapping;
    // grule -> gruleType mapping, inited when AnalyzedLang obj creating,
    // completed after sub-rule rewriting
    private Map<Grule, GruleType> gruleTypeMapping;
    private static final Map<Element, SpecialType> specialTypeMapping = new HashMap<Element, SpecialType>();
    static {
        // special type 1, 'nothing' represents a empty alternative.
        specialTypeMapping.put(CC.NOTHING, new SpecialType(0));
    }
    // token group num -> token type
    private Map<Integer, EleType> groupNumToType;
    // the token mathcing pattern
    private Pattern tokenPatterns;

    private boolean whitespaceSensitive;

    // Grammar rule type -> alternatives with predicts, analysis & generated
    // from 'gruleTypeMapping' after sub-rule rewriting
    private Map<GruleType, List<CAlternative>> ruleTypeToAlts;

    // kleeneNode -> kleeneNodeType mapping, inited after building
    // 'gruleTypeMapping' immidiatelly
    private Map<AbstractKleeneNode, KleeneType> kleeneTypeMapping;

    // kleene node Type -> 'compiled' kleene node mapping, built while
    // 'AnalyzedLang' compiling(resolveParserAst). For later analysis & code gen
    private Map<KleeneType, CKleeneNode> kleeneTypeToNode;

    public AnalyzedLang(List<Token> tokens, List<Grule> grules, boolean whitespaceSensitive) {
        // build token -> tokenType mapping
        this.tokens = tokens;

        this.whitespaceSensitive = whitespaceSensitive;

        this.tokenTypeMapping = LexerCompiler.buildTokenTypeMapping(tokens, whitespaceSensitive);

        this.grules = grules;
        // rewrite sub-rules
        List<Grule> genGrules = ParserCompiler.rewriteSubRules(this.grules);
        // build grule -> gruleType mapping for all grules(including generated
        // ones) in 'gruleTypeMapping'
        this.gruleTypeMapping = ParserCompiler.buildGruleTypeMapping(this.grules, genGrules);

        // traverse and register kleene nodes
        this.kleeneTypeMapping = KleeneCompiler.buildKleeneTypeMapping(this.gruleTypeMapping);
    }

    public void compile() {
        // 1.check & compile token rules
        Pair<Map<Integer, EleType>, Pattern> compiledTokenUnit = LexerCompiler.checkAndCompileTokenRules(this.tokens, this.tokenTypeMapping);
        this.groupNumToType = compiledTokenUnit.getLeft();
        this.tokenPatterns = compiledTokenUnit.getRight();

        // 2.resolving the parser ast
        TypeMappingParam typeMappingParam = new TypeMappingParam(this.tokenTypeMapping, this.gruleTypeMapping, specialTypeMapping, this.kleeneTypeMapping);
        // at this point, 'gruleTypeMapping' contains all grule -> type
        // mappings, including generated grules
        this.ruleTypeToAlts = ParserCompiler.buildRuleTypeToAlts(typeMappingParam);

        // XXX need identical genGrules merging?

        // at this time, 'kleeneTypeMapping' should contain all KleeneNode ->
        // KleeneType mapping (built when traverse and register kleene nodes)
        this.kleeneTypeToNode = KleeneCompiler.buildKleeneTypeToNode(typeMappingParam);

        // 3.check or simplify & compute grammar rules
        // detect and report left-recursion, LL parsing needed
        ParserCompiler.checkAndReportLeftRecursions(this.ruleTypeToAlts, this.kleeneTypeToNode);
        // TODO compute predicts, LL(*)
        // TODO detect and report rule conflicts
        // 4.parser code gen
        // TODO kleene match should return a 'retry-able' result, and kleene
        // match should handle try-rollback logic
        // 5.compile and maintain the code in a separate classloader
    }

    public Map<Token, TokenType> getTokenTypeMapping() {
        return tokenTypeMapping;
    }

    public Map<Grule, GruleType> getGruleTypeMapping() {
        return gruleTypeMapping;
    }

    public Map<KleeneType, CKleeneNode> getKleeneTypeToNode() {
        return kleeneTypeToNode;
    }

    public Map<Integer, EleType> getGroupNumToType() {
        return groupNumToType;
    }

    public Map<GruleType, List<CAlternative>> getRuleTypeToAlts() {
        return ruleTypeToAlts;
    }

    public Pattern getTokenPatterns() {
        return tokenPatterns;
    }
}

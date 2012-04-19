package com.github.pfmiles.dropincc.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import com.github.pfmiles.dropincc.CC;
import com.github.pfmiles.dropincc.DropinccException;
import com.github.pfmiles.dropincc.Element;
import com.github.pfmiles.dropincc.Grule;
import com.github.pfmiles.dropincc.Token;
import com.github.pfmiles.dropincc.Tokens;
import com.github.pfmiles.dropincc.impl.kleene.AbstractKleeneNode;
import com.github.pfmiles.dropincc.impl.kleene.CKleeneNode;
import com.github.pfmiles.dropincc.impl.kleene.KleeneCrossNode;
import com.github.pfmiles.dropincc.impl.kleene.KleeneCrossType;
import com.github.pfmiles.dropincc.impl.kleene.KleeneStarNode;
import com.github.pfmiles.dropincc.impl.kleene.KleeneStarType;
import com.github.pfmiles.dropincc.impl.kleene.KleeneType;
import com.github.pfmiles.dropincc.impl.kleene.OptionalNode;
import com.github.pfmiles.dropincc.impl.kleene.OptionalType;
import com.github.pfmiles.dropincc.impl.util.Util;

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
    // generated grules while subRule rewriting
    private List<Grule> genGrules = new ArrayList<Grule>();
    // memorize grule examined in the rewriting process
    private Set<Grule> examinedGrules = new HashSet<Grule>();
    private Map<Token, TokenType> tokenTypeMapping = new HashMap<Token, TokenType>();
    // grule -> gruleType mapping, inited when AnalyzedLang obj creating,
    // completed after sub-rule rewriting
    private Map<Grule, GruleType> gruleTypeMapping = new HashMap<Grule, GruleType>();
    private static Map<Element, SpecialType> specialTypeMapping = new HashMap<Element, SpecialType>();
    {
        // EOF is of token type -1
        tokenTypeMapping.put(Tokens.EOF, new TokenType(-1));
    }
    static {
        // special type 1, 'nothing' represents a empty alternative.
        specialTypeMapping.put(CC.NOTHING, new SpecialType(0));
    }
    // token group num -> token type
    private Map<Integer, EleType> groupNumToType = new HashMap<Integer, EleType>();
    // the token mathcing pattern
    private Pattern tokenPatterns;

    // Grammar rule type -> alternatives with predicts, analysis & generated
    // from 'gruleTypeMapping' after sub-rule rewriting
    private Map<GruleType, List<CAlternative>> ruleTypeToAlts = new HashMap<GruleType, List<CAlternative>>();

    // kleeneNode -> kleeneNodeType mapping, inited after building
    // 'gruleTypeMapping' immidiatelly
    private Map<AbstractKleeneNode, KleeneType> kleeneTypeMapping = new HashMap<AbstractKleeneNode, KleeneType>();
    // count kleene nodes while traverse and register kleene nodes, help to
    // build kleene node types
    private int kleeneCount = 0;

    // kleene node Type -> 'compiled' kleene node mapping, built while
    // 'AnalyzedLang' compiling(resolveParserAst). For later analysis & code gen
    private Map<KleeneType, CKleeneNode> kleeneTypeToNode = new HashMap<KleeneType, CKleeneNode>();

    public AnalyzedLang(List<Token> tokens, List<Grule> grules) {
        // build token -> tokenType mapping
        this.tokens = tokens;
        if (tokens != null) {
            for (int i = 0; i < this.tokens.size(); i++) {
                this.tokenTypeMapping.put(tokens.get(i), new TokenType(i));
            }
        }
        this.grules = grules;
        // rewrite sub-rules, build grule -> gruleType mapping for all
        // grules(including generated ones) in 'gruleTypeMapping'
        if (grules != null && !grules.isEmpty()) {
            rewriteSubRules(this.grules);
            for (int i = 0; i < grules.size(); i++) {
                this.gruleTypeMapping.put(grules.get(i), new GruleType(i));
            }
            if (genGrules.size() != 0) {
                int base = this.grules.size();
                for (int i = 0; i < this.genGrules.size(); i++) {
                    this.gruleTypeMapping.put(this.genGrules.get(i), new GruleType(base + i));
                }
            }
        } else {
            throw new DropinccException("No grammar rules defined, error!");
        }
        // traverse and register kleene nodes
        for (Map.Entry<Grule, GruleType> entry : this.gruleTypeMapping.entrySet()) {
            registerKleenes(entry.getKey());
        }
    }

    private void registerKleenes(Grule grule) {
        for (Alternative alt : grule.getAlts()) {
            registerKleenesInElements(alt.getElements());
        }
    }

    private void registerKleenesInElements(List<Element> elements) {
        for (Element e : elements) {
            Class<?> eleCls = e.getClass();
            if (Grule.class.isAssignableFrom(eleCls)) {
                for (Alternative alt : ((Grule) e).getAlts()) {
                    registerKleenesInElements(alt.getElements());
                }
            } else if (AbstractKleeneNode.class.isAssignableFrom(eleCls)) {
                if (!this.kleeneTypeMapping.containsKey(e)) {
                    this.kleeneTypeMapping.put((AbstractKleeneNode) e,
                            resolveKleeneType((AbstractKleeneNode) e));
                    registerKleenesInElements(((AbstractKleeneNode) e).getElements());
                }
            } else if (Token.class.isAssignableFrom(eleCls)) {
                return;
            } else if (CC.NOTHING.equals(e)) {
                return;
            } else {
                throw new DropinccException("Unexpected element encountered when register kleene node: " + e);
            }
        }
    }

    private KleeneType resolveKleeneType(AbstractKleeneNode e) {
        Class<?> eleCls = e.getClass();
        KleeneType ret = null;
        if (KleeneCrossNode.class.isAssignableFrom(eleCls)) {
            ret = new KleeneCrossType(kleeneCount);
        } else if (KleeneStarNode.class.isAssignableFrom(eleCls)) {
            ret = new KleeneStarType(kleeneCount);
        } else if (OptionalNode.class.isAssignableFrom(eleCls)) {
            ret = new OptionalType(kleeneCount);
        } else {
            throw new DropinccException("Unhandled kleene node: " + e);
        }
        kleeneCount++;
        return ret;
    }

    // rewrite possiblely AndSubRules or OrSubRules, left to right post-order
    // traverse, recursively, stop when all grules are in 'examinedGrules'
    private void rewriteSubRules(List<Grule> grules) {
        for (Grule g : grules) {
            this.examinedGrules.add(g);
            rewriteAlts(g.getAlts());
        }
    }

    // examine and rewrite grules, check if the grule is already examined.
    private void examineAndRewriteGrule(Grule g) {
        if (!this.examinedGrules.contains(g)) {
            this.examinedGrules.add(g);
            this.rewriteAlts(g.getAlts());
        }
    }

    // rewrite subRules, in order not to pollute the Element.java interface,
    // it's implemented here.
    private void rewriteAlts(List<Alternative> alts) {
        for (Alternative a : alts) {
            rewriteElements(a.getElements());
        }
    }

    private void rewriteElements(List<Element> eles) {
        ListIterator<Element> iter = eles.listIterator();
        while (iter.hasNext()) {
            Element e = iter.next();
            Class<?> eleCls = e.getClass();
            // andSubRule or orSubRule should be rewritten currently
            if (AndSubRule.class.isAssignableFrom(eleCls)) {
                iter.remove();
                AndSubRule asr = (AndSubRule) e;
                Grule genGrule = GruleCreator.createGrule();
                List<Alternative> asrAlts = asr.getAlts();
                genGrule.setAlts(asrAlts);
                iter.add(genGrule);
                this.genGrules.add(genGrule);
                rewriteAlts(asrAlts);
            } else if (OrSubRule.class.isAssignableFrom(eleCls)) {
                iter.remove();
                OrSubRule osr = (OrSubRule) e;
                Grule genGrule = GruleCreator.createGrule();
                List<Alternative> osrAlts = osr.getAlts();
                genGrule.setAlts(osrAlts);
                iter.add(genGrule);
                this.genGrules.add(genGrule);
                rewriteAlts(osrAlts);
            } else if (ConstructingGrule.class.isAssignableFrom(eleCls)) {
                throw new DropinccException(
                        "Something must be wrong, ConstructingGrule shouldn't appear here");
            } else if (Grule.class.isAssignableFrom(eleCls)) {
                examineAndRewriteGrule((Grule) e);
            } else if (Token.class.isAssignableFrom(eleCls)) {
                continue;
            } else if (CC.NOTHING.equals(e)) {
                continue;
            } else if (AbstractKleeneNode.class.isAssignableFrom(eleCls)) {
                this.rewriteElements(((AbstractKleeneNode) e).getElements());
            } else {
                throw new DropinccException("Unhandled element: " + e);
            }
        }
    }

    private void checkAndCompileTokenRules() {
        // check regex valid
        checkRegexps(tokens);
        combineAndCompileRules(tokens);
    }

    // combine all token rules into one for matching these tokens using 'group
    // capturing', also returns each rule's corresponding group number TODO
    // white-space sensitive
    private Pattern combineAndCompileRules(List<Token> tokens) {
        StringBuilder sb = new StringBuilder();
        int groupCount = 1;// group num starts at 1
        for (Token t : tokens) {
            if (sb.length() != 0)
                sb.append("|");
            sb.append("(");

            String regExp = t.getRegexp();
            sb.append(regExp);
            this.groupNumToType.put(groupCount, this.tokenTypeMapping.get(t));

            sb.append(")");
            groupCount++;
            groupCount += countInnerGroups(regExp);// skip groups in the pattern
        }
        return Pattern.compile(sb.toString());
    }

    // count groups inside the sub regExp
    private int countInnerGroups(String regExp) {
        int ret = 0;
        for (int i = 0; i < regExp.length(); i++) {
            if ('(' == regExp.charAt(i) && (i - 1 < 0 || '\\' != regExp.charAt(i - 1)))
                ret++;
        }
        return ret;
    }

    private void checkRegexps(List<Token> tokens) {
        for (Token t : tokens) {
            if (Util.isEmpty(t.getRegexp()))
                throw new DropinccException("Cannot create null token.");
            try {
                Pattern.compile(t.getRegexp());
            } catch (PatternSyntaxException e) {
                throw new DropinccException("Invalid token rule: '" + t.getRegexp() + "'", e);
            }
        }
    }

    // resolve the grammar itself's ast, for later analysis & code gen
    private void resolveParserAst() {
        // at this point, 'gruleTypeMapping' contains all grule -> type
        // mappings, including generated grules
        for (Map.Entry<Grule, GruleType> entry : this.gruleTypeMapping.entrySet()) {
            this.ruleTypeToAlts.put(entry.getValue(), resolveCAlts(entry.getKey().getAlts()));
        }
        // at this time, 'kleeneTypeMapping' should contain all KleeneNode ->
        // KleeneType mapping (built when traverse and register kleene nodes)
        for (Map.Entry<AbstractKleeneNode, KleeneType> entry : this.kleeneTypeMapping.entrySet()) {
            this.kleeneTypeToNode.put(entry.getValue(), new CKleeneNode(resolveCKleeneNode(entry.getKey())));
        }
    }

    private List<EleType> resolveCKleeneNode(AbstractKleeneNode node) {
        List<EleType> ret = new ArrayList<EleType>();
        if (node == null || node.getElements() == null || node.getElements().isEmpty()) {
            throw new DropinccException("Cannot create empty kleene node: " + node);
        }
        for (Element e : node.getElements()) {
            ret.add(resolveEleType(e));
        }
        return ret;
    }

    private List<CAlternative> resolveCAlts(List<Alternative> alts) {
        List<CAlternative> ret = new ArrayList<CAlternative>();
        for (Alternative a : alts) {
            List<EleType> ms = this.eleListToTypeList(a.getElements());
            ret.add(new CAlternative(ms, a.getAction()));
        }
        return ret;
    }

    private List<EleType> eleListToTypeList(List<Element> eles) {
        List<EleType> ret = new ArrayList<EleType>();
        for (Element e : eles) {
            EleType t = resolveEleType(e);
            if (t == null)
                throw new DropinccException("Could not resolve element type for element: " + e
                        + ", is this element defined in a proper manner?");
            ret.add(t);
        }
        return ret;
    }

    // in order not to polluting Element.java's API, the resolveEleType method
    // defined here using if-else to determine which type should be returned.
    public EleType resolveEleType(Element e) {
        Class<?> eleCls = e.getClass();
        if (AndSubRule.class.isAssignableFrom(eleCls)) {
            throw new DropinccException(
                    "AndSubRule shouldn't exist when resolving element types, it should be already rewrited in prior steps.");
        } else if (ConstructingGrule.class.isAssignableFrom(eleCls)) {
            throw new DropinccException(
                    "There must be something wrong, ConstructingGrule shouldn't appear here.");
        } else if (Grule.class.isAssignableFrom(eleCls)) {
            return this.gruleTypeMapping.get(e);
        } else if (OrSubRule.class.isAssignableFrom(eleCls)) {
            throw new DropinccException(
                    "OrSubRule shouldn't exist when resolving element types, it should be already rewrited in prior steps.");
        } else if (Token.class.isAssignableFrom(eleCls)) {
            return this.tokenTypeMapping.get(e);
        } else if (e.equals(CC.NOTHING)) {
            return specialTypeMapping.get(e);
        } else if (AbstractKleeneNode.class.isAssignableFrom(eleCls)) {
            return this.kleeneTypeMapping.get(e);
        } else {
            throw new DropinccException("Unhandled element: " + e);
        }
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

    public void compile() {
        // 1.check & compile token rules
        checkAndCompileTokenRules();
        // 2.resolving the parser ast
        resolveParserAst();
        // 3.check & simplify & compute grammar rules TODO
        // 4.parser code gen
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

}

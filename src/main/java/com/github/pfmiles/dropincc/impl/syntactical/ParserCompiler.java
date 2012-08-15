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
package com.github.pfmiles.dropincc.impl.syntactical;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

import com.github.pfmiles.dropincc.CC;
import com.github.pfmiles.dropincc.DropinccException;
import com.github.pfmiles.dropincc.Element;
import com.github.pfmiles.dropincc.Grule;
import com.github.pfmiles.dropincc.TokenDef;
import com.github.pfmiles.dropincc.impl.Alternative;
import com.github.pfmiles.dropincc.impl.AndSubRule;
import com.github.pfmiles.dropincc.impl.CAlternative;
import com.github.pfmiles.dropincc.impl.ConstructingGrule;
import com.github.pfmiles.dropincc.impl.EleType;
import com.github.pfmiles.dropincc.impl.GruleType;
import com.github.pfmiles.dropincc.impl.OrSubRule;
import com.github.pfmiles.dropincc.impl.SpecialType;
import com.github.pfmiles.dropincc.impl.TokenType;
import com.github.pfmiles.dropincc.impl.TypeMappingParam;
import com.github.pfmiles.dropincc.impl.kleene.AbstractKleeneNode;
import com.github.pfmiles.dropincc.impl.kleene.KleeneStarType;
import com.github.pfmiles.dropincc.impl.kleene.KleeneType;
import com.github.pfmiles.dropincc.impl.kleene.OptionalType;
import com.github.pfmiles.dropincc.impl.llstar.GenedKleeneGruleType;
import com.github.pfmiles.dropincc.impl.llstar.LlstarAnalysis;
import com.github.pfmiles.dropincc.impl.llstar.PredictingGrule;
import com.github.pfmiles.dropincc.impl.llstar.PredictingKleene;
import com.github.pfmiles.dropincc.impl.syntactical.codegen.AltsActionsGen;
import com.github.pfmiles.dropincc.impl.syntactical.codegen.CodeGenContext;
import com.github.pfmiles.dropincc.impl.syntactical.codegen.KleeneDfasGen;
import com.github.pfmiles.dropincc.impl.syntactical.codegen.ParserClsGen;
import com.github.pfmiles.dropincc.impl.syntactical.codegen.ParserCodeGenResult;
import com.github.pfmiles.dropincc.impl.syntactical.codegen.PredsGen;
import com.github.pfmiles.dropincc.impl.syntactical.codegen.RuleDfasGen;
import com.github.pfmiles.dropincc.impl.syntactical.codegen.RuleMethodsGen;
import com.github.pfmiles.dropincc.impl.syntactical.codegen.TokenTypesGen;
import com.github.pfmiles.dropincc.impl.util.Pair;
import com.github.pfmiles.dropincc.impl.util.SeqGen;
import com.github.pfmiles.dropincc.impl.util.SetStack;
import com.github.pfmiles.dropincc.impl.util.Util;

/**
 * Util, to analysis & check, manipulate Grammar rules.
 * 
 * @author pf-miles
 * 
 */
public class ParserCompiler {

    /**
     * rewrite sub rules, return generated grules during rewriting.
     * 
     * @param grules
     * @return generated grules while rewrite
     */
    public static List<Grule> rewriteSubRules(List<Grule> grules) {
        if (grules != null && !grules.isEmpty()) {
            List<Grule> genGrules = new ArrayList<Grule>();
            Set<Grule> examinedGrules = new HashSet<Grule>();
            for (Grule g : grules)
                rewriteSubRulesForGrule(g, examinedGrules, genGrules);
            return genGrules;
        } else {
            throw new DropinccException("No grammar rules defined, error!");
        }
    }

    private static void rewriteSubRulesForGrule(Grule g, Set<Grule> examinedGrules, List<Grule> genGrules) {
        if (examinedGrules.contains(g))
            return;
        examinedGrules.add(g);
        for (Alternative alt : g.getAlts()) {
            rewriteSubRuleForElements(alt.getElements(), examinedGrules, genGrules);
        }
    }

    private static void rewriteSubRuleForElements(List<Element> eles, Set<Grule> examinedGrules, List<Grule> genGrules) {
        ListIterator<Element> iter = eles.listIterator();
        while (iter.hasNext()) {
            Element e = iter.next();
            Class<?> eleCls = e.getClass();
            // andSubRule or orSubRule should be rewritten currently
            if (AndSubRule.class.isAssignableFrom(eleCls)) {
                iter.remove();
                AndSubRule asr = (AndSubRule) e;
                Grule genGrule = new GenedGrule();
                List<Alternative> asrAlts = asr.getAlts();
                genGrule.setAlts(asrAlts);
                iter.add(genGrule);
                genGrules.add(genGrule);
                for (Alternative alt : asrAlts)
                    rewriteSubRuleForElements(alt.getElements(), examinedGrules, genGrules);
            } else if (OrSubRule.class.isAssignableFrom(eleCls)) {
                iter.remove();
                OrSubRule osr = (OrSubRule) e;
                Grule genGrule = new GenedGrule();
                List<Alternative> osrAlts = osr.getAlts();
                genGrule.setAlts(osrAlts);
                iter.add(genGrule);
                genGrules.add(genGrule);
                for (Alternative alt : osrAlts)
                    rewriteSubRuleForElements(alt.getElements(), examinedGrules, genGrules);
            } else if (ConstructingGrule.class.isAssignableFrom(eleCls)) {
                throw new DropinccException("Something must be wrong, ConstructingGrule shouldn't appear here");
            } else if (Grule.class.isAssignableFrom(eleCls)) {
                rewriteSubRulesForGrule((Grule) e, examinedGrules, genGrules);
            } else if (TokenDef.class.isAssignableFrom(eleCls)) {
                continue;
            } else if (CC.NOTHING.equals(e)) {
                continue;
            } else if (AbstractKleeneNode.class.isAssignableFrom(eleCls)) {
                rewriteSubRuleForElements(((AbstractKleeneNode) e).getElements(), examinedGrules, genGrules);
            } else {
                throw new DropinccException("Unhandled element: " + e);
            }
        }
    }

    public static Map<Grule, GruleType> buildGruleTypeMapping(List<Grule> grules, List<Grule> genGrules) {
        Map<Grule, GruleType> gruleTypeMapping = new LinkedHashMap<Grule, GruleType>();
        if (grules != null && !grules.isEmpty()) {
            for (int i = 0; i < grules.size(); i++) {
                gruleTypeMapping.put(grules.get(i), new GruleType(i));
            }
            if (genGrules.size() != 0) {
                int base = grules.size();
                for (int i = 0; i < genGrules.size(); i++) {
                    gruleTypeMapping.put(genGrules.get(i), new GenedGruleType(base + i));
                }
            }
        } else {
            throw new DropinccException("No grammar rules defined, error!");
        }
        return gruleTypeMapping;
    }

    public static Map<GruleType, List<CAlternative>> buildRuleTypeToAlts(TypeMappingParam param) {
        Map<GruleType, List<CAlternative>> ruleTypeToAlts = new HashMap<GruleType, List<CAlternative>>();
        for (Map.Entry<Grule, GruleType> entry : param.getGruleTypeMapping().entrySet()) {
            ruleTypeToAlts.put(entry.getValue(), resolveCAlts(entry.getKey().getAlts(), param));
        }
        return ruleTypeToAlts;
    }

    private static List<CAlternative> resolveCAlts(List<Alternative> alts, TypeMappingParam param) {
        List<CAlternative> ret = new ArrayList<CAlternative>();
        for (Alternative a : alts) {
            List<EleType> ms = eleListToTypeList(a.getElements(), param);
            ret.add(new CAlternative(ms, a.getAction(), a.getPred()));
        }
        return ret;
    }

    private static List<EleType> eleListToTypeList(List<Element> eles, TypeMappingParam param) {
        List<EleType> ret = new ArrayList<EleType>();
        for (Element e : eles) {
            EleType t = Util.resolveEleType(e, param);
            if (t == null)
                throw new DropinccException("Could not resolve element type for element: " + e + ", is this element defined in a proper manner?");
            ret.add(t);
        }
        return ret;
    }

    /**
     * Detect left-recursions, which is not allowed in LL parsing
     * 
     */
    public static void checkAndReportLeftRecursions(Map<GruleType, List<CAlternative>> ruleTypeToAlts, Map<KleeneType, List<EleType>> kleeneTypeToNode) {
        SetStack<GruleType> path = new SetStack<GruleType>();
        for (Map.Entry<GruleType, List<CAlternative>> e : ruleTypeToAlts.entrySet())
            examineEleType(e.getKey(), path, ruleTypeToAlts, kleeneTypeToNode);
    }

    private static void examineEleType(EleType t, SetStack<GruleType> path, Map<GruleType, List<CAlternative>> ruleTypeToAlts,
            Map<KleeneType, List<EleType>> kleeneTypeToNode) {
        if (t instanceof SpecialType) {
            return;
        } else if (t instanceof TokenType) {
            return;
        } else if (t instanceof GruleType) {
            if (path.contains(t)) {
                throw new DropinccException("Left recursion detected: " + Util.dumpCirclePath(path, t));
            } else {
                path.push((GruleType) t);
                List<CAlternative> alts = ruleTypeToAlts.get(t);
                for (CAlternative a : alts) {
                    for (EleType ele : a.getMatchSequence()) {
                        examineEleType(ele, path, ruleTypeToAlts, kleeneTypeToNode);
                        if (!(ele instanceof KleeneStarType || ele instanceof OptionalType))
                            break;
                    }
                }
                path.pop();
            }
        } else if (t instanceof KleeneType) {
            List<EleType> knode = kleeneTypeToNode.get(t);
            for (EleType ele : knode) {
                examineEleType(ele, path, ruleTypeToAlts, kleeneTypeToNode);
                if (!(ele instanceof KleeneStarType || ele instanceof OptionalType))
                    break;
            }
        } else {
            throw new DropinccException("Unhandled element type: " + t);
        }
    }

    /**
     * Compute lookAheads for grules and kleene nodes, prepare to generate
     * parser code
     * 
     * @param ruleTypeToAlts
     * @param kleeneTypeToNode
     * @return
     */
    public static PredictingResult computePredictingGrules(Map<GruleType, List<CAlternative>> ruleTypeToAlts, Map<KleeneType, List<EleType>> kleeneTypeToNode) {
        List<PredictingGrule> pgs = new ArrayList<PredictingGrule>();
        LlstarAnalysis a = new LlstarAnalysis(ruleTypeToAlts, kleeneTypeToNode);
        Set<GruleType> nonLLRegularGrules = a.getNonLLRegularGrules();
        Set<KleeneType> nonLLRegularKleenes = a.getNonLLRegularKleenes();
        // find all grule and kleene node on backtracking path
        Pair<Set<GruleType>, Set<KleeneType>> onBacktrackPath = findNodesOnBacktrackingPath(nonLLRegularGrules, nonLLRegularKleenes, ruleTypeToAlts, kleeneTypeToNode);
        Set<GruleType> gtypeOnBacktrackPath = onBacktrackPath.getLeft();
        Set<KleeneType> ktypeOnBacktrackPath = onBacktrackPath.getRight();
        for (Map.Entry<GruleType, List<CAlternative>> e : ruleTypeToAlts.entrySet()) {
            GruleType grule = e.getKey();
            if (nonLLRegularGrules.contains(grule)) {
                pgs.add(new PredictingGrule(grule, e.getValue(), gtypeOnBacktrackPath.contains(grule)));
            } else {
                pgs.add(new PredictingGrule(grule, a.getLookAheadDfa(grule), e.getValue(), gtypeOnBacktrackPath.contains(grule)));
            }
        }
        List<PredictingKleene> pks = new ArrayList<PredictingKleene>();
        for (KleeneType ktype : kleeneTypeToNode.keySet()) {
            if (nonLLRegularKleenes.contains(ktype)) {
                pks.add(new PredictingKleene(ktype, ktypeOnBacktrackPath.contains(ktype)));
            } else {
                pks.add(new PredictingKleene(ktype, a.getKleenDfaMapping().get(ktype), ktypeOnBacktrackPath.contains(ktype)));
            }
        }
        return new PredictingResult(pgs, pks, a.getDebugMsg(), a.getWarnings());
    }

    private static Pair<Set<GruleType>, Set<KleeneType>> findNodesOnBacktrackingPath(Set<GruleType> backtrackGrules, Set<KleeneType> backtrackKleenes,
            Map<GruleType, List<CAlternative>> ruleTypeToAlts, Map<KleeneType, List<EleType>> kleeneTypeToNode) {
        Set<GruleType> onPathGrules = new HashSet<GruleType>();
        Set<KleeneType> onPathKleenes = new HashSet<KleeneType>();
        Set<GruleType> examinedGrules = new HashSet<GruleType>();
        Set<KleeneType> examinedKleenes = new HashSet<KleeneType>();
        if (backtrackGrules != null)
            for (GruleType g : backtrackGrules) {
                if (!g.getClass().equals(GruleType.class))
                    // only concerns not-generated grules here
                    continue;
                examinedGrules.add(g);
                for (CAlternative alt : ruleTypeToAlts.get(g)) {
                    markBacktrackPathForElements(alt.getMatchSequence(), onPathGrules, onPathKleenes, ruleTypeToAlts, kleeneTypeToNode, examinedGrules, examinedKleenes);
                }
            }
        if (backtrackKleenes != null)
            for (KleeneType k : backtrackKleenes) {
                examinedKleenes.add(k);
                markBacktrackPathForElements(kleeneTypeToNode.get(k), onPathGrules, onPathKleenes, ruleTypeToAlts, kleeneTypeToNode, examinedGrules, examinedKleenes);
            }
        return new Pair<Set<GruleType>, Set<KleeneType>>(onPathGrules, onPathKleenes);
    }

    private static void markBacktrackPathForElements(List<EleType> matchSequence, Set<GruleType> onPathGrules, Set<KleeneType> onPathKleenes,
            Map<GruleType, List<CAlternative>> ruleTypeToAlts, Map<KleeneType, List<EleType>> kleeneTypeToNode, Set<GruleType> examinedGrules,
            Set<KleeneType> examinedKleenes) {
        for (EleType ele : matchSequence) {
            if (ele instanceof GruleType) {
                // add to on-path grule set and process children
                GruleType g = (GruleType) ele;
                if (examinedGrules.contains(g))
                    continue;
                onPathGrules.add(g);
                examinedGrules.add(g);
                for (CAlternative alt : ruleTypeToAlts.get(g)) {
                    markBacktrackPathForElements(alt.getMatchSequence(), onPathGrules, onPathKleenes, ruleTypeToAlts, kleeneTypeToNode, examinedGrules, examinedKleenes);
                }
            } else if (ele instanceof KleeneType) {
                // add to on-path kleene set and process children
                KleeneType k = (KleeneType) ele;
                if (examinedKleenes.contains(k))
                    continue;
                onPathKleenes.add(k);
                examinedKleenes.add(k);
                markBacktrackPathForElements(kleeneTypeToNode.get(k), onPathGrules, onPathKleenes, ruleTypeToAlts, kleeneTypeToNode, examinedGrules, examinedKleenes);
            }
        }
    }

    /**
     * Render parser code according to predicting grules
     * 
     * @param predGrules
     * @return
     */
    public static ParserCodeGenResult genParserCode(String parserName, GruleType startRule, List<PredictingGrule> predGrules, List<PredictingKleene> pks,
            Collection<TokenType> tokenTypes, Map<KleeneType, List<EleType>> kleeneTypeToNode) {
        TokenTypesGen tokenTypesGen = new TokenTypesGen(tokenTypes);
        // list([grule, altIndex, actionObj])
        List<Object[]> actionInfos = new ArrayList<Object[]>();
        // list([grule, altInfex, predObj])
        List<Object[]> predInfos = new ArrayList<Object[]>();
        for (PredictingGrule pg : predGrules) {
            GruleType grule = pg.getGruleType();
            for (int i = 0; i < pg.getAlts().size(); i++) {
                CAlternative calt = pg.getAlts().get(i);
                if (calt.getAction() != null) {
                    actionInfos.add(new Object[] { grule, i, calt.getAction() });
                }
                if (calt.getPredicate() != null) {
                    predInfos.add(new Object[] { grule, i, calt.getPredicate() });
                }
            }
        }
        AltsActionsGen actionsGen = new AltsActionsGen(actionInfos);
        PredsGen predsGen = new PredsGen(predInfos);
        RuleDfasGen ruleDfaGen = new RuleDfasGen(predGrules);
        KleeneDfasGen kleeneDfas = new KleeneDfasGen(pks);
        RuleMethodsGen ruleMethodsGen = new RuleMethodsGen(predGrules);
        ParserClsGen parserGen = new ParserClsGen(parserName, tokenTypesGen, actionsGen, predsGen, ruleDfaGen, kleeneDfas, startRule, ruleMethodsGen);

        CodeGenContext ctx = new CodeGenContext(kleeneTypeToNode, resolveBacktrackKleeneSet(pks));
        return new ParserCodeGenResult(parserGen.render(ctx), ctx);
    }

    private static Set<KleeneType> resolveBacktrackKleeneSet(List<PredictingKleene> pks) {
        Set<KleeneType> ret = new HashSet<KleeneType>();
        for (PredictingKleene pk : pks) {
            if (pk.isBacktrack())
                ret.add(pk.getKleeneType());
        }
        return ret;
    }

    /**
     * Generate analyzing grules for every kleene node
     * 
     * @param kleeneTypeToNode
     * @param base
     *            current grule count, as the base number for generated grule
     *            type's defIndexes
     * @return
     */
    public static Pair<Map<GruleType, List<CAlternative>>, Map<KleeneType, GenedKleeneGruleType>> genAnalyzingGrulesForKleenes(
            Map<KleeneType, List<EleType>> kleeneTypeToNode, int base) {
        Map<GruleType, List<CAlternative>> genedGrules = new HashMap<GruleType, List<CAlternative>>();
        Map<KleeneType, GenedKleeneGruleType> kleeneToGenedGrule = new HashMap<KleeneType, GenedKleeneGruleType>();
        SeqGen seq = new SeqGen(base);
        for (Map.Entry<KleeneType, List<EleType>> e : kleeneTypeToNode.entrySet()) {
            List<CAlternative> alts = new ArrayList<CAlternative>();
            alts.add(new CAlternative(e.getValue(), null, null));
            alts.add(new CAlternative(Collections.<EleType> emptyList(), null, null));
            GenedKleeneGruleType gt = new GenedKleeneGruleType(seq.next());
            genedGrules.put(gt, alts);
            kleeneToGenedGrule.put(e.getKey(), gt);
        }
        return new Pair<Map<GruleType, List<CAlternative>>, Map<KleeneType, GenedKleeneGruleType>>(genedGrules, kleeneToGenedGrule);
    }
}

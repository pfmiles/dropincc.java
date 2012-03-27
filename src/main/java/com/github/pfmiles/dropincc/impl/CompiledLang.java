/**
 * 
 */
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
import com.github.pfmiles.dropincc.impl.util.Util;

/**
 * 
 * A compiled language structure
 * 
 * @author pf-miles
 * 
 */
public class CompiledLang {
	private List<Token> tokens;
	private List<Grule> grules;
	private List<Grule> genGrules = new ArrayList<Grule>();
	// memorize grule examined in the rewriting process
	private Set<Grule> resolvedGrules = new HashSet<Grule>();
	private Map<Token, TokenType> tokenTypeMapping = new HashMap<Token, TokenType>();
	private Map<Grule, GruleType> gruleTypeMapping = new HashMap<Grule, GruleType>();
	private static Map<Element, SpecialType> specialTypeMapping = new HashMap<Element, SpecialType>();
	static {
		specialTypeMapping.put(CC.EOF, new SpecialType(-1));
		specialTypeMapping.put(CC.NOTHING, new SpecialType(-2));
	}
	// token group num -> token type
	private Map<Integer, EleType> groupNumToType = new HashMap<Integer, EleType>();
	// the token mathcing pattern
	private Pattern tokenPatterns;

	// Grammar rule type -> alternatives with predicts
	private Map<GruleType, List<CAlternative>> ruleTypeToAlts = new HashMap<GruleType, List<CAlternative>>();

	public CompiledLang(List<Token> tokens, List<Grule> grules) {
		this.tokens = tokens;
		if (tokens != null) {
			for (int i = 0; i < this.tokens.size(); i++) {
				this.tokenTypeMapping.put(tokens.get(i), new TokenType(i));
			}
		}
		this.grules = grules;
		if (grules != null) {
			rewriteSubRules(this.grules);
			for (int i = 0; i < grules.size(); i++) {
				this.gruleTypeMapping.put(grules.get(i), new GruleType(i));
			}
			if (genGrules.size() != 0) {
				int base = this.grules.size();
				for (int i = 0; i < this.genGrules.size(); i++) {
					this.gruleTypeMapping.put(this.genGrules.get(i),
							new GruleType(base + i));
				}
			}
		}
	}

	// rewrite possiblely AndSubRules or OrSubRules
	private void rewriteSubRules(List<Grule> grules) {
		for (Grule g : grules) {
			this.resolvedGrules.add(g);
			rewriteAlts(g.getAlts());
		}
	}

	// examine and rewrite grules, check if the grule is already examined.
	private void examineAndRewriteGrule(Grule g) {
		if (!this.resolvedGrules.contains(g)) {
			this.resolvedGrules.add(g);
			this.rewriteAlts(g.getAlts());
		}
	}

	// rewrite subRules, in order not to pollute the Element.java interface,
	// it's implemented here.
	private void rewriteAlts(List<Alternative> alts) {
		for (Alternative a : alts) {
			ListIterator<Element> iter = a.getElements().listIterator();
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
				} else if (CC.EOF.equals(e) || CC.NOTHING.equals(e)) {
					continue;
				} else {
					throw new DropinccException("Unhandled element: " + e);
				}
			}
		}
	}

	private void checkAndCompileTokenRules() {
		// check regex valid
		checkRegexps(tokens);
		combineAndCompileRules(tokens);
	}

	// combine all token rules into one for matching these tokens using 'group
	// capturing', also returns each rule's corresponding group number
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
			if ('(' == regExp.charAt(i)
					&& (i - 1 < 0 || '\\' != regExp.charAt(i - 1)))
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
				throw new DropinccException("Invalid token rule: '"
						+ t.getRegexp() + "'", e);
			}
		}
	}

	/*
	 * resolve the grammar itself's ast, for later analysis & code gen
	 * 
	 * @param grules
	 */
	private void resolveParserAst() {
		for (Grule g : grules) {
			this.ruleTypeToAlts.put(this.gruleTypeMapping.get(g),
					resolveCAlts(g.getAlts()));
		}
	}

	private List<CAlternative> resolveCAlts(List<Alternative> alts) {
		List<CAlternative> ret = new ArrayList<CAlternative>();
		for (Alternative a : alts) {
			List<EleType> ms = new ArrayList<EleType>();
			for (Element e : a.getElements()) {
				EleType t = resolveEleType(e);
				if (t == null)
					throw new DropinccException(
							"Could not resolve element type for element: "
									+ e
									+ ", is this element defined in a proper manner?");
				ms.add(t);
			}
			ret.add(new CAlternative(ms, a.getAction()));
		}
		return ret;
	}

	// in order not to polluting Element.java's API, the resolveEleType method
	// defined here using if-else to determine which type should be returned.
	private EleType resolveEleType(Element e) {
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
		} else if (e.equals(CC.EOF)) {
			return specialTypeMapping.get(e);
		} else if (e.equals(CC.NOTHING)) {
			return specialTypeMapping.get(e);
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

}

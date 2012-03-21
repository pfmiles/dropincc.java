/**
 * 
 */
package com.github.pfmiles.dropincc.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import com.github.pfmiles.dropincc.DropinccException;
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
	private List<Integer> tokenGroupNums;
	private Pattern tokenPatterns;

	public void checkAndCompileTokenRules(List<Token> tokens) {
		// check regex valid
		this.checkRegexps(tokens);
		combineAndCompileRules(tokens);
	}

	// combine all token rules into one for matching these tokens using 'group
	// capturing', also returns each rule's corresponding group number
	private void combineAndCompileRules(List<Token> tokens) {
		List<Integer> groupNums = new ArrayList<Integer>();
		StringBuilder sb = new StringBuilder();
		int groupCount = 1;// group num starts at 1
		for (Token t : tokens) {
			if (sb.length() != 0)
				sb.append("|");
			sb.append("(");

			String regExp = t.getRegexp();
			sb.append(regExp);
			groupNums.add(groupCount);

			sb.append(")");
			groupCount++;
			groupCount += countInnerGroups(regExp);// skip groups in the pattern
		}
		this.tokenPatterns = Pattern.compile(sb.toString());
		this.tokenGroupNums = groupNums;
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

	public List<Integer> getTokenGroupNums() {
		return tokenGroupNums;
	}

	public Pattern getTokenPatterns() {
		return tokenPatterns;
	}

}

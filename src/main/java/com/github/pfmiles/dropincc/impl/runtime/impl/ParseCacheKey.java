package com.github.pfmiles.dropincc.impl.runtime.impl;

/**
 * @author pf-miles
 * 
 */
public class ParseCacheKey {

    public int ruleNum;
    public int position;

    public ParseCacheKey(int ruleNum, int position) {
        this.ruleNum = ruleNum;
        this.position = position;
    }

    public int hashCode() {
        return 3 * this.ruleNum + 2 * this.position;
    }

    public boolean equals(Object obj) {
        ParseCacheKey o = (ParseCacheKey) obj;
        return o.ruleNum == this.ruleNum && o.position == this.position;
    }

}

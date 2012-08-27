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

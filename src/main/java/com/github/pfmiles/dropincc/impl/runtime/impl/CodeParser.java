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

import java.util.HashMap;
import java.util.Map;

import com.github.pfmiles.dropincc.DropinccException;
import com.github.pfmiles.dropincc.impl.TokenType;
import com.github.pfmiles.dropincc.impl.runtime.Token;
import com.github.pfmiles.dropincc.impl.util.Pair;

/**
 * The parer super class for non-stream (that means, input is finite) parsing.
 * 
 * @author pf-miles
 * 
 */
public abstract class CodeParser extends Parser {

    protected CodeLexer lexer;

    private Map<ParseCacheKey, Pair<DelayedAction, Integer>> parseCache = new HashMap<ParseCacheKey, Pair<DelayedAction, Integer>>();

    private int lastForwardPosition = -1;

    protected void putInCache(int ruleNum, DelayedAction node, int startPoint) {
        int forwardPosition = lexer.getCurrentPosition();
        this.parseCache.put(new ParseCacheKey(ruleNum, startPoint), new Pair<DelayedAction, Integer>(node, forwardPosition));
    }

    protected DelayedAction tryResolveFromCache(int ruleNum) {
        ParseCacheKey k = new ParseCacheKey(ruleNum, lexer.getCurrentPosition());
        if (parseCache.containsKey(k)) {
            Pair<DelayedAction, Integer> p = parseCache.get(k);
            this.lastForwardPosition = p.getRight();
            return p.getLeft();
        } else {
            return null;
        }
    }

    protected Object match(TokenType type) {
        Token ret = lexer.nextElement();
        if (!ret.getType().equals(type))
            throw new DropinccException("Unexpected token encountered: " + ret + ", expected type: " + type + ", at position: " + lexer.getCurrentPosition());
        return ret.getLexeme();
    }

    protected void fastForward() {
        lexer.fastForward(this.lastForwardPosition);
    }

    public void setLexer(Lexer lexer) {
        this.lexer = (CodeLexer) lexer;
    }

}

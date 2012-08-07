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

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Enumeration;
import java.util.List;

import com.github.pfmiles.dropincc.impl.TokenType;
import com.github.pfmiles.dropincc.impl.runtime.Token;
import com.github.pfmiles.dropincc.impl.util.Pair;

/**
 * Common super class for all generated lexer.
 * 
 * @author pf-miles
 * 
 */
public abstract class Lexer implements Enumeration<Token> {

    protected List<Token> lookAheadBuf = new ArrayList<Token>();

    // def index stack of grule, indicates which rule sets the save point, and
    // the corresponding backup start index
    private Deque<Pair<Integer, Integer>> savePoints = new ArrayDeque<Pair<Integer, Integer>>();

    // matched token backup when any save point is set
    private ArrayList<Token> backUp = new ArrayList<Token>();

    /**
     * check if this lexer has more token to get
     */
    public abstract boolean hasMoreElements();

    /**
     * get next token(may get from buffer)
     */
    public Token nextElement() {
        Token ret = null;
        if (!this.lookAheadBuf.isEmpty()) {
            ret = this.lookAheadBuf.remove(0);
        } else {
            ret = realNext();
        }
        if (!this.savePoints.isEmpty()) {
            // backtracking enabled
            this.backUp.add(ret);
        }
        return ret;
    }

    /**
     * the real get next token logic according to the underlying lexer
     * implementation.
     * 
     * @return
     */
    protected abstract Token realNext();

    /**
     * Return the current lexing position.
     * 
     * @return
     */
    public abstract int getCurrentPosition();

    /**
     * Return the next 2 tokens' string representation, used as context
     * infomation in reporting an error.
     * 
     * @return
     */
    public String getAheadTokensRepr() {
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i <= 3; i++) {
            if (sb.length() != 0)
                sb.append(", ");
            sb.append("'").append(this.LT(i).getLexeme()).append("'");
        }
        return sb.toString();
    }

    /**
     * look ahead token type
     * 
     * @param i
     *            lookahead count
     * @return
     */
    public TokenType LA(int i) {
        Token t = this.LT(i);
        return t != null ? t.getType() : null;
    }

    protected final Token LT(int i) {
        if (i < this.lookAheadBuf.size())
            return this.lookAheadBuf.get(i - 1);
        int lng = i - this.lookAheadBuf.size();
        for (int j = 0; j < lng; j++) {
            // real next return null if no more token
            Token t = this.realNext();
            if (t == null)
                return null;
            this.lookAheadBuf.add(t);
        }
        return this.lookAheadBuf.get(i - 1);
    }

    /**
     * Set a save point. Pushing the rule number to the top of the save point
     * stack. Save points could be nested.
     * 
     * @param ruleNum
     */
    public void setSavePoint(int ruleNum) {
        this.savePoints.push(new Pair<Integer, Integer>(ruleNum, this.backUp.size()));
    }

    /**
     * Release the save point at the top of the save point stack. If the rule
     * number of the stack top is not equal to the specified one, report error.
     * If the released save point the the last save point: 1) if not success,
     * push back the back-upped tokens into the token buffer. 2) if success,
     * clear both save point and back ups.
     * 
     * @param ruleNum
     * @param success
     *            if is a successful match
     */
    public void releaseSavePoint(int ruleNum, boolean success) {
        Pair<Integer, Integer> top = this.savePoints.pop();
        if (top.getLeft() != ruleNum)
            throw new RuntimeException("Fatal Error! Rule number doesn't match when releasing save point!");

        List<Token> backOfThisSavePoint = this.backUp.subList(top.getRight(), this.backUp.size());
        if (!success) {
            this.lookAheadBuf.addAll(backOfThisSavePoint);
        }
        backOfThisSavePoint.clear();
    }
}

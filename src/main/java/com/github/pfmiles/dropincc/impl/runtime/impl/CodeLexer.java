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
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.github.pfmiles.dropincc.DropinccException;
import com.github.pfmiles.dropincc.impl.TokenType;
import com.github.pfmiles.dropincc.impl.runtime.Token;

/**
 * Lexer class for code scanning. Each scanning process should create a new
 * CodeLexer Object to use.
 * 
 * @author pf-miles
 * 
 */
public class CodeLexer extends Lexer {

    // all regex group index -> tokenType mapping
    private Map<Integer, TokenType> groupNumToType;
    // scanning code
    private String code = null;
    private Matcher matcher = null;
    // current position in the input sequence
    private int currentRealPos = 0;
    private boolean eofReturned = false;
    // does the user care about white spaces?
    private boolean whiteSpaceSensitive;

    // [ruleNum, backUpPosition, currentParsingPosition]
    private Deque<Integer[]> savePoints = new ArrayDeque<Integer[]>();

    // matched token backup when any save point is set
    private ArrayList<Token> backUp = new ArrayList<Token>();

    public CodeLexer(Pattern pattern, Map<Integer, TokenType> groupNumToType, String code, boolean whiteSpaceSensitive) {
        this.groupNumToType = groupNumToType;
        this.code = code;
        this.matcher = pattern.matcher(code);
        this.whiteSpaceSensitive = whiteSpaceSensitive;
    }

    public boolean hasMoreElements() {
        if (!this.lookAheadBuf.isEmpty() || currentRealPos < code.length())
            return true;
        if (!this.eofReturned)
            return true;
        return false;
    }

    /**
     * get next token(may get from buffer)
     */
    public Token nextElement() {
        Token ret = null;
        if (!this.lookAheadBuf.isEmpty()) {
            ret = this.lookAheadBuf.remove(0);
            this.allBufferedLexemeLength -= ret.getLength();
        } else {
            ret = realNext();
        }
        if (ret == null)
            throw new DropinccException("No more token.");
        if (!this.savePoints.isEmpty()) {
            // backtracking enabled
            this.backUp.add(ret);
        }
        return ret;
    }

    /**
     * Real next implementation with whitespace sensitive option
     */
    protected Token realNext() {
        Token t = this._realNext();
        if (!this.whiteSpaceSensitive)
            while (t != null && t.getType().equals(TokenType.WHITESPACE))
                t = this._realNext();
        return t;
    }

    private Token _realNext() {
        if (currentRealPos < code.length()) {
            if (this.matcher.find(currentRealPos)) {
                // XXX find a more efficient named-capturing group
                // implementation here(planned to bootstrap the regex engine)
                for (Map.Entry<Integer, TokenType> e : this.groupNumToType.entrySet()) {
                    int gnum = e.getKey();
                    if (gnum != -1) {
                        String txt = this.matcher.group(gnum);
                        if (txt != null) {
                            int length = this.matcher.end() - this.matcher.start();
                            this.currentRealPos += length;
                            return new Token(e.getValue(), txt, length);
                        }
                    }
                }
                throw new DropinccException("No token matched at position: " + this.currentRealPos + ", subsequent char: '"
                        + this.code.charAt(currentRealPos) + "'");
            } else {
                throw new DropinccException("Unexpected char: '" + this.code.charAt(currentRealPos) + "' at position: " + this.currentRealPos);
            }
        } else if (!this.eofReturned) {
            this.eofReturned = true;
            return Token.EOF;
        } else {
            return null;
        }
    }

    public int getCurrentPosition() {
        return this.currentRealPos - this.allBufferedLexemeLength;
    }

    /**
     * Set a save point. Pushing the rule number to the top of the save point
     * stack. Save points could be nested.
     * 
     * @param ruleNum
     */
    public void setSavePoint(int ruleNum) {
        this.savePoints.push(new Integer[] { ruleNum, this.backUp.size(), this.getCurrentPosition() });
    }

    /**
     * Release the save point at the top of the save point stack. If the rule
     * number of the stack top is not equal to the specified one, report error.
     * If save point is released under a unsuccessful match, pushback the
     * buffer. If the released save point is the the last save point, clear the
     * backup.
     * 
     * @param ruleNum
     * @param success
     *            if is a successful match
     * @return the parsing position when backuped
     */
    public int releaseSavePoint(int ruleNum, boolean success) {
        Integer[] top = this.savePoints.pop();
        if (top[0] != ruleNum)
            throw new RuntimeException("Fatal Error! Rule number doesn't match when releasing save point!");
        if (!success) {
            List<Token> sub = this.backUp.subList(top[1], this.backUp.size());
            this.lookAheadBuf.addAll(0, sub);
            for (Token t : sub)
                this.allBufferedLexemeLength += t.getLength();
            sub.clear();
        } else if (this.savePoints.isEmpty() && !this.backUp.isEmpty()) {
            // it's the last save point with successful match, clean the backUp
            this.backUp.clear();
        }
        return top[2];
    }

    /**
     * Tells if is backtracking at parsing runtime.
     * 
     * @return
     */
    public boolean isBacktracking() {
        return !this.savePoints.isEmpty();
    }

    /**
     * Let the lexer forward to the specified position directly
     * 
     * @param position
     */
    public void fastForward(int position) {
        int chars = position - this.getCurrentPosition();
        if (chars < 0)
            throw new RuntimeException("Illegal fast forward operation, the target position is before the current position!");
        while (chars > 0)
            chars -= this.nextElement().getLength();
        if (chars < 0)
            throw new RuntimeException("Illegal fast forward operation, failed to forward to the proper position!");
    }
}

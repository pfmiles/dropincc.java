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

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import com.github.pfmiles.dropincc.impl.TokenType;
import com.github.pfmiles.dropincc.impl.runtime.Token;

/**
 * Common super class for all generated lexer.
 * 
 * @author pf-miles
 * 
 */
public abstract class Lexer implements Enumeration<Token> {

    // TODO test if LinkedList or ArrayDeque is better? or invent a new
    // implementation to suit for the lexer situation
    protected List<Token> lookAheadBuf = new ArrayList<Token>();

    protected int allBufferedLexemeLength = 0;

    /**
     * check if this lexer has more token to get
     */
    public abstract boolean hasMoreElements();

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
            if (this.LT(i) == null)
                break;
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
            this.allBufferedLexemeLength += t.getLength();
        }
        return this.lookAheadBuf.get(i - 1);
    }

}

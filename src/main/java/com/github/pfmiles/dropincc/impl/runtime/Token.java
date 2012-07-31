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
package com.github.pfmiles.dropincc.impl.runtime;

import com.github.pfmiles.dropincc.impl.TokenType;

/**
 * A token recognized.
 * 
 * @author pf-miles
 * 
 */
public class Token {
    /**
     * The EOF token predefined
     */
    public static final Token EOF = new Token(TokenType.EOF, "<<EOF>>");
    private TokenType type;
    private Object lexeme;

    // TODO filename, row, col numbers record?

    public Token(TokenType type, Object lexeme) {
        this.type = type;
        this.lexeme = lexeme;
    }

    public TokenType getType() {
        return type;
    }

    public void setType(TokenType type) {
        this.type = type;
    }

    public Object getLexeme() {
        return lexeme;
    }

    public void setLexeme(Object lexeme) {
        this.lexeme = lexeme;
    }

    public String toString() {
        return "(" + this.type + ", " + this.lexeme + ")";
    }

    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((lexeme == null) ? 0 : lexeme.hashCode());
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        return result;
    }

    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Token other = (Token) obj;
        if (lexeme == null) {
            if (other.lexeme != null)
                return false;
        } else if (!lexeme.equals(other.lexeme))
            return false;
        if (type == null) {
            if (other.type != null)
                return false;
        } else if (!type.equals(other.type))
            return false;
        return true;
    }

}

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
    private String lexeme;

    // TODO filename, row, col numbers record?

    public Token(TokenType type, String lexeme) {
        this.type = type;
        this.lexeme = lexeme;
    }

    public TokenType getType() {
        return type;
    }

    public void setType(TokenType type) {
        this.type = type;
    }

    public String getLexeme() {
        return lexeme;
    }

    public void setLexeme(String lexeme) {
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

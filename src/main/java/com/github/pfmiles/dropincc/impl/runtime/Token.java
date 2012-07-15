package com.github.pfmiles.dropincc.impl.runtime;

import com.github.pfmiles.dropincc.impl.TokenType;
import com.github.pfmiles.dropincc.impl.util.Util;

/**
 * A token recognized.
 * 
 * @author pf-miles
 * 
 */
public class Token {
    private TokenType type;
    private String lexeme;
    private int row;
    private int col;
    private String fileName;

    public Token(TokenType type, String lexeme, String fileName, int row, int col) {
        this.type = type;
        this.lexeme = lexeme;
        this.fileName = fileName;
        this.row = row;
        this.col = col;
    }

    public Token(TokenType type, String lexeme) {
        this(type, lexeme, null, 0, 0);
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

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Token(");
        sb.append("type: ").append(this.type).append(", lexeme: ").append(Util.showHiddenChars(this.lexeme));
        if (this.fileName != null) {
            sb.append(", file: " + this.fileName + ", row: " + this.row + ", col: " + this.col);
        }
        sb.append(")");
        return sb.toString();
    }

    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + col;
        result = prime * result + ((fileName == null) ? 0 : fileName.hashCode());
        result = prime * result + ((lexeme == null) ? 0 : lexeme.hashCode());
        result = prime * result + row;
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
        if (col != other.col)
            return false;
        if (fileName == null) {
            if (other.fileName != null)
                return false;
        } else if (!fileName.equals(other.fileName))
            return false;
        if (lexeme == null) {
            if (other.lexeme != null)
                return false;
        } else if (!lexeme.equals(other.lexeme))
            return false;
        if (row != other.row)
            return false;
        if (type == null) {
            if (other.type != null)
                return false;
        } else if (!type.equals(other.type))
            return false;
        return true;
    }

}

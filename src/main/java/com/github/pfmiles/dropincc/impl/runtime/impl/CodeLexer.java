package com.github.pfmiles.dropincc.impl.runtime.impl;

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
    private int currentPos = 0;
    private boolean eofReturned = false;
    // does the user care about white spaces?
    private boolean whiteSpaceSensitive;

    public CodeLexer(Pattern pattern, Map<Integer, TokenType> groupNumToType, String code, boolean whiteSpaceSensitive) {
        this.groupNumToType = groupNumToType;
        this.code = code;
        this.matcher = pattern.matcher(code);
        this.whiteSpaceSensitive = whiteSpaceSensitive;
    }

    public boolean hasMoreElements() {
        if (!this.lookAheadBuf.isEmpty() || currentPos < code.length())
            return true;
        if (!this.eofReturned)
            return true;
        return false;
    }

    public Token nextElement() {
        if (!this.lookAheadBuf.isEmpty()) {
            return this.lookAheadBuf.remove(0);
        } else {
            return realNextFiltered();
        }
    }

    private Token realNextFiltered() {
        Token t = this.realNext();
        if (!this.whiteSpaceSensitive)
            while (t != null && t.getType().equals(TokenType.WHITESPACE))
                t = this.realNext();
        return t;
    }

    protected Token realNext() {
        if (currentPos < code.length()) {
            if (this.matcher.find(currentPos)) {
                // XXX find a more efficient named-capturing group
                // implementation here(planned to bootstrap the regex engine)
                for (Map.Entry<Integer, TokenType> e : this.groupNumToType.entrySet()) {
                    int gnum = e.getKey();
                    if (gnum != -1) {
                        String txt = this.matcher.group(gnum);
                        if (txt != null) {
                            this.currentPos += this.matcher.end() - this.matcher.start();
                            return new Token(e.getValue(), txt);
                        }
                    }
                }
                throw new DropinccException("No token matched at position: " + this.currentPos + ", subsequent char: '" + this.code.charAt(currentPos) + "'");
            } else {
                throw new DropinccException("Unexpected char: '" + this.code.charAt(currentPos) + "' at position: " + this.currentPos);
            }
        } else if (!this.eofReturned) {
            this.eofReturned = true;
            return Token.EOF;
        } else {
            return null;
        }
    }

    public int getCurrentPosition() {
        return this.currentPos - this.lookAheadBuf.size();
    }

    public String getAheadTokensRepr() {
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i <= 3; i++) {
            if (sb.length() != 0)
                sb.append(", ");
            sb.append("'").append(this.LT(i).getLexeme()).append("'");
        }
        return sb.toString();
    }

}

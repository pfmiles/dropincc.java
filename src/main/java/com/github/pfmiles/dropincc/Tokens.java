package com.github.pfmiles.dropincc;

/**
 * Defined some commonly used tokens.
 * 
 * @author pf-miles
 * 
 */
public class Tokens {
    private Tokens() {
    }

    /**
     * end of parsing file(or character stream) token
     */
    public static final Token EOF = new Token(null) {
        private static final long serialVersionUID = 9194950534504326943L;
    };
}

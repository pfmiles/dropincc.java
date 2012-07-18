package com.github.pfmiles.dropincc;

/**
 * Defined some commonly used tokens.
 * 
 * @author pf-miles
 * 
 */
public abstract class Tokens {
    private Tokens() {
    }

    /**
     * end of parsing file(or character stream) token
     */
    public static final TokenDef EOF = new TokenDef("EOF") {
        private static final long serialVersionUID = 9194950534504326943L;

        public String toString() {
            return "EOF";
        }
    };
}

package com.github.pfmiles.dropincc.impl.runtime.impl;

import java.util.Enumeration;

import com.github.pfmiles.dropincc.impl.runtime.Token;

/**
 * 
 * The lexer prototype for runtime copy & use.
 * 
 * @author pf-miles
 * 
 */
public interface LexerPrototype {

    /**
     * Create a enumerable lexer from this prototype.
     * 
     * @param args
     *            any optional args needed by concrete prototype
     *            implementations.
     * @return
     */
    Enumeration<Token> create(Object... args);

}

package com.github.pfmiles.dropincc.impl.runtime.impl;

import java.util.Enumeration;

import com.github.pfmiles.dropincc.impl.runtime.Parser;
import com.github.pfmiles.dropincc.impl.runtime.Token;

/**
 * The parser prototype for runtime copy & use.
 * 
 * @author pf-miles
 * 
 */
public interface ParserPrototype {

    /**
     * @param lexer
     * @param arg
     * @return
     */
    Parser create(Enumeration<Token> lexer, Object arg);
    // TODO
}

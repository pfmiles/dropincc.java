package com.github.pfmiles.dropincc.impl.runtime.impl;

import com.github.pfmiles.dropincc.impl.runtime.Parser;

/**
 * The parser prototype for runtime copy & use.
 * 
 * @author pf-miles
 * 
 */
public interface ParserPrototype {

    /**
     * Create a new parser instance
     * 
     * @param lexer
     * @param arg
     * @return
     */
    Parser create(Lexer lexer);
}

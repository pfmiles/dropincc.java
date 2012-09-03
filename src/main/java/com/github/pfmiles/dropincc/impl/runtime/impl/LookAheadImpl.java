package com.github.pfmiles.dropincc.impl.runtime.impl;

import com.github.pfmiles.dropincc.LookAhead;

/**
 * @author pf-miles
 * 
 */
public class LookAheadImpl implements LookAhead {
    private Lexer lexer;
    
    public LookAheadImpl(Lexer lexer) {
        this.lexer = lexer;
    }

    @SuppressWarnings("unchecked")
    public <T> T ahead(int count) {
        return (T) lexer.LT(count).getLexeme();
    }

}

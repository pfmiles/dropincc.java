package com.github.pfmiles.dropincc.impl.runtime.impl;

import com.github.pfmiles.dropincc.DropinccException;
import com.github.pfmiles.dropincc.impl.runtime.Parser;

/**
 * @author pf-miles
 * 
 */
public class ClassBasedParserPrototype implements ParserPrototype {

    private Class<? extends Parser> parserCls;

    public ClassBasedParserPrototype(Class<? extends Parser> cls) {
        this.parserCls = cls;
    }

    public Parser create(Lexer lexer, Object arg) {
        Parser p = null;
        try {
            p = parserCls.newInstance();
        } catch (Exception e) {
            throw new DropinccException(e);
        }
        p.setLexer(lexer);
        return p;
    }

}

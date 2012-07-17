package com.github.pfmiles.dropincc.impl.lexical;

import com.github.pfmiles.dropincc.TokenDef;

/**
 * used internally to create additional tokens.
 * 
 * @author pf-miles
 * 
 */
final class GenedTokenDef extends TokenDef {

    private static final long serialVersionUID = 7972632057092553906L;

    GenedTokenDef(String regexp) {
        super(regexp);
    }

}
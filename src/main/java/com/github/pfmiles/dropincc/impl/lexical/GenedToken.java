package com.github.pfmiles.dropincc.impl.lexical;

import com.github.pfmiles.dropincc.Token;

/**
 * used internally to create additional tokens.
 * 
 * @author pf-miles
 * 
 */
final class GenedToken extends Token {

    private static final long serialVersionUID = 7972632057092553906L;

    GenedToken(String regexp) {
        super(regexp);
    }

}

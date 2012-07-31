/*******************************************************************************
 * Copyright (c) 2012 pf_miles.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     pf_miles - initial API and implementation
 ******************************************************************************/
package com.github.pfmiles.dropincc.impl.runtime.impl;

import java.util.Map;
import java.util.regex.Pattern;

import com.github.pfmiles.dropincc.impl.TokenType;

/**
 * Lexer prototype using pre-written code skeleton. For string code lexing(not
 * stream lexing).
 * 
 * @author pf-miles
 * 
 */
public class PreWrittenStringLexerPrototype implements LexerPrototype {

    private Map<Integer, TokenType> groupNumToType;
    private Pattern tokenPatterns;
    private boolean whitespaceSensitive;

    public PreWrittenStringLexerPrototype(Map<Integer, TokenType> groupNumToType, Pattern tokenPatterns, boolean whitespaceSensitive) {
        this.groupNumToType = groupNumToType;
        this.tokenPatterns = tokenPatterns;
        this.whitespaceSensitive = whitespaceSensitive;
    }

    public Lexer create(Object... code) {
        return new CodeLexer(this.tokenPatterns, this.groupNumToType, (String) code[0], this.whitespaceSensitive);
    }

}

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
package com.github.pfmiles.dropincc.impl;

/**
 * @author pf-miles
 * 
 */
public class TokenType extends EleType {
    /**
     * Auto-added EOF token type constant
     */
    public static final TokenType EOF = new TokenType(-1, "EOF");
    /**
     * Auto-generated white space token type constant
     */
    public static final TokenType WHITESPACE = new TokenType(-2, "WHITESPACE");

    // just for print only
    private String name;

    /**
     * user defined token defIndex starts from 0, special built-in tokens'
     * defIndex is below 0
     * 
     * @param index
     */
    public TokenType(int index, String name) {
        super(index);
        this.name = name;
    }

    public String toString() {
        return name;
    }

    public String toCodeGenStr() {
        if (this.defIndex >= 0)
            return "tokenType" + this.defIndex;
        return "specialTokenType" + Math.abs(this.defIndex);
    }

}

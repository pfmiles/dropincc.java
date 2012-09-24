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
package com.github.pfmiles.dropincc.template.impl;

import java.text.MessageFormat;

/**
 * Token implementation for dropin template compilation.
 * 
 * @author pf-miles
 * 
 */
public class DtToken {

    /**
     * The special EOF token
     */
    public static final DtToken EOF = new DtToken(DtTokenType.EOF, null, -1, -1);

    private DtTokenType type;
    private String lexeme;
    private int row;
    private int col;

    public DtToken(DtTokenType type, String lexeme, int row, int col) {
        this.type = type;
        this.lexeme = lexeme;
        this.row = row;
        this.col = col;
    }

    public DtTokenType getType() {
        return type;
    }

    public void setType(DtTokenType type) {
        this.type = type;
    }

    public String getLexeme() {
        return lexeme;
    }

    public void setLexeme(String lexeme) {
        this.lexeme = lexeme;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + col;
        result = prime * result + ((lexeme == null) ? 0 : lexeme.hashCode());
        result = prime * result + row;
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        return result;
    }

    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        DtToken other = (DtToken) obj;
        if (col != other.col)
            return false;
        if (lexeme == null) {
            if (other.lexeme != null)
                return false;
        } else if (!lexeme.equals(other.lexeme))
            return false;
        if (row != other.row)
            return false;
        if (type != other.type)
            return false;
        return true;
    }

    public String toString() {
        return new MessageFormat("DtToken({0}, {1}, {2}, {3})").format(new Object[] { this.lexeme, this.type, this.row, this.col });
    }

}

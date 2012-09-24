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

import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author pf-miles
 * 
 */
public class DropinTemplateLexer {

    private static final String LINE_SEP = System.getProperty("line.separator");

    /**
     * lexer rules
     * 
     * <pre>
     * ESCAPED ::= '\\\\#|\\\\\\$'
     * BACK_SLASH ::= '\\\\\\\\'
     * IF ::= '#if'
     * LEFT_PAREN ::= '\\('
     * RIGHT_PAREN ::= '\\)'
     * ELSE_IF ::= '#elseif'
     * ELSE ::= '#else'
     * END ::= '#end'
     * FOREACH ::= '#foreach'
     * REF ::= '\\$\\{[a-zA-Z_]\\w*(\\.[a-zA-Z_]\\w*)*\\}|\\$[a-zA-Z_]\\w*(\\.[a-zA-Z_]\\w*)*'
     * IN ::= 'in'
     * RANGE ::= '\\[[ \t]*\\-?\\d+[ \t]*\\.\\.[ \t]*\\-?\\d+[ \t]*\\]'
     * OR ::= '\\|\\|'
     * AND ::= '\\&\\&'
     * NOT ::= '\\!'
     * EQ ::= '\\=\\='
     * NUM ::= '\\-?\\d+(\\.\\d+)?'
     * STR ::= ''[^']*''
     * WHITE_SPACE ::= '\\s+'
     * PLAIN_TXT ::= '[^\\\\#\\$]+'
     * </pre>
     * 
     * parser rules
     * 
     * <pre>
     * template ::= content $
     * content ::= renderable*
     * renderable ::= IF WHITE_SPACE? LEFT_PAREN WHITE_SPACE? boolExpr WHITE_SPACE? RIGHT_PAREN content (ELSE_IF WHITE_SPACE? LEFT_PAREN WHITE_SPACE? boolExpr WHITE_SPACE? RIGHT_PAREN content)* (ELSE content)? END
     *              | FOREACH WHITE_SPACE? LEFT_PAREN WHITE_SPACE? REF WHITE_SPACE? IN WHITE_SPACE? (REF|RANGE) WHITE_SPACE? RIGHT_PAREN content END
     *              | ESCAPED
     *              | LEFT_PAREN
     *              | RIGHT_PAREN
     *              | REF
     *              | IN
     *              | RANGE
     *              | OR
     *              | AND
     *              | NOT
     *              | EQ
     *              | NUM
     *              | STR
     *              | WHITE_SPACE
     *              | BACK_SLASH
     *              | PLAIN_TXT
     * boolExpr ::= andExpr (WHITE_SPACE? OR WHITE_SPACE? andExpr)*
     * andExpr ::= term (WHITE_SPACE? AND WHITE_SPACE? term)*
     * term ::= NOT? WHITE_SPACE? REF
     *        | value WHITE_SPACE? EQ WHITE_SPACE? value
     *        | NOT? WHITE_SPACE? LEFT_PAREN WHITE_SPACE? boolExpr WHITE_SPACE? RIGHT_PAREN
     * value ::= NUM
     *         | STR
     * </pre>
     */
    private static final Pattern pattern = Pattern.compile(
    // ESCAPED 1
            "(\\G\\\\#|\\\\\\$)" +
            // BACK_SLASH 2
                    "|(\\G\\\\\\\\)" +
                    // IF 3
                    "|(\\G#if)" +
                    // LEFT_PAREN 4
                    "|(\\G\\()" +
                    // RIGHT_PAREN 5
                    "|(\\G\\))" +
                    // ELSE_IF 6
                    "|(\\G#elseif)" +
                    // ELSE 7
                    "|(\\G#else)" +
                    // END 8
                    "|(\\G#end)" +
                    // FOREACH 9
                    "|(\\G#foreach)" +
                    // REF 10
                    "|(\\G\\$\\{[a-zA-Z_]\\w*(\\.[a-zA-Z_]\\w*)*\\}|\\$[a-zA-Z_]\\w*(\\.[a-zA-Z_]\\w*)*)" +
                    // IN 13
                    "|(\\Gin)" +
                    // RANGE 14
                    "|(\\G\\[[ \t]*\\-?\\d+[ \t]*\\.\\.[ \t]*\\-?\\d+[ \t]*\\])" +
                    // OR 15
                    "|(\\G\\|\\|)" +
                    // AND 16
                    "|(\\G\\&\\&)" +
                    // NOT 17
                    "|(\\G\\!)" +
                    // EQ 18
                    "|(\\G\\=\\=)" +
                    // NUM 19
                    "|(\\G\\-?\\d+(\\.\\d+)?)" +
                    // STR 21
                    "|(\\G'[^']*')" +
                    // WHITE_SPACE 22
                    "|(\\G\\s+)" +
                    // PLAIN_TXT 23
                    "|(\\G[^\\\\#\\$]+)");
    private String code = null;
    private Matcher matcher = null;
    private int currentPos = 0;
    private int currentRow = 1;
    private int currentCol = 1;
    private boolean eofReturned = false;

    private LinkedList<DtToken> lookAheadBuf = new LinkedList<DtToken>();

    public boolean hasMoreElements() {
        if (!this.lookAheadBuf.isEmpty() || currentPos < code.length())
            return true;
        if (!this.eofReturned)
            return true;
        return false;
    }

    public DtToken nextElement() {
        if (!this.lookAheadBuf.isEmpty()) {
            return this.lookAheadBuf.removeFirst();
        } else {
            return realNext();
        }
    }

    /**
     * look ahead type
     * 
     * @param i
     *            lookahead count
     * @return
     */
    public DtTokenType LA(int i) {
        DtToken t = this.LT(i);
        return t != null ? t.getType() : null;
    }

    /**
     * look ahead
     * 
     * @param i
     *            lookahead count
     * @return
     */
    public DtToken LT(int i) {
        if (i < this.lookAheadBuf.size())
            return this.lookAheadBuf.get(i - 1);
        int lng = i - this.lookAheadBuf.size();
        for (int j = 0; j < lng; j++) {
            this.lookAheadBuf.add(this.realNext());
        }
        return this.lookAheadBuf.get(i - 1);
    }

    public DtToken realNext() {
        if (currentPos < code.length()) {
            if (this.matcher.find(currentPos)) {
                for (DtTokenType t : DtTokenType.values()) {
                    int gnum = t.getGroupNum();
                    if (gnum != -1) {
                        String txt = this.matcher.group(gnum);
                        if (txt != null) {
                            int nIndex = txt.lastIndexOf('\n');
                            int lexemeLength = this.matcher.end() - this.matcher.start();
                            DtToken ret = new DtToken(t, txt, this.currentRow, this.currentCol);
                            this.currentPos += lexemeLength;
                            if (nIndex != -1) {
                                this.currentRow += countN(txt, nIndex);
                                this.currentCol = lexemeLength - (nIndex + LINE_SEP.length()) + 1;
                            } else {
                                this.currentCol += lexemeLength;
                            }
                            return ret;
                        }
                    }
                }
                throw new RuntimeException("No token matched at position: " + this.currentPos + ", subsequent char: '" + this.code.charAt(currentPos)
                        + "'");
            } else {
                throw new RuntimeException("Unexpected char: '" + this.code.charAt(currentPos) + "' at position: " + this.currentPos);
            }
        } else if (!this.eofReturned) {
            this.eofReturned = true;
            return DtToken.EOF;
        } else {
            return null;
        }
    }

    // count number of line separators
    private static int countN(String txt, int nIndex) {
        int ret = 0;
        int lineSepLength = LINE_SEP.length();
        for (int i = 0; i <= nIndex; i++) {
            if (LINE_SEP.equals(txt.substring(i, i + lineSepLength)))
                ret++;
        }
        return ret;
    }

    public DropinTemplateLexer(String code) {
        this.code = code;
        this.matcher = pattern.matcher(code);
    }

}

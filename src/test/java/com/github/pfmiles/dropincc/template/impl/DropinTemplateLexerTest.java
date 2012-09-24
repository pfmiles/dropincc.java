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

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

/**
 * @author pf-miles
 * 
 */
public class DropinTemplateLexerTest extends TestCase {
    /**
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
     */
    public void testAllTokenMatches() {
        // ESCAPED ::= '\\\\#|\\\\\\$'
        DropinTemplateLexer lexer = new DropinTemplateLexer("\\#\\$");
        List<DtToken> ts = lexAll(lexer);
        assertTrue(ts.size() == 3);
        assertTrue(ts.get(0).getLexeme().equals("\\#"));
        assertTrue(ts.get(0).getType() == DtTokenType.ESCAPED);
        assertTrue(ts.get(0).getRow() == 1);
        assertTrue(ts.get(0).getCol() == 1);
        assertTrue(ts.get(1).getLexeme().equals("\\$"));
        assertTrue(ts.get(1).getRow() == 1);
        assertTrue(ts.get(1).getCol() == 3);
        assertTrue(ts.get(1).getType() == DtTokenType.ESCAPED);
        assertTrue(ts.get(2).equals(DtToken.EOF));
        // BACK_SLASH ::= '\\\\'
        lexer = new DropinTemplateLexer("\\\\\\\\");
        ts = lexAll(lexer);
        assertTrue(ts.size() == 3);
        assertTrue(ts.get(0).getLexeme().equals("\\\\"));
        assertTrue(ts.get(0).getType() == DtTokenType.BACK_SLASH);
        assertTrue(ts.get(0).getRow() == 1);
        assertTrue(ts.get(0).getCol() == 1);
        assertTrue(ts.get(1).getLexeme().equals("\\\\"));
        assertTrue(ts.get(1).getRow() == 1);
        assertTrue(ts.get(1).getCol() == 3);
        assertTrue(ts.get(1).getType() == DtTokenType.BACK_SLASH);
        assertTrue(ts.get(2).equals(DtToken.EOF));
        // IF ::= '#if'
        lexer = new DropinTemplateLexer("#if#if");
        ts = lexAll(lexer);
        assertTrue(ts.size() == 3);
        assertTrue(ts.get(0).getLexeme().equals("#if"));
        assertTrue(ts.get(0).getType() == DtTokenType.IF);
        assertTrue(ts.get(0).getRow() == 1);
        assertTrue(ts.get(0).getCol() == 1);
        assertTrue(ts.get(1).getLexeme().equals("#if"));
        assertTrue(ts.get(1).getRow() == 1);
        assertTrue(ts.get(1).getCol() == 4);
        assertTrue(ts.get(1).getType() == DtTokenType.IF);
        assertTrue(ts.get(2).equals(DtToken.EOF));
        // LEFT_PAREN ::= '\\('
        lexer = new DropinTemplateLexer("((");
        ts = lexAll(lexer);
        assertTrue(ts.size() == 3);
        assertTrue(ts.get(0).getLexeme().equals("("));
        assertTrue(ts.get(0).getType() == DtTokenType.LEFT_PAREN);
        assertTrue(ts.get(0).getRow() == 1);
        assertTrue(ts.get(0).getCol() == 1);
        assertTrue(ts.get(1).getLexeme().equals("("));
        assertTrue(ts.get(1).getRow() == 1);
        assertTrue(ts.get(1).getCol() == 2);
        assertTrue(ts.get(1).getType() == DtTokenType.LEFT_PAREN);
        assertTrue(ts.get(2).equals(DtToken.EOF));
        // RIGHT_PAREN ::= '\\)'
        lexer = new DropinTemplateLexer("))");
        ts = lexAll(lexer);
        assertTrue(ts.size() == 3);
        assertTrue(ts.get(0).getLexeme().equals(")"));
        assertTrue(ts.get(0).getType() == DtTokenType.RIGHT_PAREN);
        assertTrue(ts.get(0).getRow() == 1);
        assertTrue(ts.get(0).getCol() == 1);
        assertTrue(ts.get(1).getLexeme().equals(")"));
        assertTrue(ts.get(1).getRow() == 1);
        assertTrue(ts.get(1).getCol() == 2);
        assertTrue(ts.get(1).getType() == DtTokenType.RIGHT_PAREN);
        assertTrue(ts.get(2).equals(DtToken.EOF));
        // ELSE_IF ::= '#elseif'
        lexer = new DropinTemplateLexer("#elseif#elseif");
        ts = lexAll(lexer);
        assertTrue(ts.size() == 3);
        assertTrue(ts.get(0).getLexeme().equals("#elseif"));
        assertTrue(ts.get(0).getType() == DtTokenType.ELSE_IF);
        assertTrue(ts.get(0).getRow() == 1);
        assertTrue(ts.get(0).getCol() == 1);
        assertTrue(ts.get(1).getLexeme().equals("#elseif"));
        assertTrue(ts.get(1).getRow() == 1);
        assertTrue(ts.get(1).getCol() == 8);
        assertTrue(ts.get(1).getType() == DtTokenType.ELSE_IF);
        assertTrue(ts.get(2).equals(DtToken.EOF));
        // ELSE ::= '#else'
        lexer = new DropinTemplateLexer("#else#else");
        ts = lexAll(lexer);
        assertTrue(ts.size() == 3);
        assertTrue(ts.get(0).getLexeme().equals("#else"));
        assertTrue(ts.get(0).getType() == DtTokenType.ELSE);
        assertTrue(ts.get(0).getRow() == 1);
        assertTrue(ts.get(0).getCol() == 1);
        assertTrue(ts.get(1).getLexeme().equals("#else"));
        assertTrue(ts.get(1).getRow() == 1);
        assertTrue(ts.get(1).getCol() == 6);
        assertTrue(ts.get(1).getType() == DtTokenType.ELSE);
        assertTrue(ts.get(2).equals(DtToken.EOF));
        // END ::= '#end'
        lexer = new DropinTemplateLexer("#end#end");
        ts = lexAll(lexer);
        assertTrue(ts.size() == 3);
        assertTrue(ts.get(0).getLexeme().equals("#end"));
        assertTrue(ts.get(0).getType() == DtTokenType.END);
        assertTrue(ts.get(0).getRow() == 1);
        assertTrue(ts.get(0).getCol() == 1);
        assertTrue(ts.get(1).getLexeme().equals("#end"));
        assertTrue(ts.get(1).getRow() == 1);
        assertTrue(ts.get(1).getCol() == 5);
        assertTrue(ts.get(1).getType() == DtTokenType.END);
        assertTrue(ts.get(2).equals(DtToken.EOF));
        // FOREACH ::= '#foreach'
        lexer = new DropinTemplateLexer("#foreach#foreach");
        ts = lexAll(lexer);
        assertTrue(ts.size() == 3);
        assertTrue(ts.get(0).getLexeme().equals("#foreach"));
        assertTrue(ts.get(0).getType() == DtTokenType.FOREACH);
        assertTrue(ts.get(0).getRow() == 1);
        assertTrue(ts.get(0).getCol() == 1);
        assertTrue(ts.get(1).getLexeme().equals("#foreach"));
        assertTrue(ts.get(1).getRow() == 1);
        assertTrue(ts.get(1).getCol() == 9);
        assertTrue(ts.get(1).getType() == DtTokenType.FOREACH);
        assertTrue(ts.get(2).equals(DtToken.EOF));
        // REF ::=
        // '\\$\\{[a-zA-Z_]\\w*(\\.[a-zA-Z_]\\w*)*\\}|\\$[a-zA-Z_]\\w*(\\.[a-zA-Z_]\\w*)*'
        lexer = new DropinTemplateLexer("${c_om.al1baba_}$co_m.al1bab0._china");
        ts = lexAll(lexer);
        assertTrue(ts.size() == 3);
        assertTrue(ts.get(0).getLexeme().equals("${c_om.al1baba_}"));
        assertTrue(ts.get(0).getType() == DtTokenType.REF);
        assertTrue(ts.get(0).getRow() == 1);
        assertTrue(ts.get(0).getCol() == 1);
        assertTrue(ts.get(1).getLexeme().equals("$co_m.al1bab0._china"));
        assertTrue(ts.get(1).getRow() == 1);
        assertTrue(ts.get(1).getCol() == 17);
        assertTrue(ts.get(1).getType() == DtTokenType.REF);
        assertTrue(ts.get(2).equals(DtToken.EOF));
        // IN ::= 'in'
        lexer = new DropinTemplateLexer("inin");
        ts = lexAll(lexer);
        assertTrue(ts.size() == 3);
        assertTrue(ts.get(0).getLexeme().equals("in"));
        assertTrue(ts.get(0).getType() == DtTokenType.IN);
        assertTrue(ts.get(0).getRow() == 1);
        assertTrue(ts.get(0).getCol() == 1);
        assertTrue(ts.get(1).getLexeme().equals("in"));
        assertTrue(ts.get(1).getRow() == 1);
        assertTrue(ts.get(1).getCol() == 3);
        assertTrue(ts.get(1).getType() == DtTokenType.IN);
        assertTrue(ts.get(2).equals(DtToken.EOF));
        // RANGE ::= '\\[[ \t]*\\-?\\d+[ \t]*\\.\\.[ \t]*\\-?\\d+[ \t]*\\]'
        lexer = new DropinTemplateLexer("[  -5  ..    10    ][0..-10]");
        ts = lexAll(lexer);
        assertTrue(ts.size() == 3);
        assertTrue(ts.get(0).getLexeme().equals("[  -5  ..    10    ]"));
        assertTrue(ts.get(0).getType() == DtTokenType.RANGE);
        assertTrue(ts.get(0).getRow() == 1);
        assertTrue(ts.get(0).getCol() == 1);
        assertTrue(ts.get(1).getLexeme().equals("[0..-10]"));
        assertTrue(ts.get(1).getRow() == 1);
        assertTrue(ts.get(1).getCol() == 21);
        assertTrue(ts.get(1).getType() == DtTokenType.RANGE);
        assertTrue(ts.get(2).equals(DtToken.EOF));
        // OR ::= '\\|\\|'
        lexer = new DropinTemplateLexer("||||");
        ts = lexAll(lexer);
        assertTrue(ts.size() == 3);
        assertTrue(ts.get(0).getLexeme().equals("||"));
        assertTrue(ts.get(0).getType() == DtTokenType.OR);
        assertTrue(ts.get(0).getRow() == 1);
        assertTrue(ts.get(0).getCol() == 1);
        assertTrue(ts.get(1).getLexeme().equals("||"));
        assertTrue(ts.get(1).getRow() == 1);
        assertTrue(ts.get(1).getCol() == 3);
        assertTrue(ts.get(1).getType() == DtTokenType.OR);
        assertTrue(ts.get(2).equals(DtToken.EOF));
        // AND ::= '\\&\\&'
        lexer = new DropinTemplateLexer("&&&&");
        ts = lexAll(lexer);
        assertTrue(ts.size() == 3);
        assertTrue(ts.get(0).getLexeme().equals("&&"));
        assertTrue(ts.get(0).getType() == DtTokenType.AND);
        assertTrue(ts.get(0).getRow() == 1);
        assertTrue(ts.get(0).getCol() == 1);
        assertTrue(ts.get(1).getLexeme().equals("&&"));
        assertTrue(ts.get(1).getRow() == 1);
        assertTrue(ts.get(1).getCol() == 3);
        assertTrue(ts.get(1).getType() == DtTokenType.AND);
        assertTrue(ts.get(2).equals(DtToken.EOF));
        // NOT ::= '\\!'
        lexer = new DropinTemplateLexer("!!");
        ts = lexAll(lexer);
        assertTrue(ts.size() == 3);
        assertTrue(ts.get(0).getLexeme().equals("!"));
        assertTrue(ts.get(0).getType() == DtTokenType.NOT);
        assertTrue(ts.get(0).getRow() == 1);
        assertTrue(ts.get(0).getCol() == 1);
        assertTrue(ts.get(1).getLexeme().equals("!"));
        assertTrue(ts.get(1).getRow() == 1);
        assertTrue(ts.get(1).getCol() == 2);
        assertTrue(ts.get(1).getType() == DtTokenType.NOT);
        assertTrue(ts.get(2).equals(DtToken.EOF));
        // EQ ::= '\\=\\='
        lexer = new DropinTemplateLexer("====");
        ts = lexAll(lexer);
        assertTrue(ts.size() == 3);
        assertTrue(ts.get(0).getLexeme().equals("=="));
        assertTrue(ts.get(0).getType() == DtTokenType.EQ);
        assertTrue(ts.get(0).getRow() == 1);
        assertTrue(ts.get(0).getCol() == 1);
        assertTrue(ts.get(1).getLexeme().equals("=="));
        assertTrue(ts.get(1).getRow() == 1);
        assertTrue(ts.get(1).getCol() == 3);
        assertTrue(ts.get(1).getType() == DtTokenType.EQ);
        assertTrue(ts.get(2).equals(DtToken.EOF));
        // NUM ::= '\\-?\\d+(\\.\\d+)?'
        lexer = new DropinTemplateLexer("-1.25 12345.689004");
        ts = lexAll(lexer);
        assertTrue(ts.size() == 4);
        assertTrue(ts.get(0).getLexeme().equals("-1.25"));
        assertTrue(ts.get(0).getType() == DtTokenType.NUM);
        assertTrue(ts.get(0).getRow() == 1);
        assertTrue(ts.get(0).getCol() == 1);
        assertTrue(ts.get(2).getLexeme().equals("12345.689004"));
        assertTrue(ts.get(2).getRow() == 1);
        assertTrue(ts.get(2).getCol() == 7);
        assertTrue(ts.get(2).getType() == DtTokenType.NUM);
        assertTrue(ts.get(3).equals(DtToken.EOF));
        // STR ::= ''[^']*''
        lexer = new DropinTemplateLexer("'ha  \nha''heher7798@%&!%@&!%@&!%'");
        ts = lexAll(lexer);
        assertTrue(ts.size() == 3);
        assertTrue(ts.get(0).getLexeme().equals("'ha  \nha'"));
        assertTrue(ts.get(0).getType() == DtTokenType.STR);
        assertTrue(ts.get(0).getRow() == 1);
        assertTrue(ts.get(0).getCol() == 1);
        assertTrue(ts.get(1).getLexeme().equals("'heher7798@%&!%@&!%@&!%'"));
        assertTrue(ts.get(1).getRow() == 2);
        assertTrue(ts.get(1).getCol() == 4);
        assertTrue(ts.get(1).getType() == DtTokenType.STR);
        assertTrue(ts.get(2).equals(DtToken.EOF));
        // WHITE_SPACE ::= '\\s+'
        lexer = new DropinTemplateLexer("   \r\n\n\r   \n  \t \r -1.25");
        ts = lexAll(lexer);
        assertTrue(ts.size() == 3);
        assertTrue(ts.get(0).getLexeme().equals("   \r\n\n\r   \n  \t \r "));
        assertTrue(ts.get(0).getType() == DtTokenType.WHITE_SPACE);
        assertTrue(ts.get(0).getRow() == 1);
        assertTrue(ts.get(0).getCol() == 1);
        assertTrue(ts.get(1).getLexeme().equals("-1.25"));
        assertTrue(ts.get(1).getRow() == 4);
        assertTrue(ts.get(1).getCol() == 7);
        assertTrue(ts.get(1).getType() == DtTokenType.NUM);
        assertTrue(ts.get(2).equals(DtToken.EOF));
        // PLAIN_TXT ::= '[^\\\\#\\$]+'
        lexer = new DropinTemplateLexer("The quick brown fox,\n jumps over the lazy dog.#ifhelloworld!${miles}thanks\\#hehe\\$haha\\\\ok");
        ts = lexAll(lexer);
        assertTrue(ts.size() == 12);
        assertTrue(ts.get(0).getLexeme().equals("The quick brown fox,\n jumps over the lazy dog."));
        assertTrue(ts.get(0).getType() == DtTokenType.PLAIN_TXT);
        assertTrue(ts.get(0).getRow() == 1);
        assertTrue(ts.get(0).getCol() == 1);
        assertTrue(ts.get(2).getLexeme().equals("helloworld!"));
        assertTrue(ts.get(2).getRow() == 2);
        assertTrue(ts.get(2).getCol() == 29);
        assertTrue(ts.get(2).getType() == DtTokenType.PLAIN_TXT);
        assertTrue(ts.get(11).equals(DtToken.EOF));
    }

    private static List<DtToken> lexAll(DropinTemplateLexer lexer) {
        List<DtToken> ret = new ArrayList<DtToken>();
        while (lexer.hasMoreElements())
            ret.add(lexer.nextElement());
        return ret;
    }
}

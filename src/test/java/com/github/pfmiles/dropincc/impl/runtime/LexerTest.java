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
package com.github.pfmiles.dropincc.impl.runtime;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import com.github.pfmiles.dropincc.CC;
import com.github.pfmiles.dropincc.Element;
import com.github.pfmiles.dropincc.Exe;
import com.github.pfmiles.dropincc.Grule;
import com.github.pfmiles.dropincc.Lang;
import com.github.pfmiles.dropincc.TokenDef;
import com.github.pfmiles.dropincc.impl.AnalyzedLang;
import com.github.pfmiles.dropincc.impl.runtime.impl.Lexer;
import com.github.pfmiles.dropincc.testhelper.TestHelper;

/**
 * @author pf-miles
 * 
 */
public class LexerTest extends TestCase {

    public void testBasicLex1() {
        Lang lang = new Lang("Test");
        Element a = lang.newToken("a");
        Element b = lang.newToken("b");
        Element c = lang.newToken("c");
        Grule A = lang.newGrule();
        lang.defineGrule(A, CC.EOF);
        A.define(CC.ks(a), b).alt(CC.kc(a), c);
        Exe exe = lang.compile();
        // to test ignore whitespaces
        // System.out.println(exe.lexing("     abc   a\r\tbc\n   \r   "));
        // +1 for EOF
        assertTrue(exe.lexing("     abc   a\r\tbc\n   \r   ").size() == 7);
    }

    // public void testBasicInstantTokens()

    public void testJavaTokens() {
        Lang lang = new Lang("Test");
        // these keywords tokens must be defined before the identifier token in
        // the initial version of dropincc.java, because it could not do
        // 'longest' match, the later versions of dropincc.java should solve the
        // problem
        Element _public = lang.newToken("public");
        Element _void = lang.newToken("void");
        Element digit = lang.newToken("\\d+");
        Element _id = lang.newToken("[a-zA-Z_]\\w*");
        Element _new = lang.newToken("new");
        Element leftParen = lang.newToken("\\(");
        Element rightParen = lang.newToken("\\)");
        Element leftBrace = lang.newToken("\\{");
        Element rightBrace = lang.newToken("\\}");
        Element equal = lang.newToken("\\=\\=");
        Element assign = lang.newToken("\\=");
        Element dot = lang.newToken("\\.");
        Element semi = lang.newToken(";");
        Element comma = lang.newToken(",");
        Element str = lang.newToken("\"[^\"]*\"");

        lang.defineGrule(_public, _void, _new, digit, _id, leftParen, rightParen, leftBrace, rightBrace, equal, assign, dot, semi, comma, str, CC.EOF);

        Exe exe = lang.compile();
        List<Token> ts = exe.lexing("public void testBasicLex1() {" + "Lang lang = new Lang();" + "Element a = lang.newToken(\"a\");"
                + "Element b = lang.newToken(\"b\");" + "Element c = lang.newToken(\"c\");" + "Grule A = lang.newGrule();" + "lang.defineGrule(A, CC.EOF);"
                + "A.define(CC.ks(a), b).alt(CC.kc(a), c);" + "Exe exe = lang.compile();" + "assertTrue(exe.lexing(\"ab\\r\\t   c d\").size() == 7);" + "}");
        // for (Token t : ts)
        // System.out.println(t);
        // +1 for EOF
        assertTrue(ts.size() == 117);
    }

    public void testJavaRegexAnnoyingTokens() {
        Lang lang = new Lang("Test");
        Grule A = lang.newGrule();
        lang.defineGrule(A, CC.EOF);
        A.define("\\(", A, "\\)").alt("\\\\G");
        Exe exe = lang.compile();
        assertTrue(exe.lexing("(((\\G)))").size() == 8);
    }

    public void testWhitespaceSensitive() {
        Lang lang = new Lang("Test");
        Grule A = lang.newGrule();
        lang.defineGrule(A, CC.EOF);
        A.define(" \\(", A, "\\) ").alt(" \\\\G ");
        lang.setWhiteSpaceSensitive(true);
        Exe exe = lang.compile();
        // System.out.println(exe.lexing(" ( ( ( \\G ) ) ) "));
        assertTrue(exe.lexing(" ( ( ( \\G ) ) ) ").size() == 8);
    }

    public void testLaLt() {
        Lang lang = new Lang("Test");
        Grule A = lang.newGrule();
        lang.defineGrule(A, CC.EOF);
        A.define(" \\(", A, "\\) ").alt(" \\\\G ");
        lang.setWhiteSpaceSensitive(true);
        Exe exe = lang.compile();
        AnalyzedLang al = TestHelper.priField(exe, "al");
        Lexer l = al.newLexer(" ( ( ( \\G ) ) ) ");
        for (int i = 1; i <= 8; i++) {
            assertTrue(l.LA(i) != null);
        }
        assertTrue(l.LA(9) == null);
        List<Token> ts = new ArrayList<Token>();
        while (l.hasMoreElements())
            ts.add(l.nextElement());
        // System.out.println(ts);
        assertTrue(ts.size() == 8);
    }

    public void testLexerRuleContainsVerticalBar() {
        Lang lang = new Lang("Test");
        TokenDef a = lang.newToken("ab|bc");
        TokenDef b = lang.newToken("uv|wx");
        lang.defineGrule(CC.ks(a, b), CC.EOF);
        Exe exe = lang.compile();
        List<Token> ts = exe.lexing("bcabababbcbcabwxwxuvwx");
        // System.out.println(ts);
        assertTrue(ts.size() == 12);
    }
}

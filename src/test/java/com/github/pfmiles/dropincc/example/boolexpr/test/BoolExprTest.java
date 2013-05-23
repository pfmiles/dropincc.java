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
package com.github.pfmiles.dropincc.example.boolexpr.test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import junit.framework.TestCase;

import com.github.pfmiles.dropincc.example.boolexpr.BoolExpr;

/**
 * @author pf-miles Aug 21, 2012 1:45:51 PM
 */
public class BoolExprTest extends TestCase {

    public void testBasic() throws Throwable {
        assertTrue(BoolExpr.exe("1.25     >-1.25"));
        assertFalse(BoolExpr.exe("1.25<     -1.25"));
        assertFalse(BoolExpr.exe("     1.25=-1.25"));
        assertTrue(BoolExpr.exe("     1.25    !=    -1.25    "));

        assertTrue(BoolExpr.exe("#2012-04-24 16:26:36#     >#2012-04-24#"));
        assertFalse(BoolExpr.exe("#2012-04-24 16:26:36#<     #2012-04-24#"));
        assertFalse(BoolExpr.exe("     #2012-04-24 16:26:36#=#2012-04-24#"));
        assertTrue(BoolExpr.exe("     #2012-04-24 16:26:36#    !=    #2012-04-24#    "));

        Map<String, Object> context = new HashMap<String, Object>();
        context.put("$a", 1.25);
        context.put("$b", -1.25);
        assertTrue(BoolExpr.exe(context, "$a     >$b"));
        assertFalse(BoolExpr.exe(context, "$a<     $b"));
        assertFalse(BoolExpr.exe(context, "     $a=$b"));
        assertTrue(BoolExpr.exe(context, "     $a    !=    $b    "));

        context = new HashMap<String, Object>();
        context.put("$a", new Date());
        Calendar c = Calendar.getInstance();
        c.set(Calendar.MILLISECOND, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.HOUR_OF_DAY, 0);
        context.put("$b", c.getTime());
        assertTrue(BoolExpr.exe(context, "$a     >$b"));
        assertFalse(BoolExpr.exe(context, "$a<     $b"));
        assertFalse(BoolExpr.exe(context, "     $a=$b"));
        assertTrue(BoolExpr.exe(context, "     $a    !=    $b    "));

        context = new HashMap<String, Object>();
        context.put("$a", new Date());
        assertTrue(BoolExpr.exe(context, "$a     >#2012-04-24#"));
        assertFalse(BoolExpr.exe(context, "$a<     #2012-04-24#"));
        assertFalse(BoolExpr.exe(context, "     $a=#2012-04-24#"));
        assertTrue(BoolExpr.exe(context, "     $a    !=    #2012-04-24#    "));

        context = new HashMap<String, Object>();
        context.put("$a", 1.25);
        assertTrue(BoolExpr.exe(context, "$a     >-1.25"));
        assertFalse(BoolExpr.exe(context, "$a<     -1.25"));
        assertFalse(BoolExpr.exe(context, "     $a=-1.25"));
        assertTrue(BoolExpr.exe(context, "     $a    !=    -1.25    "));

        context = new HashMap<String, Object>();
        context.put("$b", -1.25);
        assertTrue(BoolExpr.exe(context, "1.25     >$b"));
        assertFalse(BoolExpr.exe(context, "1.25<     $b"));
        assertFalse(BoolExpr.exe(context, "     1.25=$b"));
        assertTrue(BoolExpr.exe(context, "     1.25    !=    $b    "));

        context = new HashMap<String, Object>();
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse("2012-04-24");
        context.put("$b", date);
        assertTrue(BoolExpr.exe(context, "#2012-04-24 16:26:36#     >$b"));
        assertFalse(BoolExpr.exe(context, "#2012-04-24 16:26:36#<     $b"));
        assertFalse(BoolExpr.exe(context, "     #2012-04-24 16:26:36#=$b"));
        assertTrue(BoolExpr.exe(context, "     #2012-04-24 16:26:36#    !=    $b    "));

        assertTrue(BoolExpr.exe("1.25 between (1, 2)"));
        assertFalse(BoolExpr.exe("-1.25 between (1, 2)"));
        assertFalse(BoolExpr.exe("3 between (1, 2)"));
        assertFalse(BoolExpr.exe("1.25 between (1.25, 2)"));
        assertFalse(BoolExpr.exe("-1.25 between (-2, -1.25)"));
        assertTrue(BoolExpr.exe("1.25 between [1.25, 2)"));
        assertTrue(BoolExpr.exe("-1.25 between (-2, -1.25]"));
        assertFalse(BoolExpr.exe("1.5 between [2, 1.25]"));
        context = new HashMap<String, Object>();
        context.put("$a", 1.25);
        assertTrue(BoolExpr.exe(context, "$a between (1, 2)"));
        context.put("$a", -1.25);
        assertFalse(BoolExpr.exe(context, "$a between (1, 2)"));
        context.put("$a", 3);
        assertFalse(BoolExpr.exe(context, "$a between (1, 2)"));
        context.put("$a", 1.25);
        assertFalse(BoolExpr.exe(context, "$a between (1.25, 2)"));
        context.put("$a", -1.25);
        assertFalse(BoolExpr.exe(context, "$a between (-2, -1.25)"));
        context.put("$a", 1.25);
        assertTrue(BoolExpr.exe(context, "$a between [1.25, 2)"));
        context.put("$a", -1.25);
        assertTrue(BoolExpr.exe(context, "$a between (-2, -1.25]"));
        context.put("$a", 1.5);
        assertFalse(BoolExpr.exe(context, "$a between [2, 1.25]"));

        assertFalse(BoolExpr.exe("1.25 !between (1, 2)"));
        assertTrue(BoolExpr.exe("-1.25 !between (1, 2)"));
        assertTrue(BoolExpr.exe("3 !between (1, 2)"));
        assertTrue(BoolExpr.exe("1.25 !between (1.25, 2)"));
        assertTrue(BoolExpr.exe("-1.25 !between (-2, -1.25)"));
        assertFalse(BoolExpr.exe("1.25 !between [1.25, 2)"));
        assertFalse(BoolExpr.exe("-1.25 !between (-2, -1.25]"));
        assertTrue(BoolExpr.exe("1.5 !between [2, 1.25]"));
        context = new HashMap<String, Object>();
        context.put("$a", 1.25);
        assertFalse(BoolExpr.exe(context, "$a !between (1, 2)"));
        context.put("$a", -1.25);
        assertTrue(BoolExpr.exe(context, "$a !between (1, 2)"));
        context.put("$a", 3);
        assertTrue(BoolExpr.exe(context, "$a !between (1, 2)"));
        context.put("$a", 1.25);
        assertTrue(BoolExpr.exe(context, "$a !between (1.25, 2)"));
        context.put("$a", -1.25);
        assertTrue(BoolExpr.exe(context, "$a !between (-2, -1.25)"));
        context.put("$a", 1.25);
        assertFalse(BoolExpr.exe(context, "$a !between [1.25, 2)"));
        context.put("$a", -1.25);
        assertFalse(BoolExpr.exe(context, "$a !between (-2, -1.25]"));
        context.put("$a", 1.5);
        assertTrue(BoolExpr.exe(context, "$a !between [2, 1.25]"));

        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat fmts = new SimpleDateFormat("yyyy-MM-dd");
        assertTrue(BoolExpr.exe("#2012-04-24 16:26:36# between (#2012-04-24#, #2012-04-25#)"));
        assertFalse(BoolExpr.exe("#2012-04-23 16:26:36# between (#2012-04-24#, #2012-04-25#)"));
        assertFalse(BoolExpr.exe("#2012-04-25 16:26:36# between (#2012-04-24#, #2012-04-25#)"));
        assertFalse(BoolExpr.exe("#2012-04-24# between (#2012-04-24#, #2012-04-25#)"));
        assertFalse(BoolExpr.exe("#2012-04-25# between (#2012-04-24#, #2012-04-25#)"));
        assertTrue(BoolExpr.exe("#2012-04-24# between [#2012-04-24#, #2012-04-25#)"));
        assertTrue(BoolExpr.exe("#2012-04-25# between (#2012-04-24#, #2012-04-25#]"));
        assertFalse(BoolExpr.exe("#2012-04-24 16:26:36# between (#2012-04-25#, #2012-04-24#)"));
        context = new HashMap<String, Object>();
        context.put("$a", fmt.parse("2012-04-24 16:26:36"));
        assertTrue(BoolExpr.exe(context, "$a between (#2012-04-24#, #2012-04-25#)"));
        context.put("$a", fmt.parse("2012-04-23 16:26:36"));
        assertFalse(BoolExpr.exe(context, "$a between (#2012-04-24#, #2012-04-25#)"));
        context.put("$a", fmt.parse("2012-04-25 16:26:36"));
        assertFalse(BoolExpr.exe(context, "$a between (#2012-04-24#, #2012-04-25#)"));
        context.put("$a", fmts.parse("2012-04-24"));
        assertFalse(BoolExpr.exe(context, "$a between (#2012-04-24#, #2012-04-25#)"));
        context.put("$a", fmts.parse("2012-04-25"));
        assertFalse(BoolExpr.exe(context, "$a between (#2012-04-24#, #2012-04-25#)"));
        context.put("$a", fmts.parse("2012-04-24"));
        assertTrue(BoolExpr.exe(context, "$a between [#2012-04-24#, #2012-04-25#)"));
        context.put("$a", fmts.parse("2012-04-25"));
        assertTrue(BoolExpr.exe(context, "$a between (#2012-04-24#, #2012-04-25#]"));
        context.put("$a", fmt.parse("2012-04-24 16:26:36"));
        assertFalse(BoolExpr.exe(context, "$a between (#2012-04-25#, #2012-04-24#)"));

        assertFalse(BoolExpr.exe("#2012-04-24 16:26:36# !between (#2012-04-24#, #2012-04-25#)"));
        assertTrue(BoolExpr.exe("#2012-04-23 16:26:36# !between (#2012-04-24#, #2012-04-25#)"));
        assertTrue(BoolExpr.exe("#2012-04-25 16:26:36# !between (#2012-04-24#, #2012-04-25#)"));
        assertTrue(BoolExpr.exe("#2012-04-24# !between (#2012-04-24#, #2012-04-25#)"));
        assertTrue(BoolExpr.exe("#2012-04-25# !between (#2012-04-24#, #2012-04-25#)"));
        assertFalse(BoolExpr.exe("#2012-04-24# !between [#2012-04-24#, #2012-04-25#)"));
        assertFalse(BoolExpr.exe("#2012-04-25# !between (#2012-04-24#, #2012-04-25#]"));
        assertTrue(BoolExpr.exe("#2012-04-24 16:26:36# !between (#2012-04-25#, #2012-04-24#)"));
        context = new HashMap<String, Object>();
        context.put("$a", fmt.parse("2012-04-24 16:26:36"));
        assertFalse(BoolExpr.exe(context, "$a !between (#2012-04-24#, #2012-04-25#)"));
        context.put("$a", fmt.parse("2012-04-23 16:26:36"));
        assertTrue(BoolExpr.exe(context, "$a !between (#2012-04-24#, #2012-04-25#)"));
        context.put("$a", fmt.parse("2012-04-25 16:26:36"));
        assertTrue(BoolExpr.exe(context, "$a !between (#2012-04-24#, #2012-04-25#)"));
        context.put("$a", fmts.parse("2012-04-24"));
        assertTrue(BoolExpr.exe(context, "$a !between (#2012-04-24#, #2012-04-25#)"));
        context.put("$a", fmts.parse("2012-04-25"));
        assertTrue(BoolExpr.exe(context, "$a !between (#2012-04-24#, #2012-04-25#)"));
        context.put("$a", fmts.parse("2012-04-24"));
        assertFalse(BoolExpr.exe(context, "$a !between [#2012-04-24#, #2012-04-25#)"));
        context.put("$a", fmts.parse("2012-04-25"));
        assertFalse(BoolExpr.exe(context, "$a !between (#2012-04-24#, #2012-04-25#]"));
        context.put("$a", fmt.parse("2012-04-24 16:26:36"));
        assertTrue(BoolExpr.exe(context, "$a !between (#2012-04-25#, #2012-04-24#)"));

        assertTrue(BoolExpr.exe("'test123' = 'test123'"));
        assertFalse(BoolExpr.exe("'test123' != 'test123'"));

        context = new HashMap<String, Object>();
        context.put("$a", "test123");
        assertTrue(BoolExpr.exe(context, "'test123' = $a"));
        assertFalse(BoolExpr.exe(context, "'test123' != $a"));
        context.put("$a", "test");

        context = new HashMap<String, Object>();
        context.put("$a", "test123");
        assertTrue(BoolExpr.exe(context, "$a = 'test123'"));
        assertFalse(BoolExpr.exe(context, "$a != 'test123'"));
        Set<String> set = new HashSet<String>();
        set.add("test");
        set.add("test1");
        context.put("$a", set);

        context = new HashMap<String, Object>();
        context.put("$a", set);
        context.put("$b", "test1");
        assertTrue(BoolExpr.exe(context, "$a include $b"));
        assertFalse(BoolExpr.exe(context, "$a !include $b"));

        assertTrue(BoolExpr.exe("true"));
        assertFalse(BoolExpr.exe("false"));
        assertFalse(BoolExpr.exe("!(true)"));
        assertTrue(BoolExpr.exe("!!(true)"));
        assertFalse(BoolExpr.exe("!!!(true)"));

        context = new HashMap<String, Object>();
        context.put("$a", "test");
        assertTrue(BoolExpr.exe("{-1.25, #2012-04-25#, 'test'} include -1.25"));
        assertTrue(BoolExpr.exe("{-1.25, #2012-04-25#, 'test'} include #2012-04-25#"));
        assertTrue(BoolExpr.exe(context, "{-1.25, #2012-04-25#, 'test'} include $a"));
        assertTrue(BoolExpr.exe("{-1.25, #2012-04-25#, 'test'} include 'test'"));

        assertFalse(BoolExpr.exe("{-1.25, #2012-04-25#, 'test'} !include -1.25"));
        assertFalse(BoolExpr.exe("{-1.25, #2012-04-25#, 'test'} !include #2012-04-25#"));
        assertFalse(BoolExpr.exe(context, "{-1.25, #2012-04-25#, 'test'} !include $a"));
        assertFalse(BoolExpr.exe("{-1.25, #2012-04-25#, 'test'} !include 'test'"));

        context = new HashMap<String, Object>();
        Set<Object> set3 = new HashSet<Object>();
        set3.add(-1.25);
        set3.add(fmts.parse("2012-04-25"));
        context.put("$a", set3);
        assertTrue(BoolExpr.exe(context, "$a include -1.25"));
        assertTrue(BoolExpr.exe(context, "$a include #2012-04-25#"));

        assertFalse(BoolExpr.exe(context, "$a !include -1.25"));
        assertFalse(BoolExpr.exe(context, "$a !include #2012-04-25#"));

        // compound tests
        assertTrue(BoolExpr.exe("2>1 && 4< 5"));
        assertFalse(BoolExpr.exe("2>1 && 4> 5"));
        assertTrue(BoolExpr.exe("2>1 || 4< 5"));
        assertFalse(BoolExpr.exe("2>3 || 6< 5"));

        assertTrue(BoolExpr.exe("2>1 && 4< 5 || false"));
        assertTrue(BoolExpr.exe("2>1 && 4> 5 || true"));
        assertTrue(BoolExpr.exe("2>1 || 4< 5 && false"));
        assertTrue(BoolExpr.exe("true || 6< 5 && true"));

        assertTrue(BoolExpr.exe("(2>1 && 4< 5)"));
        assertFalse(BoolExpr.exe("!(2>1 && 4< 5)"));
        assertFalse(BoolExpr.exe("(2>1 && 4< 5) && false"));
        assertTrue(BoolExpr.exe("(2>1 && 4> 5) || true"));
        assertFalse(BoolExpr.exe("true && !(2>1 || 4< 5)"));
        assertTrue(BoolExpr.exe("true || (2>3 || 6< 5)"));

    }

    // 2013.05 guanjing.pangj@alibaba-inc.com case:
    public void testGuanJingCase() throws Throwable {
        Map<String, Object> context = new HashMap<String, Object>();
        context.put("$bu", "T");
        context.put("$source", "model");
        context.put("$identifier", "fake");
        context.put("$modelScore", 100);
        context.put("$punishScore", 30);
        assertTrue(BoolExpr
                .exe(context,
                        "$bu='T' && ($source='model' && $identifier='fake' && $modelScore> 90 ||$source='punish' && $identifier='fake' && $punishScore > 24)"));
    }
}

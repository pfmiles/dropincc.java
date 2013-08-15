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
package com.github.pfmiles.dropincc.testhelper;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.github.pfmiles.dropincc.Element;
import com.github.pfmiles.dropincc.Grule;
import com.github.pfmiles.dropincc.Lang;
import com.github.pfmiles.dropincc.TokenDef;
import com.github.pfmiles.dropincc.impl.AnalyzedLang;
import com.github.pfmiles.dropincc.impl.GruleType;
import com.github.pfmiles.dropincc.impl.SpecialType;
import com.github.pfmiles.dropincc.impl.TokenType;
import com.github.pfmiles.dropincc.impl.TypeMappingParam;
import com.github.pfmiles.dropincc.impl.kleene.AbstractKleeneNode;
import com.github.pfmiles.dropincc.impl.kleene.KleeneCompiler;
import com.github.pfmiles.dropincc.impl.kleene.KleeneType;
import com.github.pfmiles.dropincc.impl.syntactical.ParserCompiler;

/**
 * Some helper methods to help tests.
 * 
 * @author pf-miles
 * 
 */
public class TestHelper {
    /**
     * invoke non-public method of a class
     * 
     * @param obj
     * @param mName
     * @param args
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T priIvk(Object obj, String mName, Object... args) {
        Class<?>[] parameterTypes = null;
        if (args != null && args.length > 0) {
            List<Class<?>> ats = new ArrayList<Class<?>>();
            for (Object arg : args) {
                ats.add(arg.getClass());
            }
            parameterTypes = ats.toArray(new Class<?>[ats.size()]);
        }
        Method m;
        try {
            m = obj.getClass().getDeclaredMethod(mName, parameterTypes);
            m.setAccessible(true);
            return (T) m.invoke(obj, args);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e.getCause().getMessage(), e.getCause());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * fetch non-public field value
     * 
     * @param obj
     * @param fieldName
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T priField(Object obj, String fieldName) {
        if (obj == null)
            return null;
        Class<?> cls = obj.getClass();
        Field f;
        try {
            f = cls.getDeclaredField(fieldName);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if (f == null)
            return null;
        f.setAccessible(true);
        try {
            return (T) f.get(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * creates a temporarily 'AnalyzedLang' object for test usage
     * 
     * @param lang
     * @return
     */
    @SuppressWarnings("unchecked")
    public static AnalyzedLangForTest resolveAnalyzedLangForTest(Lang lang) {
        AnalyzedLangForTest ret = new AnalyzedLangForTest();
        AnalyzedLang al = new AnalyzedLang("test", (List<TokenDef>) priField(lang, "tokens"), (List<Grule>) priField(lang, "grules"),
                (Boolean) priField(lang, "whiteSpaceSensitive"));
        TypeMappingParam typeMappingParam = new TypeMappingParam((Map<TokenDef, TokenType>) TestHelper.priField(al, "tokenTypeMapping"),
                (Map<Grule, GruleType>) TestHelper.priField(al, "gruleTypeMapping"), (Map<Element, SpecialType>) TestHelper.priField(al,
                        "specialTypeMapping"), (Map<AbstractKleeneNode, KleeneType>) TestHelper.priField(al, "kleeneTypeMapping"));
        ret.kleeneTypeToNode = KleeneCompiler.buildKleeneTypeToNode(typeMappingParam);
        ret.ruleTypeToAlts = ParserCompiler.buildRuleTypeToAlts(typeMappingParam);
        return ret;
    }

    /**
     * generates a random integer value from 0 to (range-1)
     * 
     * @param range
     * @return
     */
    public static int randInt(int range) {
        return (int) (Math.random() * range);
    }

}

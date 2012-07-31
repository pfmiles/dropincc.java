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
package com.github.pfmiles.dropincc.impl.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.github.pfmiles.dropincc.CC;
import com.github.pfmiles.dropincc.DropinccException;
import com.github.pfmiles.dropincc.Element;
import com.github.pfmiles.dropincc.Grule;
import com.github.pfmiles.dropincc.TokenDef;
import com.github.pfmiles.dropincc.impl.AndSubRule;
import com.github.pfmiles.dropincc.impl.ConstructingGrule;
import com.github.pfmiles.dropincc.impl.EleType;
import com.github.pfmiles.dropincc.impl.OrSubRule;
import com.github.pfmiles.dropincc.impl.TypeMappingParam;
import com.github.pfmiles.dropincc.impl.kleene.AbstractKleeneNode;
import com.github.pfmiles.dropincc.impl.lexical.InstantTokenDef;

/**
 * @author pf-miles
 * 
 */
public class Util {
    private Util() {
    }

    /**
     * test if the str is null or 0-length string
     * 
     * @param regexp
     * @return
     */
    public static boolean isEmpty(String str) {
        return null == str || "".equals(str);
    }

    /**
     * Invoked at every point which possibly defining elements match
     * sequence(Lang.addGrammarRule, Grule.fillGrammarRule, new KleeneXxxNode()
     * etc.), filtering any special concerned element type. Ensure that the
     * resulting elements are legal for grammar rule production element
     * sequence.
     * 
     * @param eles
     * @param lang
     * @return
     */
    public static Element[] filterProductionEles(Object[] eles) {
        if (eles == null)
            return null;
        Element[] eleNoCon = new Element[eles.length];
        for (int i = 0; i < eles.length; i++) {
            Object ele = eles[i];
            if (ele instanceof ConstructingGrule) {
                // filtering out ConstructingGrule elements
                eleNoCon[i] = ((ConstructingGrule) ele).getGrule();
            } else if (ele.equals(CC.NOTHING)) {
                // check for empty alt
                if (eles.length != 1)
                    throw new DropinccException(
                            "Alternative production which contains 'CC.NOTHING' could not have any other elements(Because this is an 'empty' alternative production.)");
            } else if (ele instanceof Element) {
                // any other Element type
                eleNoCon[i] = (Element) ele;
            } else if (ele instanceof String) {
                // tokenDef on-the-fly
                eleNoCon[i] = new InstantTokenDef((String) ele);
            } else {
                // report error
                throw new DropinccException("Illegal production element type: " + ele);
            }
        }
        return eleNoCon;
    }

    // in order not to polluting Element.java's API, the resolveEleType method
    // defined here using if-else to determine which type should be returned.
    public static EleType resolveEleType(Element e, TypeMappingParam param) {
        Class<?> eleCls = e.getClass();
        EleType t = null;
        if (AndSubRule.class.isAssignableFrom(eleCls)) {
            throw new DropinccException("AndSubRule shouldn't exist when resolving element types, it should be already rewrited in prior steps.");
        } else if (ConstructingGrule.class.isAssignableFrom(eleCls)) {
            throw new DropinccException("There must be something wrong, ConstructingGrule shouldn't appear here.");
        } else if (Grule.class.isAssignableFrom(eleCls)) {
            t = param.getGruleTypeMapping().get(e);
        } else if (OrSubRule.class.isAssignableFrom(eleCls)) {
            throw new DropinccException("OrSubRule shouldn't exist when resolving element types, it should be already rewrited in prior steps.");
        } else if (TokenDef.class.isAssignableFrom(eleCls)) {
            t = param.getTokenTypeMapping().get(e);
        } else if (e.equals(CC.NOTHING)) {
            t = param.getSpecialTypeMapping().get(e);
        } else if (AbstractKleeneNode.class.isAssignableFrom(eleCls)) {
            t = param.getKleeneTypeMapping().get(e);
        } else {
            throw new DropinccException("Unhandled element: " + e);
        }
        if (t == null)
            throw new DropinccException("Could not resolve element type for element: " + e + ", is this element defined in a proper manner?");
        return t;
    }

    /**
     * return an action(anonymous inner class), for debug
     * 
     * @param action
     * @return
     */
    public static String resolveActionName(Object action) {
        if (action == null)
            return null;
        Class<?> cls = action.getClass();
        String name = cls.getSimpleName();
        if (name != null && !"".equals(name))
            return name;
        name = cls.getName();
        return name.substring(name.lastIndexOf(".") + 1);
    }

    /**
     * 'reverse' a map's key and values, beware this may cause losing some
     * entries(values may have duplications)
     * 
     * @param gruleTypeMapping
     * @return
     */
    public static <K, V> Map<V, K> reverseMap(Map<K, V> map) {
        Map<V, K> ret = new HashMap<V, K>();
        for (Map.Entry<K, V> e : map.entrySet()) {
            ret.put(e.getValue(), e.getKey());
        }
        return ret;
    }

    /**
     * Dump a string representation for cycled traced path
     * 
     * @param path
     *            the tracing path
     * @param t
     *            the node which is 're-visited' now
     * @return string representation of the circle in path
     */
    public static String dumpCirclePath(SetStack<?> path, Object t) {
        StringBuilder sb = new StringBuilder();
        Iterator<?> nodes = path.iterator();
        Object n = nodes.next();
        while (nodes.hasNext() && !t.equals(n)) {
            n = nodes.next();
        }
        sb.append(n.toString());
        while (nodes.hasNext())
            sb.append(" -> ").append(nodes.next().toString());
        sb.append(" -> ").append(t.toString());
        return sb.toString();
    }

    public static String showHiddenChars(String str) {
        if (str == null)
            return null;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (Character.isISOControl(c) || Character.isWhitespace(c)) {
                sb.append("\\").append((int) c);
            } else if ('\\' == c) {
                sb.append("\\\\");
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * find the min integer
     * 
     * @param ints
     * @return
     */
    public static int minInt(Collection<Integer> ints) {
        int ret = Integer.MAX_VALUE;
        for (int i : ints)
            if (i < ret)
                ret = i;
        return ret;
    }

    /**
     * Join all elements as a whole string, using 'joint' to separate each
     * element
     * 
     * @param joint
     * @param elements
     * @return
     */
    public static String join(String joint, List<String> elements) {
        StringBuilder sb = new StringBuilder();
        for (String ele : elements) {
            if (sb.length() != 0)
                sb.append(joint);
            sb.append(ele);
        }
        return sb.toString();
    }

}

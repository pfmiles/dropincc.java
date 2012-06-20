package com.github.pfmiles.dropincc.impl.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.github.pfmiles.dropincc.Action;
import com.github.pfmiles.dropincc.CC;
import com.github.pfmiles.dropincc.DropinccException;
import com.github.pfmiles.dropincc.Element;
import com.github.pfmiles.dropincc.Grule;
import com.github.pfmiles.dropincc.Token;
import com.github.pfmiles.dropincc.impl.AndSubRule;
import com.github.pfmiles.dropincc.impl.ConstructingGrule;
import com.github.pfmiles.dropincc.impl.EleType;
import com.github.pfmiles.dropincc.impl.OrSubRule;
import com.github.pfmiles.dropincc.impl.TypeMappingParam;
import com.github.pfmiles.dropincc.impl.kleene.AbstractKleeneNode;

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
     * accepts an element array contains ConstructingGrule possibly, returns an
     * array with no containing ConstructingGrule.
     * 
     * @param eles
     * @return
     */
    public static Element[] filterConstructingGrules(Element[] eles) {
        if (eles == null)
            return null;
        Element[] eleNoCon = new Element[eles.length];
        for (int i = 0; i < eles.length; i++) {
            if (eles[i].getClass().equals(ConstructingGrule.class)) {
                eleNoCon[i] = ((ConstructingGrule) eles[i]).getGrule();
            } else {
                eleNoCon[i] = eles[i];
            }
        }
        return eleNoCon;
    }

    // in order not to polluting Element.java's API, the resolveEleType method
    // defined here using if-else to determine which type should be returned.
    public static EleType resolveEleType(Element e, TypeMappingParam param) {
        Class<?> eleCls = e.getClass();
        if (AndSubRule.class.isAssignableFrom(eleCls)) {
            throw new DropinccException("AndSubRule shouldn't exist when resolving element types, it should be already rewrited in prior steps.");
        } else if (ConstructingGrule.class.isAssignableFrom(eleCls)) {
            throw new DropinccException("There must be something wrong, ConstructingGrule shouldn't appear here.");
        } else if (Grule.class.isAssignableFrom(eleCls)) {
            return param.getGruleTypeMapping().get(e);
        } else if (OrSubRule.class.isAssignableFrom(eleCls)) {
            throw new DropinccException("OrSubRule shouldn't exist when resolving element types, it should be already rewrited in prior steps.");
        } else if (Token.class.isAssignableFrom(eleCls)) {
            return param.getTokenTypeMapping().get(e);
        } else if (e.equals(CC.NOTHING)) {
            return param.getSpecialTypeMapping().get(e);
        } else if (AbstractKleeneNode.class.isAssignableFrom(eleCls)) {
            return param.getKleeneTypeMapping().get(e);
        } else {
            throw new DropinccException("Unhandled element: " + e);
        }
    }

    /**
     * return an action(anonymous inner class), for debug
     * 
     * @param action
     * @return
     */
    public static String resolveActionName(Action action) {
        if (action == null)
            return null;
        Class<? extends Action> cls = action.getClass();
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
}
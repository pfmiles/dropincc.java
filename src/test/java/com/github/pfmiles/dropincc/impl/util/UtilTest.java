package com.github.pfmiles.dropincc.impl.util;

import junit.framework.TestCase;

import com.github.pfmiles.dropincc.Action;

/**
 * @author pf-miles
 * 
 */
public class UtilTest extends TestCase {
    public void testResolveActionName() {
        Action a = new Action() {

            public Object act(Object... params) {
                return null;
            }
        };
        assertTrue("UtilTest$1".equals(Util.resolveActionName(a)));
    }

    public void testDumpCirclePath() {
        SetStack<String> s = new SetStack<String>();
        s.push("1");
        s.push("2");
        s.push("3");
        assertTrue("1 -> 2 -> 3 -> 1".equals(Util.dumpCirclePath(s, "1")));

        s = new SetStack<String>();
        s.push("1");
        assertTrue("1 -> 1".equals(Util.dumpCirclePath(s, "1")));
    }
}

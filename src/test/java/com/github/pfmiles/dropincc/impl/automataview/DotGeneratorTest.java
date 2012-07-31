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
package com.github.pfmiles.dropincc.impl.automataview;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import com.github.pfmiles.dropincc.impl.util.Pair;

/**
 * @author pf-miles
 * 
 */
public class DotGeneratorTest extends TestCase {

    private static class DummyState implements GeneratingState {
        private String id;
        private List<Pair<String, GeneratingState>> trans = new ArrayList<Pair<String, GeneratingState>>();
        private boolean _final;

        private DummyState(String id, boolean f) {
            this.id = id;
            this._final = f;
        }

        private DummyState(String id, List<Pair<String, GeneratingState>> trans, boolean f) {
            this.id = id;
            this.trans = trans;
            this._final = f;
        }

        public String getId() {
            return this.id;
        }

        public List<Pair<String, GeneratingState>> getTransitions() {
            return this.trans;
        }

        public boolean isFinal() {
            return this._final;
        }

        public void addTrans(String edge, GeneratingState state) {
            this.trans.add(new Pair<String, GeneratingState>(edge, state));
        }
    }

    public void testToDotString() {
        List<GeneratingState> states = new ArrayList<GeneratingState>();
        List<Pair<String, GeneratingState>> trans1 = new ArrayList<Pair<String, GeneratingState>>();
        DummyState s2 = new DummyState("p2", false);
        GeneratingState s3 = new DummyState("p3", true);
        s2.addTrans("b", s3);
        trans1.add(new Pair<String, GeneratingState>("a", s2));
        GeneratingState s1 = new DummyState("p1", trans1, false);
        states.add(s1);
        states.add(s2);
        states.add(s3);
        DotGenerator dotGen = new DotGenerator(states);
        String dotStr = dotGen.toDotString("test", 50, 50);
        // System.out.println(dotStr);
        assertTrue(dotStr != null);
    }
}

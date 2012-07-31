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
package com.github.pfmiles.dropincc.impl.llstar;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;

import com.github.pfmiles.dropincc.DropinccException;
import com.github.pfmiles.dropincc.impl.util.Pair;

/**
 * CallStack needed in 'closure' operation.
 * 
 * @author pf-miles
 * 
 */
public class CallStack implements Cloneable {

    private Deque<AtnState> stack = new ArrayDeque<AtnState>();

    public CallStack clone() {
        CallStack cp = null;
        try {
            cp = (CallStack) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new DropinccException(e);
        }
        cp.stack = new ArrayDeque<AtnState>(this.stack);
        return cp;
    }

    /**
     * get occurance of the specified AtnState in the stack, returned as the
     * 'recurse depth'
     * 
     * @param s
     * @return
     */
    public int computeRecurseDepth(AtnState s) {
        int ret = 0;
        for (AtnState ss : this.stack) {
            if (ss.equals(s))
                ret++;
        }
        return ret;
    }

    /**
     * copy this stack, and pop the copy, return the state popped out and the
     * copy
     * 
     * @return
     */
    public Pair<AtnState, CallStack> copyAndPop() {
        CallStack cp = this.clone();
        AtnState s = cp.pop();
        return new Pair<AtnState, CallStack>(s, cp);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (AtnState s : this.stack) {
            if (sb.length() != 1)
                sb.append(", ");
            sb.append(s.getName());
        }
        sb.append("]");
        return sb.toString();
    }

    /**
     * push the state onto the stack
     * 
     * @param s
     *            the return state to push when invoking sub ATN network of
     *            another grammar rule
     */
    public void push(AtnState s) {
        this.stack.push(s);
    }

    /**
     * pop out the AtnState on top of this stack
     * 
     * @return
     */
    public AtnState pop() {
        return this.stack.pop();
    }

    public int hashCode() {
        // well, it's too hard to implement a proper hashCode method for
        // CallStack, but return '0' always make sense...In fact, there is
        // little need to use CallStack as key of a hashMap
        return 0;
    }

    // equals method defined according to the definition described in the paper
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        CallStack other = (CallStack) obj;
        if (this.stack == null) {
            if (other.stack == null) {
                return true;
            } else {
                return false;
            }
        } else {
            if (other.stack == null) {
                return false;
            } else {
                // either of them empty, returns true
                if (this.stack.isEmpty() || other.stack.isEmpty())
                    return true;
                // if two stacks are equal or one is suffix of the other,
                // returns true
                Iterator<AtnState> iter1 = this.stack.iterator();
                Iterator<AtnState> iter2 = other.stack.iterator();
                while (iter1.hasNext() && iter2.hasNext()) {
                    if (!iter1.next().equals(iter2.next()))
                        return false;
                }
                return true;
            }
        }
    }

    /**
     * Test if this callStack empty
     * 
     * @return
     */
    public boolean isEmpty() {
        return this.stack.isEmpty();
    }

}

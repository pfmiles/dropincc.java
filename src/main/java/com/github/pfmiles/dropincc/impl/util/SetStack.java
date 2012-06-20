package com.github.pfmiles.dropincc.impl.util;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedHashSet;

/**
 * A data structure which have both stack & set features, especially useful to
 * detect 'circles' in trees or graphs.
 * 
 * @author pf-miles
 * 
 */
public class SetStack<E> extends LinkedHashSet<E> {
    private static final long serialVersionUID = -3159906489148656808L;
    private Deque<E> innerStack = new ArrayDeque<E>();

    public E push(E item) {
        if (super.contains(item))
            return item;
        super.add(item);
        this.innerStack.push(item);
        return item;
    }

    public E pop() {
        E i = this.innerStack.pop();
        super.remove(i);
        return i;
    }

    public E peek() {
        return this.innerStack.peek();
    }
}

package com.github.pfmiles.dropincc;

/**
 * Action of an alternative. It is passed to a grammar's alternative when
 * building your grammar rules.It has no explicit implementation.Just create an
 * anonymous inner class object to capture any variable of your context and pass
 * the resulting action object to the alternative.(a.k.a as a 'Closure')
 * 
 * @author pf-miles
 * 
 */
public interface Action {
    /**
     * code you would like to execute when corresponding alternative matches.
     * 
     * @param matched
     *            value(s) resolved at runtime, passed in as an object(or object
     *            array when matched multiple objects), has the same order of
     *            your grammar elements stretched in the corresponding
     *            alternative, for example, if your corresponding alternative
     *            is:
     * 
     *            <pre>
     *            	MUL.or(DIV), term
     * </pre>
     * 
     *            the values in 'matched' are: [valueMatchedBy'MUL.or(DIV)',
     *            returnValueOf'term'rule] And if the alternative is:
     * 
     *            <pre>
     * A
     * </pre>
     * 
     *            (note: 'A' is a non-terminal), then your value in 'matched' is
     *            a single object returned by 'A' rule.
     * @return
     */
    public Object act(Object matched);
}

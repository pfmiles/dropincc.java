package com.github.pfmiles.dropincc;

/**
 * @author pf-miles
 * 
 */
public interface ParamedAction {
    /**
     * The same as the 'act' metod in com.github.pfmiles.dropincc.Action
     * interface, except that there is an additional 'arg' argument which is
     * pass in from outside when parsing
     * 
     * @param arg
     * @param matched
     * @see com.github.pfmiles.dropincc.Action#act(Object)
     * @return
     */
    public <T> Object act(T arg, Object matched);
}

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
package com.github.pfmiles.dropincc.impl.hotcompile;

/**
 * @author pf-miles
 * 
 */
public class CompilationResult {

    private boolean succeed;
    private String errMsg;

    private Class<?> cls;
    private ClassLoader loader;

    /**
     * Construct a failed compilation result
     * 
     * @param suceed
     * @param errMsg
     */
    public CompilationResult(String errMsg) {
        this.succeed = false;
        this.errMsg = errMsg;
    }

    /**
     * Construct a success compilation result
     * 
     * @param succeed
     * @param cls
     * @param loader
     */
    public CompilationResult(Class<?> cls, HotCompileClassLoader loader) {
        this.succeed = true;
        this.cls = cls;
        this.loader = loader;
    }

    public boolean isSucceed() {
        return succeed;
    }

    public String getErrMsg() {
        return errMsg;
    }

    @SuppressWarnings("unchecked")
    public <T> Class<T> getCls() {
        return (Class<T>) cls;
    }

    public ClassLoader getLoader() {
        return loader;
    }
}

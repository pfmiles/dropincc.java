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

import java.util.Map;

/**
 * Load hot compiled code from default directory.
 * 
 * @author pf-miles
 * 
 */
public class HotCompileClassLoader extends ClassLoader {

    private Map<String, JavaMemCls> inMemCls;

    public HotCompileClassLoader(ClassLoader parent, Map<String, JavaMemCls> clses) {
        super(parent);
        this.inMemCls = clses;
    }

    protected Class<?> findClass(String name) throws ClassNotFoundException {
        byte[] b = this.inMemCls.get(name).getClsBytes();
        return defineClass(name, b, 0, b.length);
    }

}

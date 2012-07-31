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

import java.io.IOException;
import java.net.URI;

import javax.tools.SimpleJavaFileObject;

/**
 * @author pf-miles
 * 
 */
public class JavaStringSource extends SimpleJavaFileObject {

    private String name;
    // source code
    private String source;

    protected JavaStringSource(String name, String source) {
        super(URI.create("string:///" + name.replaceAll("\\.", "/") + Kind.SOURCE.extension), Kind.SOURCE);
        this.name = name;
        this.source = source;
    }

    public CharSequence getCharContent(boolean ignoreEncodingErrors) throws IOException {
        return this.source;
    }

    public String getName() {
        return name;
    }

    public String getSource() {
        return source;
    }

}

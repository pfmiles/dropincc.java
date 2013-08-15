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
package com.github.pfmiles.dropincc.impl.syntactical.codegen;

import java.text.MessageFormat;

/**
 * @author pf-miles
 * 
 */
public class TestTemplate extends CodeGen {

    @SuppressWarnings("unchecked")
    public String render(CodeGenContext context) {
        return MessageFormat.format(getTemplate("testTemp.dt", TestTemplate.class), "hello", "world");
    }

}

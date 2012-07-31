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

/**
 * @author pf-miles
 * 
 */
public class ParserClsTemplate extends CodeGen {

    @SuppressWarnings("unchecked")
    public String render(CodeGenContext context) {
        return this.getTemplate("parserCls.dt")
                .format(new String[] { "className", "tokenTypes", "alts' actions", "preds", "startRule", "ruleMethods", "alts' predicting methods",
                        "kleene predicting methods" });
    }

}

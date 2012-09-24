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
package com.github.pfmiles.dropincc.template.impl.ast;

import com.github.pfmiles.dropincc.template.impl.DtVisitor;

/**
 * A plain text block in a template.
 * 
 * @author pf-miles
 * 
 */
public class TextNode implements RenderableNode<Object> {

    public <T> T accept(DtVisitor visitor, Object param) {
        return visitor.visitText(this, param);
    }

}

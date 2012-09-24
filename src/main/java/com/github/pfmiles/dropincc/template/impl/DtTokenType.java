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
package com.github.pfmiles.dropincc.template.impl;

/**
 * @author pf-miles
 * 
 */
public enum DtTokenType {
    ESCAPED(1), BACK_SLASH(2), IF(3), LEFT_PAREN(4), RIGHT_PAREN(5), ELSE_IF(6), ELSE(7), END(8), FOREACH(9), REF(10), IN(13), RANGE(14), OR(15), AND(
            16), NOT(17), EQ(18), NUM(19), STR(21), WHITE_SPACE(22), PLAIN_TXT(23), EOF(-1);

    private int groupNum;

    private DtTokenType(int groupNum) {
        this.groupNum = groupNum;
    }

    public int getGroupNum() {
        return groupNum;
    }

}

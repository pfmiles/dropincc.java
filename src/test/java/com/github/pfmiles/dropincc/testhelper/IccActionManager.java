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
package com.github.pfmiles.dropincc.testhelper;

import java.util.ArrayList;
import java.util.List;

/**
 * @author pf-miles
 * 
 */
public class IccActionManager {
    private List<IvkCountCheckAction> actions = new ArrayList<IvkCountCheckAction>();

    public IvkCountCheckAction newCheck(int ivkTime, int matched) {
        IvkCountCheckAction check = new IvkCountCheckAction(ivkTime, matched);
        this.actions.add(check);
        return check;
    }

    public void checkFinalCounts() {
        for (IvkCountCheckAction a : this.actions) {
            a.checkFinalCount();
        }
    }
}

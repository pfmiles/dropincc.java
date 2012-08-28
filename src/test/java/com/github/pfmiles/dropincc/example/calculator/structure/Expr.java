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
package com.github.pfmiles.dropincc.example.calculator.structure;

import java.util.ArrayList;
import java.util.List;

import com.github.pfmiles.dropincc.impl.util.Pair;
import com.github.pfmiles.dropincc.testhelper.TestHelper;

/**
 * Used in random code generation for testing calculator.
 * 
 * @author pf-miles
 * 
 */
public class Expr {
    private List<Addend> as;

    public List<Addend> getAs() {
        return as;
    }

    public void setAs(List<Addend> as) {
        this.as = as;
    }

    public Pair<String, Double> randomCodeAndResult() {
        int asCount = TestHelper.randInt(5) + 1;
        this.as = new ArrayList<Addend>();
        for (int i = 0; i < asCount; i++)
            as.add(new Addend());
        Double exp = 0d;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < as.size(); i++) {
            Addend a = as.get(i);
            Pair<String, Double> codeAndResult = a.randomCodeAndResult();
            if (i == 0) {
                exp = codeAndResult.getRight();
                sb.append(codeAndResult.getLeft());
            } else {
                if ("+".equals(a.getOp())) {
                    exp += codeAndResult.getRight();
                    sb.append("+").append(codeAndResult.getLeft());
                } else {
                    exp -= codeAndResult.getRight();
                    sb.append("-").append(codeAndResult.getLeft());
                }
            }
        }
        return new Pair<String, Double>(sb.toString(), exp);
    }
}

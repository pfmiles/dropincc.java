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
 * @author pf-miles
 * 
 */
public class Addend {
    private String op;
    private List<Factor> fs;

    public List<Factor> getFs() {
        return fs;
    }

    public void setFs(List<Factor> fs) {
        this.fs = fs;
    }

    public String getOp() {
        return op;
    }

    public void setOp(String op) {
        this.op = op;
    }

    public Pair<String, Double> randomCodeAndResult() {
        int fCount = TestHelper.randInt(5) + 1;
        this.fs = new ArrayList<Factor>();
        for (int i = 0; i < fCount; i++)
            fs.add(new Factor());

        switch (TestHelper.randInt(2)) {
        case 0:
            this.op = "+";
            break;
        case 1:
            this.op = "-";
            break;
        default:
            throw new RuntimeException("Impossible!");
        }
        StringBuilder sb = new StringBuilder();
        Double exp = 0d;
        for (int i = 0; i < fs.size(); i++) {
            Factor f = fs.get(i);
            Pair<String, Double> codeAndResult = f.randomCodeAndResult();
            if (i == 0) {
                exp = codeAndResult.getRight();
                sb.append(codeAndResult.getLeft());
            } else {
                if ("*".equals(f.getOp())) {
                    exp *= codeAndResult.getRight();
                    sb.append("*").append(codeAndResult.getLeft());
                } else {
                    exp /= codeAndResult.getRight();
                    sb.append("/").append(codeAndResult.getLeft());
                }
            }
        }
        return new Pair<String, Double>(sb.toString(), exp);
    }
}

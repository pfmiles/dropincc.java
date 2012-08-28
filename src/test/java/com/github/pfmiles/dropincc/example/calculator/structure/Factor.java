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

import java.math.BigDecimal;
import java.math.RoundingMode;

import com.github.pfmiles.dropincc.impl.util.Pair;
import com.github.pfmiles.dropincc.testhelper.TestHelper;

/**
 * @author pf-miles
 * 
 */
public class Factor {

    private String op;
    private Expr expr;
    private double digit;

    public Expr getExpr() {
        return expr;
    }

    public void setExpr(Expr expr) {
        this.expr = expr;
    }

    public double getDigit() {
        return digit;
    }

    public void setDigit(double digit) {
        this.digit = digit;
    }

    public String getOp() {
        return op;
    }

    public void setOp(String op) {
        this.op = op;
    }

    public Pair<String, Double> randomCodeAndResult() {
        switch (TestHelper.randInt(2)) {
        case 0:
            this.op = "*";
            break;
        case 1:
            this.op = "/";
            break;
        default:
            throw new RuntimeException("Impossible!");
        }
        int expOrDigit = TestHelper.randInt(20);
        if (expOrDigit == 19) {
            // exp
            this.expr = new Expr();
            Pair<String, Double> codeAndExp = this.expr.randomCodeAndResult();
            return new Pair<String, Double>("(" + codeAndExp.getLeft() + ")", codeAndExp.getRight());
        } else {
            // digit
            Double rst = new BigDecimal(Math.random() * 10 + 1).setScale(2, RoundingMode.HALF_UP).doubleValue();
            return new Pair<String, Double>(String.valueOf(rst), rst);

        }
    }
}

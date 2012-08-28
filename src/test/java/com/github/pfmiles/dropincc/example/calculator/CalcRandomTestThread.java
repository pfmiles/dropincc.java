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
package com.github.pfmiles.dropincc.example.calculator;

import java.util.concurrent.CountDownLatch;

import com.github.pfmiles.dropincc.example.calculator.structure.Expr;
import com.github.pfmiles.dropincc.impl.util.Pair;

/**
 * @author pf-miles
 * 
 */
public class CalcRandomTestThread implements Runnable {
    private int times = 10;
    private CountDownLatch startLatch;
    private CountDownLatch endLatch;

    public CalcRandomTestThread(int times, CountDownLatch startLatch, CountDownLatch endLatch) {
        this.times = times;
        this.startLatch = startLatch;
        this.endLatch = endLatch;
    }

    public void run() {
        try {
            startLatch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        try {
            for (int i = 0; i < times; i++) {
                doRandomTest();
            }
        } finally {
            endLatch.countDown();
        }
    }

    private void doRandomTest() {
        Expr e = new Expr();
        Pair<String, Double> p = e.randomCodeAndResult();
        String code = p.getLeft();
        Double exp = p.getRight();
        Double actual = Calculator.compute(code);
        // System.out.println("Code: " + code + ", exp: " + exp + ", actual: " +
        // actual);
        if (!exp.equals(actual)) {
            throw new RuntimeException("Computation incorrect. Expected: " + exp + ", actual: " + actual + ", expr: " + code);
        }
    }
}

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

import junit.framework.TestCase;

/**
 * Random test for Calculator.java
 * 
 * @author pf-miles
 * 
 */
public class CalcRandomTest extends TestCase {
    /**
     * Random expression generation and test in a multi-thread situation.
     * 
     * @param threads
     * @param countPerThread
     */
    public static void testRandom(int threads, int countPerThread) {
        CountDownLatch startLatch = new CountDownLatch(threads);
        CountDownLatch endLatch = new CountDownLatch(threads);
        for (int i = 0; i < threads; i++) {
            new Thread(new CalcRandomTestThread(countPerThread, startLatch, endLatch)).start();
            startLatch.countDown();
        }
        long time = System.currentTimeMillis();
        try {
            endLatch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Random code generation and test with " + threads + " threads, " + countPerThread
                + " generates and tests per thread completed in milliseconds: " + (System.currentTimeMillis() - time));
    }

    public void testEval() {
        // pre-load Calculator class
        System.out.println("Preload Calculator class, test compute: " + Calculator.compute("1+1"));
        testRandom(20, 100);
        // pass if no runtime exceptions thrown
        assertTrue(true);
    }
}

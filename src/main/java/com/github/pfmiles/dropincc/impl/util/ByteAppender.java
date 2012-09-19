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
package com.github.pfmiles.dropincc.impl.util;

import java.util.Arrays;

/**
 * 
 * Append bytes and manage buffer automatically.
 * 
 * @author pf-miles
 * 
 */
public class ByteAppender {
    // the byte buffer
    private byte[] buf = new byte[1024];
    // num of valid bytes in buffer
    private int count = 0;

    public void append(byte[] b, int off, int len) {
        if ((off < 0) || (off >= b.length) || (len < 0) || ((off + len) > b.length) || ((off + len) < 0)) {
            throw new IndexOutOfBoundsException();
        } else if (len == 0) {
            return;
        }
        int newcount = count + len;
        if (newcount > buf.length) {
            buf = Arrays.copyOf(buf, Math.max(buf.length << 1, newcount));
        }
        System.arraycopy(b, off, buf, count, len);
        count = newcount;
    }

    public void append(byte[] b) {
        this.append(b, 0, b.length);
    }

    public byte[] toByteArray() {
        byte[] ret = new byte[count];
        System.arraycopy(buf, 0, ret, 0, count);
        return ret;
    }
}

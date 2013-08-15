package com.github.pfmiles.dropincc.impl.hotcompile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;

import javax.tools.SimpleJavaFileObject;

/**
 * Represents compiled java class files in the memory.
 * 
 * @author pf-miles
 * 
 */
public class JavaMemCls extends SimpleJavaFileObject {

    private ByteArrayOutputStream bos;

    protected JavaMemCls(String name) {
        super(URI.create("string:///" + name.replaceAll("\\.", "/") + Kind.CLASS.extension), Kind.CLASS);
        this.bos = new ByteArrayOutputStream();
    }

    public byte[] getClsBytes() {
        return this.bos.toByteArray();
    }

    public OutputStream openOutputStream() throws IOException {
        return bos;
    }
}

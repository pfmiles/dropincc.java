package com.github.pfmiles.dropincc.impl.hotcompile;

import java.io.IOException;
import java.net.URI;

import javax.tools.SimpleJavaFileObject;

/**
 * @author pf-miles
 * 
 */
public class JavaStringSource extends SimpleJavaFileObject {

    private String name;
    // source code
    private String source;

    protected JavaStringSource(String name, String source) {
        super(URI.create("string:///" + name.replaceAll("\\.", "/") + Kind.SOURCE.extension), Kind.SOURCE);
        this.name = name;
        this.source = source;
    }

    public CharSequence getCharContent(boolean ignoreEncodingErrors) throws IOException {
        return this.source;
    }

    public String getName() {
        return name;
    }

    public String getSource() {
        return source;
    }

}

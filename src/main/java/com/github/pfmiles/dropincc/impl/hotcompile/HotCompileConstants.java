package com.github.pfmiles.dropincc.impl.hotcompile;

import java.io.File;

/**
 * @author pf-miles
 * 
 */
public interface HotCompileConstants {

    /**
     * classpath string of this java process
     */
    String CLASSPATH = System.getProperty("java.class.path");

    /**
     * hot compilation target directory
     */
    String TARGETDIR = System.getProperty("java.io.tmpdir") + File.separator + "dcHotCompile";
}

package com.github.pfmiles.dropincc.impl.hotcompile;

import java.io.IOException;
import java.util.Map;

import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.JavaFileObject.Kind;
import javax.tools.StandardJavaFileManager;

/**
 * A forwarding file manager which is used to decouple the file system
 * dependency. That is, the compilation process write compiled class files into
 * memory instead of file system.
 * 
 * @author pf-miles
 * 
 */
public class MemClsFileManager extends ForwardingJavaFileManager<StandardJavaFileManager> {

    private Map<String, JavaMemCls> destFiles;

    protected MemClsFileManager(StandardJavaFileManager fileManager, Map<String, JavaMemCls> destFiles) {
        super(fileManager);
        this.destFiles = destFiles;
    }

    public JavaFileObject getJavaFileForOutput(Location location, String className, Kind kind, FileObject sibling) throws IOException {
        if (destFiles.containsKey(className)) {
            return destFiles.get(className);
        } else {
            JavaMemCls file = new JavaMemCls(className);
            this.destFiles.put(className, file);
            return file;
        }
    }

    public void close() throws IOException {
        super.close();
        this.destFiles = null;
    }

}

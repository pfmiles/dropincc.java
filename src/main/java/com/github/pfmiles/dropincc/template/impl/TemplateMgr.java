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
package com.github.pfmiles.dropincc.template.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.github.pfmiles.dropincc.DropinccException;
import com.github.pfmiles.dropincc.impl.hotcompile.CompilationResult;
import com.github.pfmiles.dropincc.impl.hotcompile.HotCompileUtil;
import com.github.pfmiles.dropincc.impl.util.ByteAppender;

/**
 * Load & compile templates and manage template cache.
 * 
 * @author pf-miles
 * 
 */
public class TemplateMgr {

    // all compiled template cache
    private static final Map<Integer, CompiledTemplateCache> cache = new ConcurrentHashMap<Integer, CompiledTemplateCache>();

    private static final Map<Integer, Object> compileLocks = new ConcurrentHashMap<Integer, Object>();

    private static final String COMPILED_TEMP_PKG = "com.github.pfmiles.dropincc.template.impl.gen";

    // random string generator
    private static final SecureRandom random = new SecureRandom();

    /**
     * Get the running template, which is about to render string results. The
     * template is represented as a compiled java class in memory, with a
     * 'Method' for invoke to merge the template
     * 
     * @param path
     * @param cls
     * @param encoding
     * @return the template 'merge' method
     */
    public static Method getTxtTemplate(String path, Class<?> cls, String encoding) {
        encoding = encoding.toLowerCase();
        int key = resolveKey(path, cls, encoding);
        if (cache.containsKey(key)) {
            return cache.get(key).txtTemplate;
        } else {
            CompiledTemplateCache compiled = findAndCompileThenCache(key, path, cls, encoding);
            return compiled.txtTemplate;
        }
    }

    private static CompiledTemplateCache findAndCompileThenCache(int key, String path, Class<?> cls, String encoding) {
        Object lock = aquirePerTemplateCompileLock(key);
        synchronized (lock) {
            if (cache.containsKey(key)) {
                return cache.get(key);
            } else {
                String tempStr = readTemplate(path, cls, encoding);
                String javaSource = DropinTemplateCompiler.compile(tempStr);
                CompilationResult rst = HotCompileUtil.compile(resolveQualifiedName(path), javaSource);
                if (!rst.isSucceed())
                    throw new DropinccException(rst.getErrMsg());
                Method mergeMethod = resolveMergeMethod(rst.getCls());
                CompiledTemplateCache ret = new CompiledTemplateCache();
                // TODO txtTemplate only, currently
                ret.txtTemplate = mergeMethod;
                cache.put(key, ret);
                return ret;
            }
        }
    }

    // the merge method is a public method named 'merge' and have only one
    // parameter which is of type Object
    private static Method resolveMergeMethod(Class<Object> cls) {
        try {
            return cls.getMethod("merge", Object.class);
        } catch (Exception e) {
            throw new DropinccException(e);
        }
    }

    private static final Pattern validJavaFileNamePattern = Pattern.compile("[a-zA-Z_]\\w*");

    private static String resolveQualifiedName(String path) {
        String fileName = path.substring(path.lastIndexOf(File.separatorChar) + 1);
        if (fileName.contains("."))
            fileName = fileName.substring(0, fileName.indexOf("."));
        Matcher m = validJavaFileNamePattern.matcher(fileName);
        if (m.find()) {
            return COMPILED_TEMP_PKG + "." + fileName + genRandomStr();
        } else {
            return COMPILED_TEMP_PKG + "." + "Template" + genRandomStr();
        }
    }

    private static String genRandomStr() {
        return new BigInteger(130, random).toString(36);
    }

    private static String readTemplate(String path, Class<?> cls, String encoding) {
        InputStream is = cls.getResourceAsStream(path);
        ByteAppender ba = new ByteAppender();
        byte[] buf = new byte[1024];
        int read = -1;
        try {
            read = is.read(buf);
            while (read != -1) {
                ba.append(buf, 0, read);
                read = is.read(buf);
            }
            return new String(ba.toByteArray(), encoding);
        } catch (IOException e) {
            throw new DropinccException(e);
        }
    }

    private synchronized static Object aquirePerTemplateCompileLock(int key) {
        if (compileLocks.containsKey(key)) {
            return compileLocks.get(key);
        } else {
            Object ret = new Object();
            compileLocks.put(key, ret);
            return ret;
        }
    }

    // a hash code algorithm generated by eclipse...
    private static int resolveKey(String path, Class<?> cls, String encoding) {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((cls == null) ? 0 : cls.hashCode());
        result = prime * result + ((encoding == null) ? 0 : encoding.hashCode());
        result = prime * result + ((path == null) ? 0 : path.hashCode());
        return result;
    }

}

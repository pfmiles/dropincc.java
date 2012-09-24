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
package com.github.pfmiles.dropincc.template;

import java.lang.reflect.Method;
import java.nio.charset.Charset;

import com.github.pfmiles.dropincc.DropinccException;
import com.github.pfmiles.dropincc.template.impl.TemplateMgr;

/**
 * The only one entry class to help use the template engine for code generation
 * in dropincc.java. This provide all the API needed to use the template engine.
 * 
 * @author pf-miles
 * 
 */
public final class DropinTemplate {
    private DropinTemplate() {
        // non-sense to instantiate this class
    }

    /**
     * Render the specified template in the specified context.
     * 
     * @param path
     *            the relative path of the specified template
     * @param context
     *            the context, containing all data
     * @param cls
     *            the java class object used to find the template resource, the
     *            template finding behavior is consistent to
     *            'class.getResourceAsStream'
     * @return the merge result of the template and context, as a string
     */
    public static String merge(String path, Object context, Class<?> cls) {
        return merge(path, context, cls, Charset.defaultCharset().name());
    }

    /**
     * Render the specified template in the specified context.
     * 
     * @param path
     *            the relative path of the specified template
     * @param context
     *            the context, containing all data
     * @param cls
     *            the java class object used to find the template resource, the
     *            template finding behavior is consistent to
     *            'class.getResourceAsStream'
     * @return the merge result of the template and context, as a byte array,
     *         using default encoding format
     */
    public static byte[] mergeAsBytes(String path, Object context, Class<?> cls) {
        return mergeAsBytes(path, context, cls, Charset.defaultCharset().name());
    }

    /**
     * Render the specified template in the specified context. With template
     * encoding specified.
     * 
     * @param path
     *            the relative path of the specified template
     * @param context
     *            the context, containing all data
     * @param cls
     *            the java class object used to find the template resource, the
     *            template finding behavior is consistent to
     *            'class.getResourceAsStream'
     * @param encoding
     *            the encoding format to decode the template file
     * @return the merge result of the template and context, as a string
     */
    public static String merge(String path, Object context, Class<?> cls, String encoding) {
        Method temp = TemplateMgr.getTxtTemplate(path, cls, encoding);
        try {
            return (String) temp.invoke(null, context);
        } catch (Exception e) {
            throw new DropinccException(e);
        }
    }

    /**
     * Render the specified template in the specified context. With encoding
     * specified.
     * 
     * @param path
     *            the relative path of the specified template
     * @param context
     *            the context, containing all data
     * @param cls
     *            the java class object used to find the template resource, the
     *            template finding behavior is consistent to
     *            'class.getResourceAsStream'
     * @param encoding
     *            the encoding format to decode the template file and produce
     *            the resulting byte array
     * @return the merge result of the template and context, as a byte array,
     *         using the specified encoding format
     */
    public static byte[] mergeAsBytes(String path, Object context, Class<?> cls, String encoding) {
        // TODO implement in the future
        throw new UnsupportedOperationException("Currently not implemented.");
    }
}

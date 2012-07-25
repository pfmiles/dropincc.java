package com.github.pfmiles.dropincc.impl.syntactical.codegen;

import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.github.pfmiles.dropincc.DropinccException;
import com.github.pfmiles.dropincc.impl.util.ByteAppender;
import com.github.pfmiles.dropincc.impl.util.Pair;

/**
 * Common interface of all code rendering elements of a parser.
 * 
 * @author pf-miles
 * 
 */
public abstract class CodeGen {
    private static final Map<Pair<Class<? extends CodeGen>, String>, MessageFormat> cache = new ConcurrentHashMap<Pair<Class<? extends CodeGen>, String>, MessageFormat>();

    // using a message format implementation currently
    protected MessageFormat getTemplate(String name) {
        Pair<Class<? extends CodeGen>, String> k = new Pair<Class<? extends CodeGen>, String>(this.getClass(), name);
        if (cache.containsKey(k)) {
            return cache.get(k);
        } else {
            MessageFormat ret = new MessageFormat(readStrStream(this.getClass().getResourceAsStream(name)));
            cache.put(k, ret);
            return ret;
        }
    }

    private String readStrStream(InputStream stm) {
        if (stm == null)
            return null;
        ByteAppender ba = new ByteAppender();
        byte[] buf = new byte[2048];
        int count = 0;
        try {
            count = stm.read(buf);
            while (count != -1) {
                ba.write(buf, 0, count);
                count = stm.read(buf);
            }
            return new String(ba.toByteArray(), "UTF-8");
        } catch (IOException e) {
            throw new DropinccException(e);
        }
    }

    public abstract String render(CodeGenContext context);
}

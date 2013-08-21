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
package com.github.pfmiles.dropincc.impl.runtime.impl;

import java.lang.reflect.Field;
import java.util.Map;

import com.github.pfmiles.dropincc.DropinccException;
import com.github.pfmiles.dropincc.impl.syntactical.codegen.ParserCodeGenResult;

/**
 * @author pf-miles
 * 
 */
public class StatelessParserPrototype implements ParserPrototype {

    // because the parser is implemented stateless. So we can reuse the parser
    // instance over and over again by cloning it.
    private CodeParser parserPrototype;

    /**
     * Construct a stateless singleton parser prototype by parser class.
     * 
     * @param cls
     * @param parserCodeGenResult
     */
    public StatelessParserPrototype(Class<? extends Parser> cls, ParserCodeGenResult parserCodeGenResult) {
        try {
            this.parserPrototype = (CodeParser) cls.newInstance();
        } catch (Exception e) {
            throw new DropinccException(e);
        }
        // setting all compiled results
        mappingField(cls, parserPrototype, parserCodeGenResult.getFieldAltsActionMapping());
        mappingField(cls, parserPrototype, parserCodeGenResult.getFieldKleeneDfaMapping());
        mappingField(cls, parserPrototype, parserCodeGenResult.getFieldPredsMapping());
        mappingField(cls, parserPrototype, parserCodeGenResult.getFieldRuleDfaMapping());
        mappingField(cls, parserPrototype, parserCodeGenResult.getFieldTokenTypeMapping());

    }

    // setting fields' values of obj by field name to value mapping
    private void mappingField(Class<?> cls, Object obj, Map<String, ?> fieldToValue) {
        try {
            for (Map.Entry<String, ?> e : fieldToValue.entrySet()) {
                Field f = cls.getField(e.getKey());
                f.setAccessible(true);
                f.set(obj, e.getValue());
            }
        } catch (Exception ex) {
            throw new DropinccException(ex);
        }
    }

    public Parser create(Lexer lexer) {
        Parser parser = this.parserPrototype.clone();
        parser.setLexer(lexer);
        return parser;
    }

}

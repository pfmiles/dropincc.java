package com.github.pfmiles.dropincc.impl.runtime.impl;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.pfmiles.dropincc.DropinccException;
import com.github.pfmiles.dropincc.Predicate;
import com.github.pfmiles.dropincc.impl.TokenType;
import com.github.pfmiles.dropincc.impl.runtime.Parser;
import com.github.pfmiles.dropincc.impl.syntactical.codegen.ParserCodeGenResult;

/**
 * @author pf-miles
 * 
 */
public class StatelessParserPrototype implements ParserPrototype {

    private Class<? extends Parser> parserCls;

    private Map<String, TokenType> fieldTokenTypeMapping = new HashMap<String, TokenType>();
    private Map<String, Object> fieldAltsActionMapping = new HashMap<String, Object>();
    private Map<String, Predicate> fieldPredsMapping = new HashMap<String, Predicate>();
    private Map<String, RunningDfaState> fieldRuleDfaMapping = new HashMap<String, RunningDfaState>();
    private Map<String, RunningDfaState> fieldKleeneDfaMapping = new HashMap<String, RunningDfaState>();

    // parser fields cache
    private Map<String, Field> parserFieldsCache = new HashMap<String, Field>();

    /**
     * Construct a stateless singleton parser prototype by parser class.
     * 
     * @param cls
     * @param parserCodeGenResult
     */
    public StatelessParserPrototype(Class<? extends Parser> cls, ParserCodeGenResult parserCodeGenResult) {
        this.parserCls = cls;
        this.fieldAltsActionMapping = parserCodeGenResult.getFieldAltsActionMapping();
        this.fieldKleeneDfaMapping = parserCodeGenResult.getFieldKleeneDfaMapping();
        this.fieldPredsMapping = parserCodeGenResult.getFieldPredsMapping();
        this.fieldRuleDfaMapping = parserCodeGenResult.getFieldRuleDfaMapping();
        this.fieldTokenTypeMapping = parserCodeGenResult.getFieldTokenTypeMapping();
        // init fields cache
        List<String> allFields = new ArrayList<String>();
        allFields.addAll(this.fieldAltsActionMapping.keySet());
        allFields.addAll(this.fieldKleeneDfaMapping.keySet());
        allFields.addAll(this.fieldPredsMapping.keySet());
        allFields.addAll(this.fieldRuleDfaMapping.keySet());
        allFields.addAll(this.fieldTokenTypeMapping.keySet());
        for (String fname : allFields) {
            try {
                this.parserFieldsCache.put(fname, cls.getField(fname));
            } catch (Exception e) {
                throw new DropinccException(e);
            }
        }
    }

    // setting fields' values of obj by field name to value mapping
    private void mappingField(Object obj, Map<String, ?> fieldToValue) {
        for (Map.Entry<String, ?> e : fieldToValue.entrySet()) {
            Field f = this.parserFieldsCache.get(e.getKey());
            try {
                // those fields are public
                f.set(obj, e.getValue());
            } catch (Exception ex) {
                throw new DropinccException(ex);
            }
        }
    }

    public Parser create(Lexer lexer) {
        Parser parser = null;
        try {
            parser = parserCls.newInstance();
        } catch (Exception e) {
            throw new DropinccException(e);
        }
        // setting all compiled results
        mappingField(parser, this.fieldAltsActionMapping);
        mappingField(parser, this.fieldKleeneDfaMapping);
        mappingField(parser, this.fieldPredsMapping);
        mappingField(parser, this.fieldRuleDfaMapping);
        mappingField(parser, this.fieldTokenTypeMapping);

        parser.setLexer(lexer);
        return parser;
    }

}

package com.github.pfmiles.dropincc.impl.syntactical.codegen.rulemethods.code;

import java.text.MessageFormat;

import com.github.pfmiles.dropincc.DropinccException;
import com.github.pfmiles.dropincc.impl.kleene.KleeneCrossType;
import com.github.pfmiles.dropincc.impl.kleene.KleeneStarType;
import com.github.pfmiles.dropincc.impl.kleene.KleeneType;
import com.github.pfmiles.dropincc.impl.kleene.OptionalType;
import com.github.pfmiles.dropincc.impl.syntactical.codegen.CodeGen;
import com.github.pfmiles.dropincc.impl.syntactical.codegen.CodeGenContext;
import com.github.pfmiles.dropincc.impl.util.Pair;

/**
 * @author pf-miles
 * 
 */
public class KleeneEleGen extends CodeGen {

    // varName {0}
    // ksName {1}
    // elementsCode {2}
    // elementVar {3}
    private final MessageFormat ksFmt = this.getTemplate("kleeneStar.dt");
    private final MessageFormat kcFmt = this.getTemplate("kleeneCross.dt");
    private final MessageFormat opFmt = this.getTemplate("optional.dt");

    private KleeneType ele;

    public KleeneEleGen(KleeneType ele) {
        this.ele = ele;
    }

    // returns [varName, code]
    @SuppressWarnings("unchecked")
    public Pair<String, String> render(CodeGenContext context) {
        if (this.ele instanceof KleeneStarType) {
            return genKleeneStarCode((KleeneStarType) this.ele);
        } else if (this.ele instanceof KleeneCrossType) {
            return genKleeneCrossCode((KleeneCrossType) this.ele);
        } else if (this.ele instanceof OptionalType) {
            return genOptionalCode((OptionalType) this.ele);
        } else {
            throw new DropinccException("Unhandled code generation kleene node type: " + this.ele);
        }
    }

    private Pair<String, String> genOptionalCode(OptionalType ele) {
        // TODO Auto-generated method stub
        return null;
    }

    private Pair<String, String> genKleeneCrossCode(KleeneCrossType ele) {
        // TODO Auto-generated method stub
        return null;
    }

    private Pair<String, String> genKleeneStarCode(KleeneStarType ele) {
        // TODO Auto-generated method stub
        return null;
    }
}

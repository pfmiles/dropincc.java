package com.github.pfmiles.dropincc.impl.hotcompile;

/**
 * @author pf-miles
 * 
 */
public class CompilationResult {

    private boolean succeed;
    private String errMsg;

    /**
     * Construct a failed compilation result
     * 
     * @param suceed
     * @param errMsg
     */
    public CompilationResult(boolean succeed, String errMsg) {
        this.succeed = succeed;
        this.errMsg = errMsg;
    }

    public boolean isSucceed() {
        return succeed;
    }

    public String getErrMsg() {
        return errMsg;
    }
}

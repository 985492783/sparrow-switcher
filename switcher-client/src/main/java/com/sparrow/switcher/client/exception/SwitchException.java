package com.sparrow.switcher.client.exception;

/**
 * @author 985492783@qq.com
 * @date 2024/4/8 23:18
 */
public class SwitchException extends RuntimeException {

    private int errCode;
    
    private String errMsg;
    
    public SwitchException(int errCode, String errMsg) {
        this.errCode = errCode;
        this.errMsg = errMsg;
    }
    
    public int getErrCode() {
        return errCode;
    }
    
    public String getErrMsg() {
        return errMsg;
    }
    
}

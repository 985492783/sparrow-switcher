package com.sparrow.switcher.common.enums;

/**
 * @author 985492783@qq.com
 * @date 2024/4/11 14:21
 */
public enum ErrorCodeEnums {
    SYSTEM_ERROR(500),
    AUTH_FAILED(403);
    
    
    private final int code;
    ErrorCodeEnums(int code) {
        this.code = code;
    }
    
    public int getCode() {
        return code;
    }
}

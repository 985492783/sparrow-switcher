package com.sparrow.switcher.common.config;

import java.util.Properties;

/**
 * @author 985492783@qq.com
 * @date 2024/4/8 18:25
 */
public class SparrowProperties extends Properties {
    
    private final Properties properties;
    
    public SparrowProperties(Properties properties) {
        this.properties = properties;
    }
    
    public static SparrowProperties convert(Properties properties) {
        return new SparrowProperties(properties);
    }
}

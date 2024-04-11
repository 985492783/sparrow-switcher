package com.sparrow.switcher.core.entity;

import com.sparrow.switcher.common.entity.AppSwitchItem;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 985492783@qq.com
 * @date 2024/4/11 15:55
 */
public class SwitcherApplication {
    
    /**
     * Map <clazzName, Map<fieldName, Item>>
     */
    private final Map<String, Map<String, SwitchConnectionItem>> fieldMap = new ConcurrentHashMap<>();
}

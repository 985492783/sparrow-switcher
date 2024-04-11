package com.sparrow.switcher.common.request;

import com.sparrow.switcher.common.entity.AppSwitchItem;

import java.util.List;
import java.util.Map;

/**
 * @author 985492783@qq.com
 * @date 2024/4/11 14:00
 */
public class AppSwitchRequest {
    
    private String applicationName;
    
    private Map<String, List<AppSwitchItem>> fieldMap;
}

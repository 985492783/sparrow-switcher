package com.sparrow.switcher.core;

import com.sparrow.switcher.core.entity.SwitcherApplication;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 985492783@qq.com
 * @date 2024/4/8 23:02
 */
@Service
public class SwitcherCenter {
    
    /**
     * key = applicationName
     */
    private final Map<String, SwitcherApplication> applicationMap = new ConcurrentHashMap<>();
    
}

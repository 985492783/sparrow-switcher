package com.sparrow.switcher.client;

import com.alibaba.fastjson2.JSON;
import com.sparrow.switcher.client.annotation.AppSwitch;
import com.sparrow.switcher.client.exception.SwitchException;
import com.sparrow.switcher.client.utils.SwitcherFieldUtils;
import com.sparrow.switcher.common.entity.AppSwitchItem;
import com.sparrow.switcher.common.enums.ErrorCodeEnums;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * switch Manager
 *
 * @author 985492783@qq.com
 * @date 2024/4/8 23:11
 */
public class SwitcherManager {
    
    private static final Map<Class<?>, Map<String, Field>> fieldMap = new ConcurrentHashMap<>();
    
    /**
     * client register method.
     *
     * @param clazz class
     */
    public static void init(Class<?> clazz) throws SwitchException {
        if (fieldMap.containsKey(clazz)) {
            return;
        }
        Map<String, Field> map = new HashMap<>();
        Field[] fields = clazz.getDeclaredFields();
        List<AppSwitchItem> list = new ArrayList<>();
        for (Field field : fields) {
            boolean isStatic = Modifier.isStatic(field.getModifiers());
            boolean isFinal = Modifier.isFinal(field.getModifiers());
            AppSwitch appSwitch;
            if (isStatic && !isFinal && (appSwitch = field.getAnnotation(AppSwitch.class)) != null) {
                AppSwitchItem switchItem = SwitcherFieldUtils.createSwitchItem(field);
                switchItem.setDesc(appSwitch.desc());
                field.setAccessible(true);
                try {
                    Object obj = field.get(null);
                    if (obj != null) {
                        switchItem.setDefaultValue(JSON.toJSONString(obj));
                    }
                } catch (IllegalAccessException e) {
                    throw new SwitchException(ErrorCodeEnums.SYSTEM_ERROR.getCode(),
                            "field init failed: " + field.getName());
                }
                list.add(switchItem);
                map.put(field.getName(), field);
            }
        }
        fieldMap.put(clazz, map);
        //TODO 注册并将初始化值拉回来
        System.out.println(list);
        //TODO 将真实值透还给原对象 SwitcherFieldUtils.setField()
    }
}

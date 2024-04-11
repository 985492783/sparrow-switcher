package com.sparrow.switcher.client.utils;

import com.sparrow.switcher.client.exception.SwitchException;
import com.sparrow.switcher.common.entity.AppSwitchItem;
import com.sparrow.switcher.common.enums.FieldTypeEnums;

import java.lang.reflect.Field;

/**
 * @author 985492783@qq.com
 * @date 2024/4/11 14:05
 */
public class SwitcherUtils {
    
    
    public static AppSwitchItem createSwitchItem(Field field) throws SwitchException {
        AppSwitchItem switchItem = new AppSwitchItem();
        switchItem.setFieldName(field.getName());
        if (field.getType() == Integer.class || field.getType() == Long.class) {
            switchItem.setFieldType(FieldTypeEnums.INTEGER);
        } else if (field.getType() == Double.class || field.getType() == Float.class) {
            switchItem.setFieldType(FieldTypeEnums.DOUBLE);
        } else if (field.getType() == Boolean.class) {
            switchItem.setFieldType(FieldTypeEnums.BOOLEAN);
        } else if (field.getType() == String.class) {
            switchItem.setFieldType(FieldTypeEnums.STRING);
        } else {
            switchItem.setFieldType(FieldTypeEnums.JSON);
        }
        return switchItem;
    }
}

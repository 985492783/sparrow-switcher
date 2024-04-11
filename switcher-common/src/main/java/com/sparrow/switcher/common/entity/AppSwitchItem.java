package com.sparrow.switcher.common.entity;

import com.sparrow.switcher.common.enums.FieldTypeEnums;

/**
 * @author 985492783@qq.com
 * @date 2024/4/11 13:56
 */
public class AppSwitchItem {
    
    private String fieldName;
    
    private FieldTypeEnums fieldType;
    
    private String desc;
    
    private String defaultValue;
    
    public AppSwitchItem() {
    }
    
    public AppSwitchItem(String fieldName, FieldTypeEnums fieldType, String desc) {
        this.fieldName = fieldName;
        this.fieldType = fieldType;
        this.desc = desc;
    }
    
    public String getFieldName() {
        return fieldName;
    }
    
    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }
    
    public FieldTypeEnums getFieldType() {
        return fieldType;
    }
    
    public void setFieldType(FieldTypeEnums fieldType) {
        this.fieldType = fieldType;
    }
    
    public String getDesc() {
        return desc;
    }
    
    public void setDesc(String desc) {
        this.desc = desc;
    }
    
    public String getDefaultValue() {
        return defaultValue;
    }
    
    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }
    
    @Override
    public String toString() {
        return "AppSwitchItem{" + "fieldName='" + fieldName + '\'' + ", fieldType=" + fieldType + ", desc='" + desc
                + '\'' + ", defaultValue='" + defaultValue + '\'' + '}';
    }
}

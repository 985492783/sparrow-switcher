package com.sparrow.switcher.client.utils;

import com.sparrow.switcher.client.SwitcherManager;
import com.sparrow.switcher.client.annotation.AppSwitch;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 985492783@qq.com
 * @date 2024/4/11 14:27
 */
public class SwitcherFieldUtilsTest {
    
    @Test
    public void testConvert() throws NoSuchFieldException, IllegalAccessException {
        SwitcherManager.init(ConfigTest.class);
        Field fieldMap = SwitcherManager.class.getDeclaredField("fieldMap");
        fieldMap.setAccessible(true);
        Map<Class<?>, Map<String, Field>> map = (Map<Class<?>, Map<String, Field>>) fieldMap.get(null);
        Assert.assertTrue(map.containsKey(ConfigTest.class));
        Assert.assertEquals(map.get(ConfigTest.class).size(), 4);
    }
    
    @Test
    public void setValue() throws NoSuchFieldException {
        String json = "{\"key\":{\"a\":15},\"b\":{\"a\":20}}";
        Field field = ConfigTest.class.getField("map");
        SwitcherFieldUtils.setField(field, json);
        Assert.assertEquals(ConfigTest.map.get("key").a, 15);
    }
    
    
    public static class ConfigTest {
        
        @AppSwitch(desc = "test")
        public static String configName = "Hello";
        
        @AppSwitch(desc = "ss test")
        public static SS ss = new SS(5);
        @AppSwitch(desc = "null")
        public static HashMap<String, SS> map = new HashMap<String, SS>(){{
            put("a", new SS(10));
            put("b", new SS(20));
        }};
    
        @AppSwitch(desc = "array")
        public static List<SS> list = new ArrayList<SS>(){{
            add(new SS(30));
            add(new SS(40));
        }};
    }
    public static class SS {
        private int a;
    
        public SS(int a) {
            this.a = a;
        }
    
        public int getA() {
            return a;
        }
    
        public void setA(int a) {
            this.a = a;
        }
    }
}

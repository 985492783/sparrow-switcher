package com.sparrow.switcher.client.utils;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 985492783@qq.com
 * @date 2024/4/11 14:35
 */
public class JsonsTest {
    
    @Test
    public void testMapJson() throws NoSuchFieldException, IllegalAccessException {
        String json =
                "{\n" + "  \"key\": {\n" + "    \"a\": 10,\n" + "    \"b\": \"hello\"\n" + "  },\n" + "  \"value\": {\n"
                        + "    \"a\": 20,\n" + "    \"b\": \"world\"\n" + "  }\n" + "}\n";
        A a = new A();
        Field map = A.class.getField("map");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(map.getName(), JSON.parse(json));
        A b = JSON.parseObject(jsonObject.toJSONString(), A.class);
        Assert.assertEquals(b.map.get("key").a, 10);
    }
    
    @Test
    public void testJsonA() {
        String json = "{\n" + "  \"map\": {\n" + "    \"key\": {\n" + "      \"a\": 10,\n" + "      \"b\": \"hello\"\n"
                + "    },\n" + "    \"value\": {\n" + "      \"a\": 20,\n" + "      \"b\": \"world\"\n" + "    }\n"
                + "  }\n" + "  \n" + "}";
        Assert.assertEquals(JSON.parseObject(json, A.class).map.get("key").a, 10);
    }
    
    public static class A {
        
        public Map<String, B> map = new HashMap<>();
    }
    
    public static class B {
        
        private int a;
        
        private String b;
        
        public int getA() {
            return a;
        }
        
        public void setA(int a) {
            this.a = a;
        }
        
        public String getB() {
            return b;
        }
        
        public void setB(String b) {
            this.b = b;
        }
    }
}

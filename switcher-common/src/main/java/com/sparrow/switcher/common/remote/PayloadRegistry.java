/*
 * Copyright 1999-2020 Alibaba Group Holding Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sparrow.switcher.common.remote;

import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;

/**
 * payload registry,Define basic scan behavior request and response.
 *
 * @author pixel-revolve
 */

public class PayloadRegistry {
    
    private static final Map<String, Class<?>> REGISTRY_REQUEST = new HashMap<>();
    
    static boolean initialized = false;
    
    public static void init() {
        scan();
    }
    
    private static synchronized void scan() {
        if (initialized) {
            return;
        }
        ServiceLoader<Payload> payloads = ServiceLoader.load(Payload.class);
        for (Payload payload : payloads) {
            register(payload.getClass().getSimpleName(), payload.getClass());
        }
        initialized = true;
    }
    
    public static void register(String type, Class<?> clazz) {
        if (Modifier.isAbstract(clazz.getModifiers())) {
            return;
        }
        if (REGISTRY_REQUEST.containsKey(type)) {
            throw new RuntimeException(String.format("Fail to register, type:%s ,clazz:%s ", type, clazz.getName()));
        }
        REGISTRY_REQUEST.put(type, clazz);
    }
    
    public static Class<?> getClassByType(String type) {
        return REGISTRY_REQUEST.get(type);
    }
}

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

package com.sparrow.switcher.common.remote.client;

import java.lang.annotation.*;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * RpcConstants.
 *
 * @author pixel-revolve.
 */
public class RpcConstants {
    
    public static final String SPARROW_CLIENT_RPC = "sparrow.remote.client.rpc";
    
    @RpcConfigLabel
    public static final String RPC_CLIENT_TLS_ENABLE = SPARROW_CLIENT_RPC + ".tls.enable";
    
    @RpcConfigLabel
    public static final String RPC_CLIENT_TLS_PROVIDER = SPARROW_CLIENT_RPC + ".tls.provider";
    
    @RpcConfigLabel
    public static final String RPC_CLIENT_MUTUAL_AUTH = SPARROW_CLIENT_RPC + ".tls.mutualAuth";
    
    @RpcConfigLabel
    public static final String RPC_CLIENT_TLS_PROTOCOLS = SPARROW_CLIENT_RPC + ".tls.protocols";
    
    @RpcConfigLabel
    public static final String RPC_CLIENT_TLS_CIPHERS = SPARROW_CLIENT_RPC + ".tls.ciphers";
    
    @RpcConfigLabel
    public static final String RPC_CLIENT_TLS_CERT_CHAIN_PATH = SPARROW_CLIENT_RPC + ".tls.certChainFile";
    
    @RpcConfigLabel
    public static final String RPC_CLIENT_TLS_CERT_KEY = SPARROW_CLIENT_RPC + ".tls.certPrivateKey";
    
    @RpcConfigLabel
    public static final String RPC_CLIENT_TLS_TRUST_PWD = SPARROW_CLIENT_RPC + ".tls.certPrivateKeyPassword";
    
    @RpcConfigLabel
    public static final String RPC_CLIENT_TLS_TRUST_COLLECTION_CHAIN_PATH =
            SPARROW_CLIENT_RPC + ".tls.trustCollectionChainPath";
    
    @RpcConfigLabel
    public static final String RPC_CLIENT_TLS_TRUST_ALL = SPARROW_CLIENT_RPC + ".tls.trustAll";
    
    private static final Set<String> CONFIG_NAMES = new HashSet<>();
    
    @Documented
    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    protected @interface RpcConfigLabel {
    
    }
    
    static {
        Class clazz = RpcConstants.class;
        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            declaredField.setAccessible(true);
            if (declaredField.getType().equals(String.class) && null != declaredField
                    .getAnnotation(RpcConfigLabel.class)) {
                try {
                    CONFIG_NAMES.add((String) declaredField.get(null));
                } catch (IllegalAccessException ignored) {
                }
            }
        }
    }
    
    public static Set<String> getRpcParams() {
        return Collections.unmodifiableSet(CONFIG_NAMES);
    }
}

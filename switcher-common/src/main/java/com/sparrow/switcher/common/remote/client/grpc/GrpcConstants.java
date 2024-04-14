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

package com.sparrow.switcher.common.remote.client.grpc;

import java.lang.annotation.*;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * GrpcConsts.
 *
 * @author pixel-revolve
 */
public class GrpcConstants {
    
    public static final String SPARROW_SERVER_GRPC_PORT_OFFSET_KEY = "sparrow.server.grpc.port.offset";
    
    public static final String SPARROW_CLIENT_GRPC = "sparrow.remote.client.grpc";
    
    @GRpcConfigLabel
    public static final String GRPC_NAME = SPARROW_CLIENT_GRPC + ".name";
    
    @GRpcConfigLabel
    public static final String GRPC_THREADPOOL_KEEPALIVETIME = SPARROW_CLIENT_GRPC + ".pool.alive";
    
    @GRpcConfigLabel
    public static final String GRPC_THREADPOOL_CORE_SIZE = SPARROW_CLIENT_GRPC + ".pool.core.size";
    
    @GRpcConfigLabel
    public static final String GRPC_RETRY_TIMES = SPARROW_CLIENT_GRPC + ".retry.times";
    
    @GRpcConfigLabel
    public static final String GRPC_TIMEOUT_MILLS = SPARROW_CLIENT_GRPC + ".timeout";
    
    @GRpcConfigLabel
    public static final String GRPC_CONNECT_KEEP_ALIVE_TIME = SPARROW_CLIENT_GRPC + ".connect.keep.alive";
    
    @GRpcConfigLabel
    public static final String GRPC_THREADPOOL_MAX_SIZE = SPARROW_CLIENT_GRPC + ".pool.max.size";
    
    @GRpcConfigLabel
    public static final String GRPC_SERVER_CHECK_TIMEOUT = SPARROW_CLIENT_GRPC + ".server.check.timeout";
    
    @GRpcConfigLabel
    public static final String GRPC_QUEUESIZE = SPARROW_CLIENT_GRPC + ".queue.size";
    
    @GRpcConfigLabel
    public static final String GRPC_HEALTHCHECK_RETRY_TIMES = SPARROW_CLIENT_GRPC + ".health.retry";
    
    @GRpcConfigLabel
    public static final String GRPC_HEALTHCHECK_TIMEOUT = SPARROW_CLIENT_GRPC + ".health.timeout";
    
    @GRpcConfigLabel
    public static final String GRPC_MAX_INBOUND_MESSAGE_SIZE = SPARROW_CLIENT_GRPC + ".maxinbound.message.size";
    
    @GRpcConfigLabel
    public static final String GRPC_CHANNEL_KEEP_ALIVE_TIME = SPARROW_CLIENT_GRPC + ".channel.keep.alive";
    
    @GRpcConfigLabel
    public static final String GRPC_CHANNEL_KEEP_ALIVE_TIMEOUT = SPARROW_CLIENT_GRPC + ".channel.keep.alive.timeout";

    @GRpcConfigLabel
    public static final String GRPC_CHANNEL_CAPABILITY_NEGOTIATION_TIMEOUT = SPARROW_CLIENT_GRPC + ".channel.capability.negotiation.timeout";

    private static final Set<String> CONFIG_NAMES = new HashSet<>();
    
    @Documented
    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    protected @interface GRpcConfigLabel {
    
    }
    
    static {
        Class clazz = GrpcConstants.class;
        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            declaredField.setAccessible(true);
            if (declaredField.getType().equals(String.class) && null != declaredField.getAnnotation(
                    GRpcConfigLabel.class)) {
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

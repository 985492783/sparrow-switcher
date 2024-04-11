package com.sparrow.switcher.client.remote;

import com.sparrow.switcher.common.config.SparrowProperties;
import com.sparrow.switcher.common.remote.RpcClient;

import java.util.Properties;

/**
 * @author 985492783@qq.com
 * @date 2024/4/11 14:07
 */
public class ClientFactory {
    
    /**
     * TODO 创建RpcClient，重连等机制，单例
     * @param properties
     * @return
     */
    public static RpcClient createClient(Properties properties) {
        SparrowProperties sparrowProperties = SparrowProperties.convert(properties);
        return null;
    }
}

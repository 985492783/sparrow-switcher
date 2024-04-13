package com.sparrow.switcher.common.remote.client;

import com.sparrow.switcher.common.remote.ConnectionType;
import com.sparrow.switcher.common.remote.exception.SparrowException;
import org.junit.Test;
import org.mockito.Mock;

import java.util.function.Function;

import static org.junit.Assert.*;

public class RpcClientTest {

    RpcClient rpcClient;

    @Mock
    ServerListFactory serverListFactory;

    @Mock
    Connection connection;

    RpcClientConfig rpcClientConfig;

    @Test
    public void testStartClient() throws SparrowException {
        connection.serverInfo = new RpcClient.ServerInfo("127.0.0.1", 8848);
        RpcClient rpcClient = buildTestStartClient(new Function<RpcClient.ServerInfo, Connection>() {

            private int count;

            @Override
            public Connection apply(RpcClient.ServerInfo serverInfo) {
                if (count == 0) {
                    count++;
                    throw new RuntimeException("test");
                }
                return connection;
            }
        });
        try {
            rpcClient.start();
            assertTrue(rpcClient.isRunning());
        } finally {
            rpcClient.shutdown();
        }
    }

    private RpcClient buildTestStartClient(Function<RpcClient.ServerInfo, Connection> function) {
        return new RpcClient(rpcClientConfig, serverListFactory) {

            @Override
            public ConnectionType getConnectionType() {
                return ConnectionType.GRPC;
            }

            @Override
            public int rpcPortOffset() {
                return 0;
            }

            @Override
            public Connection connectToServer(ServerInfo serverInfo) {
                return function.apply(serverInfo);
            }
        };
    }

}

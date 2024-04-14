package com.sparrow.switcher.common.remote.client;

import com.sparrow.switcher.common.remote.ConnectionType;
import com.sparrow.switcher.common.remote.exception.SparrowException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.spy;

@RunWith(MockitoJUnitRunner.class)
public class RpcClientTest {

    RpcClient rpcClient;

    @Mock
    ServerListFactory serverListFactory;

    @Mock
    Connection connection;

    RpcClientConfig rpcClientConfig;

    @Before
    public void setUp() {
        rpcClientConfig = spy(new RpcClientConfig() {
            @Override
            public String name() {
                return "test";
            }

            @Override
            public int retryTimes() {
                return 1;
            }

            @Override
            public long timeOutMills() {
                return 3000L;
            }

            @Override
            public long connectionKeepAlive() {
                return 5000L;
            }

            @Override
            public int healthCheckRetryTimes() {
                return 1;
            }

            @Override
            public long healthCheckTimeOut() {
                return 3000L;
            }

            @Override
            public Map<String, String> labels() {
                return new HashMap<>();
            }
        });
        rpcClient = spy(new RpcClient(rpcClientConfig) {
            @Override
            public ConnectionType getConnectionType() {
                return null;
            }

            @Override
            public int rpcPortOffset() {
                return 0;
            }

            @Override
            public Connection connectToServer(ServerInfo serverInfo) {
                return null;
            }
        });
    }

    @After
    public void tearDown() throws IllegalAccessException, SparrowException {
        rpcClientConfig.labels().clear();
        rpcClient.rpcClientStatus.set(RpcClientStatus.WAIT_INIT);
        rpcClient.currentConnection = null;
        System.clearProperty("sparrow.server.port");
        rpcClient.eventLinkedBlockingQueue.clear();
        rpcClient.shutdown();
    }

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

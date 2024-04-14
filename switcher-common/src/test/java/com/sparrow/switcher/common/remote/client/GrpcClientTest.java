package com.sparrow.switcher.common.remote.client;

import com.google.common.util.concurrent.ListenableFuture;
import com.sparrow.switcher.common.grpc.auto.Payload;
import com.sparrow.switcher.common.grpc.auto.RequestGrpc;
import com.sparrow.switcher.common.remote.client.grpc.*;
import com.sparrow.switcher.common.remote.constants.AbilityMode;
import com.sparrow.switcher.common.remote.response.ServerCheckResponse;
import io.grpc.Channel;
import io.grpc.ClientCall;
import io.grpc.ManagedChannel;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GrpcClientTest {

    protected GrpcClient grpcClient;

    @Mock
    RpcClientTlsConfig tlsConfig;

    protected RpcClient.ServerInfo serverInfo;

    protected GrpcClientConfig clientConfig;

    @Before
    public void setUp() throws Exception {
        clientConfig = DefaultGrpcClientConfig.newBuilder().setServerCheckTimeOut(100L)
                .setCapabilityNegotiationTimeout(100L).setChannelKeepAliveTimeout((int) TimeUnit.SECONDS.toMillis(3L))
                .setChannelKeepAlive(1000).setName("testClient").build();
        clientConfig.setTlsConfig(tlsConfig);
        grpcClient = spy(new GrpcClient(clientConfig) {
            @Override
            protected AbilityMode abilityMode() {
                return AbilityMode.SDK_CLIENT;
            }

            @Override
            public int rpcPortOffset() {
                return 0;
            }
        });
        serverInfo = new RpcClient.ServerInfo("10.10.10.10", 8848);
    }

    @Test
    public void testConnectToServerMockSuccess() throws ExecutionException, InterruptedException, TimeoutException {
        RequestGrpc.RequestFutureStub stub = mockStub(new ServerCheckResponse(), null);
        doReturn(stub).when(grpcClient).createNewChannelStub(any(ManagedChannel.class));
        Connection connection = grpcClient.connectToServer(serverInfo);
        assertNotNull(connection);
        assertTrue(connection instanceof GrpcConnection);
        assertEquals(stub, ((GrpcConnection) connection).getGrpcFutureServiceStub());
    }

    @Test
    public void testConnectToServerMockHealthCheckFailed()
            throws ExecutionException, InterruptedException, TimeoutException {
        RequestGrpc.RequestFutureStub stub = mockStub(null, new RuntimeException("test"));
        doReturn(stub).when(grpcClient).createNewChannelStub(any(ManagedChannel.class));
        Connection connection = grpcClient.connectToServer(serverInfo);
        assertNull(connection);
    }

    private RequestGrpc.RequestFutureStub mockStub(ServerCheckResponse response, Throwable throwable)
            throws InterruptedException, ExecutionException, TimeoutException {
        RequestGrpc.RequestFutureStub stub = mock(RequestGrpc.RequestFutureStub.class);
        ListenableFuture<Payload> listenableFuture = mock(ListenableFuture.class);
        when(stub.request(any(Payload.class))).thenReturn(listenableFuture);
        if (null == throwable) {
            when(listenableFuture.get(100L, TimeUnit.MILLISECONDS)).thenReturn(GrpcUtils.convert(response));
        } else {
            when(listenableFuture.get(100L, TimeUnit.MILLISECONDS)).thenThrow(throwable);
        }
        Channel channel = mock(Channel.class);
        when(stub.getChannel()).thenReturn(channel);
        ClientCall mockCall = mock(ClientCall.class);
        when(channel.newCall(any(), any())).thenReturn(mockCall);
        return stub;
    }

}

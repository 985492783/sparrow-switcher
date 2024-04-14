package com.sparrow.switcher.common.grpc.client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import com.sparrow.switcher.common.grpc.auto.TestGRPCGrpc;
import com.sparrow.switcher.common.grpc.auto.TestProto;

public class GRpcClient {
    private final ManagedChannel channel;
    private final TestGRPCGrpc.TestGRPCBlockingStub blockingStub;

    static GRpcClient client;
    public static void main(String[] args) {
        client = new GRpcClient("127.0.0.1", 800);

        client.GetVersion();
    }

    public GRpcClient(String host, int port) {
        channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .build();
        blockingStub = TestGRPCGrpc.newBlockingStub(channel);
    }

    private void GetVersion() {
        TestProto.GetVersionRequest request = TestProto.GetVersionRequest.newBuilder().build();
        TestProto.GetVersionResponse response;
        response = blockingStub.getVersion(request);
        System.out.println("get grpc server resp:" + response.getResponse());
        System.out.println("get grpc server ver:" + response.getVersion());
    }
}


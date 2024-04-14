package com.sparrow.switcher.common.grpc.server;

import java.io.IOException;
import io.grpc.Server;
import io.grpc.ServerBuilder;

public class GRpcServer {
    private Server server;
    public static void main(String[] args) {
        final GRpcServer server = new GRpcServer();
        try {
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            server.blockUntilShutdown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void start() throws IOException {
        int port = 800;
        server = ServerBuilder.forPort(port)
                .addService(new TestGrpcApi())
                .build()
                .start();
        System.out.println("grpc server start!");
    }

    private void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }
}

package com.sparrow.switcher.common.grpc.server;

import com.sparrow.switcher.common.grpc.auto.*;
import io.grpc.stub.StreamObserver;

class TestGrpcApi extends TestGRPCGrpc.TestGRPCImplBase {
    @Override
    public void getVersion(TestProto.GetVersionRequest request, StreamObserver<TestProto.GetVersionResponse> responseObserver) {
        System.out.println("请求信息" + request);
        TestProto.GetVersionResponse reply = TestProto.GetVersionResponse.newBuilder()
                .setResponse(
                        TestProto.BaseResponse.newBuilder()
                                .setMessage("success").setCode(0))
                .setVersion(
                        TestProto.Version.newBuilder()
                                .setSoftwareVersion("1.0.0")
                                .setLastCompileTime(System.currentTimeMillis()))
                .build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
        System.out.println("发送版本信息完成！");
    }
}

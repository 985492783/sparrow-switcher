package com.sparrow.switcher.common.grpc.auto;

import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ClientCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ClientCalls.asyncClientStreamingCall;
import static io.grpc.stub.ClientCalls.asyncServerStreamingCall;
import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.blockingServerStreamingCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.stub.ServerCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ServerCalls.asyncClientStreamingCall;
import static io.grpc.stub.ServerCalls.asyncServerStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.14.0)",
    comments = "Source: test.proto")
public final class TestGRPCGrpc {

  private TestGRPCGrpc() {}

  public static final String SERVICE_NAME = "TestGRPC";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.sparrow.switcher.common.grpc.auto.TestProto.GetVersionRequest,
      com.sparrow.switcher.common.grpc.auto.TestProto.GetVersionResponse> getGetVersionMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetVersion",
      requestType = com.sparrow.switcher.common.grpc.auto.TestProto.GetVersionRequest.class,
      responseType = com.sparrow.switcher.common.grpc.auto.TestProto.GetVersionResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.sparrow.switcher.common.grpc.auto.TestProto.GetVersionRequest,
      com.sparrow.switcher.common.grpc.auto.TestProto.GetVersionResponse> getGetVersionMethod() {
    io.grpc.MethodDescriptor<com.sparrow.switcher.common.grpc.auto.TestProto.GetVersionRequest, com.sparrow.switcher.common.grpc.auto.TestProto.GetVersionResponse> getGetVersionMethod;
    if ((getGetVersionMethod = TestGRPCGrpc.getGetVersionMethod) == null) {
      synchronized (TestGRPCGrpc.class) {
        if ((getGetVersionMethod = TestGRPCGrpc.getGetVersionMethod) == null) {
          TestGRPCGrpc.getGetVersionMethod = getGetVersionMethod = 
              io.grpc.MethodDescriptor.<com.sparrow.switcher.common.grpc.auto.TestProto.GetVersionRequest, com.sparrow.switcher.common.grpc.auto.TestProto.GetVersionResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "TestGRPC", "GetVersion"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.sparrow.switcher.common.grpc.auto.TestProto.GetVersionRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.sparrow.switcher.common.grpc.auto.TestProto.GetVersionResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new TestGRPCMethodDescriptorSupplier("GetVersion"))
                  .build();
          }
        }
     }
     return getGetVersionMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static TestGRPCStub newStub(io.grpc.Channel channel) {
    return new TestGRPCStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static TestGRPCBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new TestGRPCBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static TestGRPCFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new TestGRPCFutureStub(channel);
  }

  /**
   */
  public static abstract class TestGRPCImplBase implements io.grpc.BindableService {

    /**
     */
    public void getVersion(com.sparrow.switcher.common.grpc.auto.TestProto.GetVersionRequest request,
        io.grpc.stub.StreamObserver<com.sparrow.switcher.common.grpc.auto.TestProto.GetVersionResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getGetVersionMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getGetVersionMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                com.sparrow.switcher.common.grpc.auto.TestProto.GetVersionRequest,
                com.sparrow.switcher.common.grpc.auto.TestProto.GetVersionResponse>(
                  this, METHODID_GET_VERSION)))
          .build();
    }
  }

  /**
   */
  public static final class TestGRPCStub extends io.grpc.stub.AbstractStub<TestGRPCStub> {
    private TestGRPCStub(io.grpc.Channel channel) {
      super(channel);
    }

    private TestGRPCStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected TestGRPCStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new TestGRPCStub(channel, callOptions);
    }

    /**
     */
    public void getVersion(com.sparrow.switcher.common.grpc.auto.TestProto.GetVersionRequest request,
        io.grpc.stub.StreamObserver<com.sparrow.switcher.common.grpc.auto.TestProto.GetVersionResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getGetVersionMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class TestGRPCBlockingStub extends io.grpc.stub.AbstractStub<TestGRPCBlockingStub> {
    private TestGRPCBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private TestGRPCBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected TestGRPCBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new TestGRPCBlockingStub(channel, callOptions);
    }

    /**
     */
    public com.sparrow.switcher.common.grpc.auto.TestProto.GetVersionResponse getVersion(com.sparrow.switcher.common.grpc.auto.TestProto.GetVersionRequest request) {
      return blockingUnaryCall(
          getChannel(), getGetVersionMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class TestGRPCFutureStub extends io.grpc.stub.AbstractStub<TestGRPCFutureStub> {
    private TestGRPCFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private TestGRPCFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected TestGRPCFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new TestGRPCFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.sparrow.switcher.common.grpc.auto.TestProto.GetVersionResponse> getVersion(
        com.sparrow.switcher.common.grpc.auto.TestProto.GetVersionRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getGetVersionMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_GET_VERSION = 0;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final TestGRPCImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(TestGRPCImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_GET_VERSION:
          serviceImpl.getVersion((com.sparrow.switcher.common.grpc.auto.TestProto.GetVersionRequest) request,
              (io.grpc.stub.StreamObserver<com.sparrow.switcher.common.grpc.auto.TestProto.GetVersionResponse>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class TestGRPCBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    TestGRPCBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.sparrow.switcher.common.grpc.auto.TestProto.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("TestGRPC");
    }
  }

  private static final class TestGRPCFileDescriptorSupplier
      extends TestGRPCBaseDescriptorSupplier {
    TestGRPCFileDescriptorSupplier() {}
  }

  private static final class TestGRPCMethodDescriptorSupplier
      extends TestGRPCBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    TestGRPCMethodDescriptorSupplier(String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (TestGRPCGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new TestGRPCFileDescriptorSupplier())
              .addMethod(getGetVersionMethod())
              .build();
        }
      }
    }
    return result;
  }
}

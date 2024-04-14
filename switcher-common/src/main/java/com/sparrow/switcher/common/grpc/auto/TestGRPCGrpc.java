package com.sparrow.switcher.common.grpc.auto;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.63.0)",
    comments = "Source: test.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class TestGRPCGrpc {

  private TestGRPCGrpc() {}

  public static final java.lang.String SERVICE_NAME = "TestGRPC";

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
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetVersion"))
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
    io.grpc.stub.AbstractStub.StubFactory<TestGRPCStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<TestGRPCStub>() {
        @java.lang.Override
        public TestGRPCStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new TestGRPCStub(channel, callOptions);
        }
      };
    return TestGRPCStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static TestGRPCBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<TestGRPCBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<TestGRPCBlockingStub>() {
        @java.lang.Override
        public TestGRPCBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new TestGRPCBlockingStub(channel, callOptions);
        }
      };
    return TestGRPCBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static TestGRPCFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<TestGRPCFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<TestGRPCFutureStub>() {
        @java.lang.Override
        public TestGRPCFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new TestGRPCFutureStub(channel, callOptions);
        }
      };
    return TestGRPCFutureStub.newStub(factory, channel);
  }

  /**
   */
  public interface AsyncService {

    /**
     */
    default void getVersion(com.sparrow.switcher.common.grpc.auto.TestProto.GetVersionRequest request,
        io.grpc.stub.StreamObserver<com.sparrow.switcher.common.grpc.auto.TestProto.GetVersionResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetVersionMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service TestGRPC.
   */
  public static abstract class TestGRPCImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return TestGRPCGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service TestGRPC.
   */
  public static final class TestGRPCStub
      extends io.grpc.stub.AbstractAsyncStub<TestGRPCStub> {
    private TestGRPCStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected TestGRPCStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new TestGRPCStub(channel, callOptions);
    }

    /**
     */
    public void getVersion(com.sparrow.switcher.common.grpc.auto.TestProto.GetVersionRequest request,
        io.grpc.stub.StreamObserver<com.sparrow.switcher.common.grpc.auto.TestProto.GetVersionResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetVersionMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service TestGRPC.
   */
  public static final class TestGRPCBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<TestGRPCBlockingStub> {
    private TestGRPCBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected TestGRPCBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new TestGRPCBlockingStub(channel, callOptions);
    }

    /**
     */
    public com.sparrow.switcher.common.grpc.auto.TestProto.GetVersionResponse getVersion(com.sparrow.switcher.common.grpc.auto.TestProto.GetVersionRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetVersionMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service TestGRPC.
   */
  public static final class TestGRPCFutureStub
      extends io.grpc.stub.AbstractFutureStub<TestGRPCFutureStub> {
    private TestGRPCFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected TestGRPCFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new TestGRPCFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.sparrow.switcher.common.grpc.auto.TestProto.GetVersionResponse> getVersion(
        com.sparrow.switcher.common.grpc.auto.TestProto.GetVersionRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetVersionMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_GET_VERSION = 0;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final AsyncService serviceImpl;
    private final int methodId;

    MethodHandlers(AsyncService serviceImpl, int methodId) {
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

  public static final io.grpc.ServerServiceDefinition bindService(AsyncService service) {
    return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
        .addMethod(
          getGetVersionMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.sparrow.switcher.common.grpc.auto.TestProto.GetVersionRequest,
              com.sparrow.switcher.common.grpc.auto.TestProto.GetVersionResponse>(
                service, METHODID_GET_VERSION)))
        .build();
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
    private final java.lang.String methodName;

    TestGRPCMethodDescriptorSupplier(java.lang.String methodName) {
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

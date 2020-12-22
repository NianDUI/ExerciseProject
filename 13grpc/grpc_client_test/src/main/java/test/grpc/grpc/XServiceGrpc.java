package test.grpc.grpc;

import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.34.1)",
    comments = "Source: XAccessGrpc.proto")
public final class XServiceGrpc {

  private XServiceGrpc() {}

  public static final String SERVICE_NAME = "rpc_package.XService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<test.grpc.grpc.XRequest,
      test.grpc.grpc.XReply> getXCallMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "XCall",
      requestType = test.grpc.grpc.XRequest.class,
      responseType = test.grpc.grpc.XReply.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<test.grpc.grpc.XRequest,
      test.grpc.grpc.XReply> getXCallMethod() {
    io.grpc.MethodDescriptor<test.grpc.grpc.XRequest, test.grpc.grpc.XReply> getXCallMethod;
    if ((getXCallMethod = XServiceGrpc.getXCallMethod) == null) {
      synchronized (XServiceGrpc.class) {
        if ((getXCallMethod = XServiceGrpc.getXCallMethod) == null) {
          XServiceGrpc.getXCallMethod = getXCallMethod =
              io.grpc.MethodDescriptor.<test.grpc.grpc.XRequest, test.grpc.grpc.XReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "XCall"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  test.grpc.grpc.XRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  test.grpc.grpc.XReply.getDefaultInstance()))
              .setSchemaDescriptor(new XServiceMethodDescriptorSupplier("XCall"))
              .build();
        }
      }
    }
    return getXCallMethod;
  }

  private static volatile io.grpc.MethodDescriptor<test.grpc.grpc.XRequest,
      test.grpc.grpc.XReply> getXCallIPCMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "XCallIPC",
      requestType = test.grpc.grpc.XRequest.class,
      responseType = test.grpc.grpc.XReply.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<test.grpc.grpc.XRequest,
      test.grpc.grpc.XReply> getXCallIPCMethod() {
    io.grpc.MethodDescriptor<test.grpc.grpc.XRequest, test.grpc.grpc.XReply> getXCallIPCMethod;
    if ((getXCallIPCMethod = XServiceGrpc.getXCallIPCMethod) == null) {
      synchronized (XServiceGrpc.class) {
        if ((getXCallIPCMethod = XServiceGrpc.getXCallIPCMethod) == null) {
          XServiceGrpc.getXCallIPCMethod = getXCallIPCMethod =
              io.grpc.MethodDescriptor.<test.grpc.grpc.XRequest, test.grpc.grpc.XReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "XCallIPC"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  test.grpc.grpc.XRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  test.grpc.grpc.XReply.getDefaultInstance()))
              .setSchemaDescriptor(new XServiceMethodDescriptorSupplier("XCallIPC"))
              .build();
        }
      }
    }
    return getXCallIPCMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static XServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<XServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<XServiceStub>() {
        @java.lang.Override
        public XServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new XServiceStub(channel, callOptions);
        }
      };
    return XServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static XServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<XServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<XServiceBlockingStub>() {
        @java.lang.Override
        public XServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new XServiceBlockingStub(channel, callOptions);
        }
      };
    return XServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static XServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<XServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<XServiceFutureStub>() {
        @java.lang.Override
        public XServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new XServiceFutureStub(channel, callOptions);
        }
      };
    return XServiceFutureStub.newStub(factory, channel);
  }

  /**
   */
  public static abstract class XServiceImplBase implements io.grpc.BindableService {

    /**
     */
    public void xCall(test.grpc.grpc.XRequest request,
        io.grpc.stub.StreamObserver<test.grpc.grpc.XReply> responseObserver) {
      asyncUnimplementedUnaryCall(getXCallMethod(), responseObserver);
    }

    /**
     */
    public void xCallIPC(test.grpc.grpc.XRequest request,
        io.grpc.stub.StreamObserver<test.grpc.grpc.XReply> responseObserver) {
      asyncUnimplementedUnaryCall(getXCallIPCMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getXCallMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                test.grpc.grpc.XRequest,
                test.grpc.grpc.XReply>(
                  this, METHODID_XCALL)))
          .addMethod(
            getXCallIPCMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                test.grpc.grpc.XRequest,
                test.grpc.grpc.XReply>(
                  this, METHODID_XCALL_IPC)))
          .build();
    }
  }

  /**
   */
  public static final class XServiceStub extends io.grpc.stub.AbstractAsyncStub<XServiceStub> {
    private XServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected XServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new XServiceStub(channel, callOptions);
    }

    /**
     */
    public void xCall(test.grpc.grpc.XRequest request,
        io.grpc.stub.StreamObserver<test.grpc.grpc.XReply> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getXCallMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void xCallIPC(test.grpc.grpc.XRequest request,
        io.grpc.stub.StreamObserver<test.grpc.grpc.XReply> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getXCallIPCMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class XServiceBlockingStub extends io.grpc.stub.AbstractBlockingStub<XServiceBlockingStub> {
    private XServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected XServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new XServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public test.grpc.grpc.XReply xCall(test.grpc.grpc.XRequest request) {
      return blockingUnaryCall(
          getChannel(), getXCallMethod(), getCallOptions(), request);
    }

    /**
     */
    public test.grpc.grpc.XReply xCallIPC(test.grpc.grpc.XRequest request) {
      return blockingUnaryCall(
          getChannel(), getXCallIPCMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class XServiceFutureStub extends io.grpc.stub.AbstractFutureStub<XServiceFutureStub> {
    private XServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected XServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new XServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<test.grpc.grpc.XReply> xCall(
        test.grpc.grpc.XRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getXCallMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<test.grpc.grpc.XReply> xCallIPC(
        test.grpc.grpc.XRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getXCallIPCMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_XCALL = 0;
  private static final int METHODID_XCALL_IPC = 1;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final XServiceImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(XServiceImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_XCALL:
          serviceImpl.xCall((test.grpc.grpc.XRequest) request,
              (io.grpc.stub.StreamObserver<test.grpc.grpc.XReply>) responseObserver);
          break;
        case METHODID_XCALL_IPC:
          serviceImpl.xCallIPC((test.grpc.grpc.XRequest) request,
              (io.grpc.stub.StreamObserver<test.grpc.grpc.XReply>) responseObserver);
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

  private static abstract class XServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    XServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return test.grpc.grpc.XAccessGrpc.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("XService");
    }
  }

  private static final class XServiceFileDescriptorSupplier
      extends XServiceBaseDescriptorSupplier {
    XServiceFileDescriptorSupplier() {}
  }

  private static final class XServiceMethodDescriptorSupplier
      extends XServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    XServiceMethodDescriptorSupplier(String methodName) {
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
      synchronized (XServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new XServiceFileDescriptorSupplier())
              .addMethod(getXCallMethod())
              .addMethod(getXCallIPCMethod())
              .build();
        }
      }
    }
    return result;
  }
}

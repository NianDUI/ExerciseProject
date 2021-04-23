package top.niandui.service;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import org.lognet.springboot.grpc.GRpcService;
import top.niandui.grpc.HelloReply;
import top.niandui.grpc.HelloRequest;
import top.niandui.grpc.HelloServiceGrpc;

import java.util.Date;

/**
 * HelloServiceImpl
 *
 * @author liyongda
 * @version 1.0
 * @date 2020/12/8 16:35
 */
@Slf4j
@GRpcService
public class HelloServiceImpl extends HelloServiceGrpc.HelloServiceImplBase {

    @Override
    public void sayHello(HelloRequest request, StreamObserver<HelloReply> responseObserver) {
        log.info("service sayHello request:{}", request);
        String message = request.getName() + " message " + new Date();
        HelloReply helloReply = HelloReply.newBuilder().setMessage(message).build();
        log.info("service sayHello response:{}", helloReply);
        responseObserver.onNext(helloReply);
        responseObserver.onCompleted();
    }

    public static void main(String[] args) throws Exception{
        // 手动启动服务端
        // 创建服务端，服务实现类对象
        HelloServiceImpl helloService = new HelloServiceImpl();
        // 下面这两种方式都可以，应该最终都调用了 NettyServerBuilder
        Server server = ServerBuilder.forPort(10013)
//        Server server = NettyServerBuilder.forPort(10013)
                .addService(helloService.bindService())
                .build().start();
        // 不加下面这一句，会自动停止
        server.awaitTermination();
    }
}

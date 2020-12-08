package top.niandui.service;

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
}

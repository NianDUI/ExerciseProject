package top.niandui.client;

import com.google.common.util.concurrent.ListenableFuture;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import top.niandui.grpc.HelloReply;
import top.niandui.grpc.HelloRequest;
import top.niandui.grpc.HelloServiceGrpc.HelloServiceBlockingStub;
import top.niandui.grpc.HelloServiceGrpc.HelloServiceFutureStub;

/**
 * HelloClient
 *
 * @author liyongda
 * @version 1.0
 * @date 2020/12/8 16:47
 */
@Slf4j
@RestController
public class HelloClient {
    @Autowired
    private HelloServiceBlockingStub helloServiceBlockingStub;
    @Autowired
    private HelloServiceFutureStub helloServiceFutureStub;

    @GetMapping("sayHelloBlocking/{name}")
    public String sayHelloBlocking(@PathVariable String name) throws Exception {
        HelloRequest request = HelloRequest.newBuilder().setName(name).build();
        log.info("client sayHelloBlocking request:{}", request);
        HelloReply helloReply = helloServiceBlockingStub.sayHello(request);
        log.info("client sayHelloBlocking response:{}", helloReply);
        return helloReply.toString();
    }

    @GetMapping("sayHelloFuture/{name}")
    public String sayHelloFuture(@PathVariable String name) throws Exception {
        HelloRequest request = HelloRequest.newBuilder().setName(name).build();
        log.info("client sayHelloFuture request:{}", request);
        ListenableFuture<HelloReply> future = helloServiceFutureStub.sayHello(request);
        log.info("client sayHelloFuture future:{}", future);
        HelloReply helloReply = future.get();
        log.info("client sayHelloFuture response:{}", helloReply);
        return helloReply.toString();
    }

}

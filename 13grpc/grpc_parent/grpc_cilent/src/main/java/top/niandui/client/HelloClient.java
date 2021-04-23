package top.niandui.client;

import com.google.common.util.concurrent.ListenableFuture;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import top.niandui.config.GRpcConfig;
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

    public static void main(String[] args) throws Exception {
        // 手动启动客户端
        GRpcConfig gRpcConfig = new GRpcConfig(10013);
        HelloClient helloClient = new HelloClient();
        helloClient.helloServiceBlockingStub = gRpcConfig.helloServiceBlockingStub();
        helloClient.helloServiceFutureStub = gRpcConfig.helloServiceFutureStub();
        String sayHelloBlocking = helloClient.sayHelloBlocking("sayHelloBlocking");
        System.out.println("sayHelloBlocking = " + sayHelloBlocking);
        String sayHelloFuture = helloClient.sayHelloFuture("sayHelloFuture");
        System.out.println("sayHelloFuture = " + sayHelloFuture);

        // 下面三次调用时测试先关闭服务端，在开启服务端的情况
        // 1. 先关闭服务端，在运行下面一次调用
        try {
            sayHelloFuture = helloClient.sayHelloFuture("sayHelloFuture");
            System.out.println("sayHelloFuture = " + sayHelloFuture);
        } catch (Exception e) {
            // java.io.IOException: 远程主机强迫关闭了一个现有的连接。
            // java.util.concurrent.ExecutionException: io.grpc.StatusRuntimeException: UNAVAILABLE: io exception
            e.printStackTrace();
        }
        System.out.println(gRpcConfig.managedChannel.isShutdown()); // false
        System.out.println(gRpcConfig.managedChannel.isTerminated()); // false

        // 2. 再启动服务端，在运行下面一次调用
        try {
            sayHelloFuture = helloClient.sayHelloFuture("sayHelloFuture");
            System.out.println("sayHelloFuture = " + sayHelloFuture);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(gRpcConfig.managedChannel.isShutdown()); // false
        System.out.println(gRpcConfig.managedChannel.isTerminated()); // false

        // 3. 创建一个新的客户端，在运行下面一次调用
        gRpcConfig = new GRpcConfig(10013);
        helloClient.helloServiceBlockingStub = gRpcConfig.helloServiceBlockingStub();
        helloClient.helloServiceFutureStub = gRpcConfig.helloServiceFutureStub();
        sayHelloFuture = helloClient.sayHelloFuture("sayHelloFuture");
        System.out.println("sayHelloFuture = " + sayHelloFuture);

        System.out.println(gRpcConfig.managedChannel.isShutdown()); // false
        System.out.println(gRpcConfig.managedChannel.isTerminated()); // false

        // 不主动关闭通道，服务点会报：远程主机强迫关闭了一个现有的连接。
        gRpcConfig.managedChannel.shutdown();
        System.out.println(gRpcConfig.managedChannel.isShutdown()); // true
        System.out.println(gRpcConfig.managedChannel.isTerminated());// true
    }

}

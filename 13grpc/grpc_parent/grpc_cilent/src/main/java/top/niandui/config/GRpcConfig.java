package top.niandui.config;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.niandui.grpc.HelloServiceGrpc;

/**
 * GRpcConfig
 *
 * @author liyongda
 * @version 1.0
 * @date 2020/12/8 16:50
 */
@Configuration
public class GRpcConfig {
    private final ManagedChannel managedChannel;

    public GRpcConfig(@Value("${grpc.port}") Integer port) {
        managedChannel = ManagedChannelBuilder.forAddress("127.0.0.1", port).usePlaintext().build();
    }

    @Bean
    public HelloServiceGrpc.HelloServiceBlockingStub helloServiceBlockingStub() {
        return HelloServiceGrpc.newBlockingStub(managedChannel);
    }

    @Bean
    public HelloServiceGrpc.HelloServiceFutureStub helloServiceFutureStub() {
        return HelloServiceGrpc.newFutureStub(managedChannel);
    }
}

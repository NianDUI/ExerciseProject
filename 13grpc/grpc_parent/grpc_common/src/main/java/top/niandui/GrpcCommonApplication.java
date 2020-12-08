package top.niandui;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GrpcCommonApplication {

    public static void main(String[] args) {
        SpringApplication.run(GrpcCommonApplication.class, args);
    }

    /**
     * 参考：
     *  (主)SpringBoot整合Grpc实现跨语言RPC通讯 https://blog.csdn.net/21aspnet/article/details/100727562
     *  (.proto)Springboot整合gRPC https://blog.csdn.net/weixin_40395050/article/details/96971708
     */
}

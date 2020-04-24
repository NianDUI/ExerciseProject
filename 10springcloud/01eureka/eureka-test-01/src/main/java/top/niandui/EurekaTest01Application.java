package top.niandui;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class EurekaTest01Application {

    public static void main(String[] args) {
        SpringApplication.run(EurekaTest01Application.class, args);
    }

}

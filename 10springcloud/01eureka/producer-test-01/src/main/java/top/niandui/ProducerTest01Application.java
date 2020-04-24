package top.niandui;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class ProducerTest01Application {

    public static void main(String[] args) {
        SpringApplication.run(ProducerTest01Application.class, args);
    }

}

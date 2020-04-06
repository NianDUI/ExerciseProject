package top.niandui;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("top.niandui.dao")
public class FictionwebApplication {

    public static void main(String[] args) {
        SpringApplication.run(FictionwebApplication.class, args);
    }

}

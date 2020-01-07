package top.niandui;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;
import java.util.Map;

@SpringBootTest
class Data01JdbcApplicationTests {

    @Autowired
    DataSource dataSource;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Test
    void contextLoads() throws Exception {
        System.out.println(dataSource.getClass());

        List<Map<String, Object>> list = jdbcTemplate.queryForList("select * from test");
        System.out.println("list = " + list);
    }

}

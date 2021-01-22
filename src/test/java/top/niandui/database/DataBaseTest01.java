package top.niandui.database;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import org.postgresql.Driver;
import org.postgresql.copy.CopyManager;
import org.postgresql.core.BaseConnection;
import org.springframework.jdbc.core.JdbcTemplate;
import top.niandui.common.uitls.MethodUtil;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * 数据库测试
 *
 * @author liyongda
 * @version 1.0
 * @date 2021/1/22 14:52
 */
public class DataBaseTest01 {
    public static Properties properties = new Properties();
    public static String url = "jdbc:postgresql://10.144.0.21:5432/fiction";

    static {
        properties.put("url", url);
        properties.put("user", "postgres");
        properties.put("password", "root");
        properties.put("username", "postgres");
    }

    // 先创建连接池，再创建JdbcTemplate
    public static JdbcTemplate getJdbcTemplate(Map properties) throws Exception {
        // 先创建连接池
        DataSource dataSource = DruidDataSourceFactory.createDataSource(properties);
        // 创建JdbcTemplate
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        return jdbcTemplate;
    }

    //先创建驱动，再创建连接
    public static Connection getConnection(String url, Properties info) throws Exception {
        // 创建 postgresql 驱动
        Driver driver = new Driver();
        // 使用驱动类 建立一个连接
        Connection connection = driver.connect(url, info);
        return connection;
    }

    public static void main(String[] args) throws Exception {
//        copy01();
//        copy02();
        select01();
    }

    // 执行SELECT SQL
    public static void select01() throws Exception {
        long start = System.currentTimeMillis();
        JdbcTemplate jdbcTemplate = getJdbcTemplate(properties);
        String sql = "select * from task";
        List<Map<String, Object>> maps = jdbcTemplate.queryForList(sql);
        maps.forEach(System.out::println);
        System.out.println((System.currentTimeMillis() - start) / 1000.0 + "S");
    }

    // 使用自己写的copy方法进行导出
    public static void copy01() throws Exception {
        long start = System.currentTimeMillis();
        JdbcTemplate jdbcTemplate = getJdbcTemplate(properties);
        String table = "( SELECT * FROM task )";
        MethodUtil.copyOut(jdbcTemplate, System.out, table, "|", "utf-8");
        System.out.println((System.currentTimeMillis() - start) / 1000.0 + "S");
    }

    // 直接使用CopyManager对象导出
    public static void copy02() throws Exception {
        long start = System.currentTimeMillis();
        Connection connection = getConnection(url, properties);
        String table = "( SELECT * FROM task )";
        String sql = String.format("COPY %s TO STDOUT DELIMITER '%s' ENCODING '%s' %s", table, "|", "UTF-8", "");
        CopyManager copyManager = new CopyManager((BaseConnection) connection);
        long l = copyManager.copyOut(sql, System.out);
        System.out.println("导出行数" + l);
        System.out.println((System.currentTimeMillis() - start) / 1000.0 + "S");
        connection.close();
    }

}

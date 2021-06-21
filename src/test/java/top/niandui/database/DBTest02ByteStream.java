package top.niandui.database;

import org.junit.jupiter.api.Test;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;

import static top.niandui.database.DataBaseTest01.getJdbcTemplate;
import static top.niandui.database.DataBaseTest01.properties;

/**
 * 二进制流存取测试
 *
 * @author liyongda
 * @version 1.0
 * @date 2021/6/18 14:33
 */
public class DBTest02ByteStream {

    @Test
    public void test01Insert() throws Exception {
        JdbcTemplate jdbcTemplate = getJdbcTemplate(properties);

        // 使用链接回调
        Integer execute = jdbcTemplate.execute(new ConnectionCallback<Integer>() {
            @Override
            public Integer doInConnection(Connection con) throws SQLException, DataAccessException {
                PreparedStatement ps = con.prepareStatement("insert into test01(id,bytes) values(?,?)");
                ps.setObject(1, 1);
                try {
                    ps.setBinaryStream(2, new FileInputStream("d:/ztest/es2.xlsx"));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                return ps.executeUpdate();
            }
        });
        System.out.println("execute = " + execute);

        // 使用预处理的声明回调
        Integer execute1 = jdbcTemplate.execute("insert into test01(id,bytes) values(?,?)", new PreparedStatementCallback<Integer>() {
            @Override
            public Integer doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
                ps.setObject(1, 1);
                try {
                    ps.setBinaryStream(2, new FileInputStream("d:/ztest/es2.xlsx"));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                return ps.executeUpdate();
            }
        });
        System.out.println("execute1 = " + execute1);
    }

    @Test
    public void test02Query() throws Exception {
        JdbcTemplate jdbcTemplate = getJdbcTemplate(properties);
        String sql = "select id,bytes from test01 where id = 1";

        jdbcTemplate.execute(sql, new CallableStatementCallback<Integer>() {
            @Override
            public Integer doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                ResultSet resultSet = cs.executeQuery();
                resultSet.next();
                try (InputStream is = resultSet.getBinaryStream(2)) {
                    Files.copy(is, Paths.get("d:/ztest/es4.xlsx"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return resultSet.getInt(1);
            }
        });

    }
}

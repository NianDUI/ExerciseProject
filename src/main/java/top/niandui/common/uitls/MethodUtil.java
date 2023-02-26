package top.niandui.common.uitls;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.session.Configuration;
import org.postgresql.copy.CopyManager;
import org.postgresql.core.BaseConnection;
import org.postgresql.core.NativeQuery;
import org.postgresql.core.Parser;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.util.StringUtils;
import top.niandui.common.model.PageOrder;

import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * 方法工具
 *
 * @author liyongda
 * @version 1.0
 * @date 2020/3/25 9:47
 */
@Slf4j
public class MethodUtil {
    // 默认一次数据量
    public static final int DEFAULT_ONE_COUNT = 500;

    private MethodUtil() {
    }

    /**
     * 批量处理数据防止一次处理过多(一次处理500条，所有要处理的数据都在list中)
     *
     * @param list     要处理的数据列表
     * @param callBack 处理回调方法
     * @param <T>      list中存放的数据类型
     * @param <R>      回调处理方法返回对象类型
     * @return 回调处理方法返回对象列表
     */
    public static <T, R> List<R> batch(List<T> list, Function<List<T>, R> callBack) {
        return batch(list, callBack, DEFAULT_ONE_COUNT);
    }

    /**
     * 批量处理数据防止一次处理过多(一次处理oneCount条，所有要处理的数据都在list中)
     *
     * @param list     要处理的数据列表
     * @param callBack 处理回调方法
     * @param oneCount 一次处理数据量
     * @param <T>      list中存放的数据类型
     * @param <R>      回调处理方法返回对象类型
     * @return 回调处理方法返回对象列表
     */
    public static <T, R> List<R> batch(List<T> list, Function<List<T>, R> callBack, int oneCount) {
        if (list == null) {
            return new ArrayList<>();
        }
        List<R> rvList = new ArrayList<>();
        if (oneCount <= 0) {
            oneCount = DEFAULT_ONE_COUNT;
        }
        int start, end = 0;
        while (list.size() > (start = end)) {
            List<T> subList = list.subList(start, Math.min((end = start + oneCount), list.size()));
            rvList.add(callBack.apply(subList));
        }
        return rvList;
    }

    /**
     * 批量处理数据防止一次处理过多(一次处理500条，所有要处理的数据都在list中)
     *
     * @param list     要处理的数据列表(回调方法第一个参数)
     * @param u        处理数据时所需参数(回调方法第二个参数)
     * @param callBack 有两个参数的回调处理方法
     * @param <T>      list中存放的数据类型
     * @param <U>      u的数据类型
     * @param <R>      回调方法的返回对象类型
     * @return 回调处理方法返回对象列表
     */
    public static <T, U, R> List<R> batch(List<T> list, U u, BiFunction<List<T>, U, R> callBack) {
        return batch(list, u, callBack, DEFAULT_ONE_COUNT);
    }

    /**
     * 批量处理数据防止一次处理过多(一次处理oneCount条，所有要处理的数据都在list中)
     *
     * @param list     要处理的数据列表(回调方法第一个参数)
     * @param u        处理数据时所需参数(回调方法第二个参数)
     * @param callBack 有两个参数的回调处理方法
     * @param oneCount 一次处理数据量
     * @param <T>      list中存放的数据类型
     * @param <U>      u的数据类型
     * @param <R>      回调方法的返回对象类型
     * @return 回调处理方法返回对象列表
     */
    public static <T, U, R> List<R> batch(List<T> list, U u, BiFunction<List<T>, U, R> callBack, int oneCount) {
        if (list == null) {
            return new ArrayList<>();
        }
        List<R> rvList = new ArrayList<>();
        if (oneCount <= 0) {
            oneCount = DEFAULT_ONE_COUNT;
        }
        int start, end = 0;
        while (list.size() > (start = end)) {
            List<T> subList = list.subList(start, Math.min((end = start + oneCount), list.size()));
            rvList.add(callBack.apply(subList, u));
        }
        return rvList;
    }

    /**
     * 批量处理数据防止一次处理过多(一次处理500条，所有要处理的数据都在list中)
     *
     * @param t        处理数据时所需参数(回调方法第一个参数)
     * @param list     要处理的数据列表(回调方法第二个参数)
     * @param callBack 有两个参数的回调处理方法
     * @param <T>      t的数据类型
     * @param <U>      list中存放的数据类型
     * @param <R>      回调方法的返回对象类型
     * @return 回调处理方法返回对象列表
     */
    public static <T, U, R> List<R> batch(T t, List<U> list, BiFunction<T, List<U>, R> callBack) {
        return batch(t, list, callBack, DEFAULT_ONE_COUNT);
    }

    /**
     * 批量处理数据防止一次处理过多(一次处理oneCount条，所有要处理的数据都在list中)
     *
     * @param t        处理数据时所需参数(回调方法第一个参数)
     * @param list     要处理的数据列表(回调方法第二个参数)
     * @param oneCount 一次处理数据量
     * @param <T>      t的数据类型
     * @param <U>      list中存放的数据类型
     * @param <R>      回调方法的返回对象类型
     * @return 回调处理方法返回对象列表
     */
    public static <T, U, R> List<R> batch(T t, List<U> list, BiFunction<T, List<U>, R> callBack, int oneCount) {
        if (list == null) {
            return new ArrayList<>();
        }
        List<R> rvList = new ArrayList<>();
        if (oneCount <= 0) {
            oneCount = DEFAULT_ONE_COUNT;
        }
        int start, end = 0;
        while (list.size() > (start = end)) {
            List<U> subList = list.subList(start, Math.min((end = start + oneCount), list.size()));
            rvList.add(callBack.apply(t, subList));
        }
        return rvList;
    }

    /**
     * 将t数据插入到list列表中，当list中数据量达到500时
     * 使用callBack回调方法将list中的所有数据进行处理并清空列表，否则返回null
     *
     * @param list     存放数据的列表
     * @param t        要加入列表的数据
     * @param callBack 回调方法：list中数据处理方法
     * @param <T>      list中存放的数据类型
     * @param <R>      回调方法返回类型
     * @return 回调处理方法返回对象
     */
    public static <T, R> R batch(List<T> list, T t, Function<List<T>, R> callBack) {
        return batch(list, t, callBack, DEFAULT_ONE_COUNT);
    }

    /**
     * 将t数据插入到list列表中，当list中数据量达到oneCount时
     * 使用callBack回调方法将list中的所有数据进行处理并清空列表，否则返回null
     *
     * @param list     存放数据的列表
     * @param t        要加入列表的数据
     * @param oneCount 要处理是的数据量
     * @param <T>      list中存放的数据类型
     * @param <R>      回调方法返回类型
     * @return 回调处理方法返回对象
     */
    public static <T, R> R batch(List<T> list, T t, Function<List<T>, R> callBack, int oneCount) {
        R r = null;
        if (list == null) {
            return r;
        }
        if (oneCount <= 0) {
            oneCount = DEFAULT_ONE_COUNT;
        }
        list.add(t);
        if (list.size() >= oneCount) {
            // 回调方法
            r = callBack.apply(list);
            // 清空列表
            list.clear();
        }
        return r;
    }

    /**
     * 转化类型，防止为null
     *
     * @param param    要转换的对象
     * @param callBack 字符串转换为目标类型的方法
     * @param <R>      目标类型
     * @return 类型转换后的对象
     */
    public static <R> R convert(Object param, Function<String, R> callBack) {
        return convert(param, callBack, null);
    }

    /**
     * 转化类型，防止为null。当类型为目标的类型时，直接返回
     *
     * @param param    要转换的对象
     * @param callBack 字符串转换为目标类型的方法
     * @param rClass   目标类型的Class类对象
     * @param <R>      目标类型
     * @return 类型转换后的对象
     */
    public static <R> R convert(Object param, Function<String, R> callBack, Class<R> rClass) {
        return param == null ? null : param.getClass() == rClass ? (R) param : callBack.apply(param.toString());
    }

    /**
     * 分页查询Map参数中添加默认排序字段和方式。不影响原有排序
     *
     * @param pageOrder 分页查询参数对象
     * @param orderBy   排序字段名
     * @param descOrAsc 排序方式
     */
    public static void addDefaultSort(PageOrder pageOrder, String orderBy, String descOrAsc) {
        if (!StringUtils.hasText(pageOrder.getOrderBy()) && StringUtils.hasText(orderBy)) {
            pageOrder.setOrderBy(orderBy.trim());
        }
        if (!StringUtils.hasText(pageOrder.getDescOrAsc()) && StringUtils.hasText(descOrAsc)) {
            pageOrder.setDescOrAsc(descOrAsc.trim());
        }
    }

    /**
     * postgresql rpad(string text, length int [,fill text])函数
     * 在字符串尾部填充text至长度为length，缺省为空白，如果string的长度已经大于length，则会截断后面多余length的字符
     *
     * @param text   要处理的文本
     * @param length 长度
     * @param c      要填充的字符
     * @return
     */
    public static String rpad(String text, int length, char c) {
        if (text == null) {
            return null;
        }
        if (text.length() >= length) {
            return text.substring(0, length);
        } else {
            StringBuilder sb = new StringBuilder(text);
            while (sb.length() < length) {
                sb.append(c);
            }
            return sb.toString();
        }
    }

    /**
     * Postgres copy from方法。默认值：列分隔符:","、编码:"UTF-8"
     *
     * @param jdbcTemplate SpringBoot JdbcTemplate对象
     * @param is           文件输入流
     * @param table        插入表信息
     * @return 导入数据条数
     * @throws Exception
     */
    public static long copyIn(JdbcTemplate jdbcTemplate, InputStream is, String table) throws Exception {
        return copyIn(jdbcTemplate, is, table, null, null, null);
    }

    /**
     * Postgres copy from方法
     *
     * @param jdbcTemplate SpringBoot JdbcTemplate对象
     * @param is           文件输入流
     * @param table        插入表信息
     * @param delimiter    列分隔符,默认","
     * @param charsetName  编码,默认"UTF-8"
     * @return 导入数据条数
     * @throws Exception
     */
    public static long copyIn(JdbcTemplate jdbcTemplate, InputStream is, String table, String delimiter, String charsetName) throws Exception {
        return copyIn(jdbcTemplate, is, table, delimiter, charsetName, null);
    }

    /**
     * Postgres copy from方法
     *
     * @param jdbcTemplate SpringBoot JdbcTemplate对象
     * @param is           文件输入流
     * @param table        插入表信息
     * @param delimiter    列分隔符,默认","
     * @param charsetName  编码,默认"UTF-8"
     * @param other        其他参数,默认""
     * @return 导入数据条数
     * @throws Exception
     */
    public static long copyIn(JdbcTemplate jdbcTemplate, InputStream is, String table, String delimiter, String charsetName, String other) throws Exception {
        if (StringUtils.hasText(delimiter)) {
            delimiter = ",";
        }
        if (StringUtils.hasText(charsetName)) {
            charsetName = "UTF-8";
        }
        if (other == null) {
            other = "";
        }
        String sql = String.format("COPY %s FROM STDIN DELIMITER '%s' ENCODING '%s' %s", table, delimiter, charsetName, other);
        Connection conn = null;
        long num;
        try {
            conn = jdbcTemplate.getDataSource().getConnection();
            CopyManager copyManager = new CopyManager((BaseConnection) conn.getMetaData().getConnection());
            log.info(sql);
            num = copyManager.copyIn(sql, is);
        } finally {
            if (conn != null) {
                DataSourceUtils.releaseConnection(conn, jdbcTemplate.getDataSource());
            }
        }
        log.info("COPY 导入 " + num + " 条数据");
        return num;
    }

    /**
     * Postgres copy to方法。默认值：列分隔符:","、编码:"UTF-8"
     *
     * @param jdbcTemplate SpringBoot JdbcTemplate对象
     * @param os           输出流
     * @param table        导出表信息
     * @return 导出数据条数
     * @throws Exception
     */
    public static long copyOut(JdbcTemplate jdbcTemplate, OutputStream os, String table) throws Exception {
        return copyOut(jdbcTemplate, os, table, null, null, null);
    }

    /**
     * Postgres copy to方法
     *
     * @param jdbcTemplate SpringBoot JdbcTemplate对象
     * @param os           输出流
     * @param table        导出表信息
     * @param delimiter    列分隔符,默认","
     * @param charsetName  编码,默认"UTF-8"
     * @return 导出数据条数
     * @throws Exception
     */
    public static long copyOut(JdbcTemplate jdbcTemplate, OutputStream os, String table, String delimiter, String charsetName) throws Exception {
        return copyOut(jdbcTemplate, os, table, delimiter, charsetName, null);
    }

    /**
     * Postgres copy to方法
     *
     * @param jdbcTemplate SpringBoot JdbcTemplate对象
     * @param os           输出流
     * @param table        导出表信息
     * @param delimiter    列分隔符,默认","
     * @param charsetName  编码,默认"UTF-8"
     * @param other        其他参数,默认""
     * @return 导出数据条数
     * @throws Exception
     */
    public static long copyOut(JdbcTemplate jdbcTemplate, OutputStream os, String table, String delimiter, String charsetName, String other) throws Exception {
        if (StringUtils.hasText(delimiter)) {
            delimiter = ",";
        }
        if (StringUtils.hasText(charsetName)) {
            charsetName = "UTF-8";
        }
        if (other == null) {
            other = "";
        }
        String sql = String.format("COPY %s TO STDOUT DELIMITER '%s' ENCODING '%s' %s", table, delimiter, charsetName, other);
        log.info(sql);
        Connection conn = null;
        long num;
        try {
            conn = jdbcTemplate.getDataSource().getConnection();
            CopyManager copyManager = new CopyManager((BaseConnection) conn.getMetaData().getConnection());
            num = copyManager.copyOut(sql, os);
        } finally {
            if (conn != null) {
                DataSourceUtils.releaseConnection(conn, jdbcTemplate.getDataSource());
            }
        }
        log.info("COPY 导出 " + num + " 条数据");
        return num;
    }

    /**
     * 获取Mapper文件中指定key的SQL，并处理成可执行SQL
     *
     * @param configuration Configuration对象，例如：sqlSessionFactory.getConfiguration()
     * @param statementId   Mapper中SQL的id
     * @param params        SQL所需参数，只允许Map类型的多层嵌套
     * @return 可执行的SQL
     * @throws SQLException
     */
    public static String getExecuteSql(Configuration configuration, String statementId, Map params) throws SQLException {
        BoundSql boundSql = configuration.getMappedStatement(statementId).getBoundSql(params);
        String sql = boundSql.getSql().replaceAll("\r?\n", "");
        log.info("getExecuteSql: statementId = " + statementId);
        log.info(" Preparing: " + sql);
        List<NativeQuery> queries = Parser.parseJdbcSql(sql, true, true, true, false, true, "");
        sql = queries.get(0).nativeSql;
        StringBuilder sb = new StringBuilder();
        for (ParameterMapping mapping : boundSql.getParameterMappings()) {
            String property = mapping.getProperty();
            Object mapValue = getMapValue(params, property);
            sql = sql.replaceFirst("\\$\\d+", handleSQLParam(mapValue));
            sb.append(mapValue).append("(").append(mapValue.getClass().getSimpleName()).append("), ");
        }
        if (sb.length() != 0) {
            sb.delete(sb.length() - 2, sb.length());
        }
        log.info("Parameters: " + sb);
        return sql;
    }

    /**
     * 获取Map的value
     *
     * @param params 参数对象集合
     * @param key    键值，例：a.b.c
     * @return 返回键的值
     */
    public static Object getMapValue(Map params, String key) {
        if (key == null) {
            params.get(key);
        }
        String[] keys = key.split("\\.");
        Object value = params;
        for (String k : keys) {
            value = ((Map) value).get(k);
        }
        return value;
    }

    /**
     * 将SQL参数处理成字符串
     *
     * @param param 参数
     * @return 处理成的字符串
     */
    public static String handleSQLParam(Object param) {
        if (param == null) {
            return "null";
        } else if (param instanceof String) {
            return String.format("'%s'", param);
        } else if (param instanceof Date) {
            return String.format("'%tF %<tT'::TIMESTAMP", param);
        } else {
            return param.toString();
        }
    }
}

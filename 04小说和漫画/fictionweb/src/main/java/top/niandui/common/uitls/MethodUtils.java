package top.niandui.common.uitls;

import lombok.extern.slf4j.Slf4j;
import org.postgresql.copy.CopyManager;
import org.postgresql.core.BaseConnection;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.util.StringUtils;
import top.niandui.common.model.PageOrder;

import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * @Title: MethodUtils.java
 * @description: 工具
 * @time: 2020/3/25 9:47
 * @author: liyongda
 * @version: 1.0
 */
@Slf4j
public class MethodUtils {
    // 默认一次数据量
    public static final int DEFAULT_ONE_COUNT = 500;

    private MethodUtils() {
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
    public static <T, R> List<R> createBatch(List<T> list, Function<List<T>, R> callBack) {
        return createBatch(list, callBack, DEFAULT_ONE_COUNT);
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
    public static <T, R> List<R> createBatch(List<T> list, Function<List<T>, R> callBack, int oneCount) {
        List<R> rvList = new ArrayList<>();
        if (list == null) {
            return rvList;
        }
        if (oneCount <= 0) {
            oneCount = DEFAULT_ONE_COUNT;
        }
        int start = 0, end = oneCount;
        while (list.size() > start) {
            List<T> subList;
            if (list.size() > end) {
                subList = list.subList(start, end);
            } else {
                subList = list.subList(start, list.size());
            }
            rvList.add(callBack.apply(subList));
            start = end;
            end = start + oneCount;
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
    public static <T, U, R> List<R> createBatch(List<T> list, U u, BiFunction<List<T>, U, R> callBack) {
        return createBatch(list, u, callBack, DEFAULT_ONE_COUNT);
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
    public static <T, U, R> List<R> createBatch(List<T> list, U u, BiFunction<List<T>, U, R> callBack, int oneCount) {
        List<R> rvList = new ArrayList<>();
        if (list == null) {
            return rvList;
        }
        if (oneCount <= 0) {
            oneCount = DEFAULT_ONE_COUNT;
        }
        int start = 0, end = oneCount;
        while (list.size() > start) {
            List<T> subList;
            if (list.size() > end) {
                subList = list.subList(start, end);
            } else {
                subList = list.subList(start, list.size());
            }
            rvList.add(callBack.apply(subList, u));
            start = end;
            end = start + oneCount;
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
    public static <T, U, R> List<R> createBatch(T t, List<U> list, BiFunction<T, List<U>, R> callBack) {
        return createBatch(t, list, callBack, DEFAULT_ONE_COUNT);
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
    public static <T, U, R> List<R> createBatch(T t, List<U> list, BiFunction<T, List<U>, R> callBack, int oneCount) {
        List<R> rvList = new ArrayList<>();
        if (list == null) {
            return rvList;
        }
        if (oneCount <= 0) {
            oneCount = DEFAULT_ONE_COUNT;
        }
        int start = 0, end = oneCount;
        while (list.size() > start) {
            List<U> subList;
            if (list.size() > end) {
                subList = list.subList(start, end);
            } else {
                subList = list.subList(start, list.size());
            }
            rvList.add(callBack.apply(t, subList));
            start = end;
            end = start + oneCount;
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
    public static <T, R> R createBatch(List<T> list, T t, Function<List<T>, R> callBack) {
        return createBatch(list, t, callBack, DEFAULT_ONE_COUNT);
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
    public static <T, R> R createBatch(List<T> list, T t, Function<List<T>, R> callBack, int oneCount) {
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
        return param == null ? null : callBack.apply(param.toString());
    }

    /**
     * 分页查询Map参数中添加默认排序字段和方式。不影响原有排序
     *
     * @param pageOrder 分页查询参数对象
     * @param orderBy   排序字段名
     * @param descOrAsc 排序方式
     */
    public static void addDefaultSort(PageOrder pageOrder, String orderBy, String descOrAsc) {
        if (!StringUtils.isEmpty(pageOrder.getOrderBy())) {
            orderBy = pageOrder.getOrderBy();
        }
        orderBy = orderBy.trim();
        if (!orderBy.startsWith("\"")) {
            orderBy = "\"" + orderBy;
        }
        if (!orderBy.endsWith("\"")) {
            orderBy += "\"";
        }
        pageOrder.setOrderBy(orderBy);
        if (StringUtils.isEmpty(pageOrder.getDescOrAsc()) && !StringUtils.isEmpty(descOrAsc)) {
            pageOrder.setDescOrAsc(descOrAsc);
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
        if (delimiter == null) {
            delimiter = ",";
        }
        if (StringUtils.isEmpty(charsetName)) {
            charsetName = "UTF-8";
        }
        if (other == null) {
            other = "";
        }
        String sql = String.format("COPY %s FROM STDIN DELIMITER '%s' ENCODING '%s' %s", table, delimiter, charsetName, other);
        Connection conn = jdbcTemplate.getDataSource().getConnection();
        CopyManager copyManager = new CopyManager((BaseConnection) conn.getMetaData().getConnection());
        log.debug(sql);
        long num = copyManager.copyIn(sql, is);
        DataSourceUtils.releaseConnection(conn, jdbcTemplate.getDataSource());
        log.debug("COPY 导入 " + num + " 条数据");
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
        if (delimiter == null) {
            delimiter = ",";
        }
        if (StringUtils.isEmpty(charsetName)) {
            charsetName = "UTF-8";
        }
        if (other == null) {
            other = "";
        }
        String sql = String.format("COPY %s TO STDIN DELIMITER '%s' ENCODING '%s' %s", table, delimiter, charsetName, other);
        log.debug(sql);
        Connection conn = jdbcTemplate.getDataSource().getConnection();
        CopyManager copyManager = new CopyManager((BaseConnection) conn.getMetaData().getConnection());
        long num = copyManager.copyOut(sql, os);
        DataSourceUtils.releaseConnection(conn, jdbcTemplate.getDataSource());
        log.debug("COPY 导出 " + num + " 条数据");
        return num;
    }
}

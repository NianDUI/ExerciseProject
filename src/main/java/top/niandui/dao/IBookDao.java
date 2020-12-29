package top.niandui.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import top.niandui.common.base.IBaseDao;
import top.niandui.common.model.IdNameModel;
import top.niandui.model.Book;
import top.niandui.model.Proxy;

import java.util.List;
import java.util.Map;

/**
 * @author 李永达
 * @version 1.0
 * @title IBookDao.java
 * @description 书籍
 * @time: 2020/3/28 14:55
 */
@Repository
public interface IBookDao<T> extends IBaseDao<T> {
    // 有条件的查询下拉
    List<IdNameModel<Long>> option(Map params);

    // 更新任务状态
    int updateTaskstatus(Long bookid, Integer taskstatus);

    // 查询站点书籍数量
    int querySiteBookCount(String id);

    // 查询书籍任务状态
    Integer queryBookTaskstatus(Long id);

    // 更新指定状态任务的任务状态
    int updateTaskstatusByRawStatus(@Param("bookid") Long bookid
            , @Param("newStatus") Integer newStatus, @Param("rawStatus") Integer rawStatus);

    // 查询内容列表
    List<Map> queryContentList(@Param("bookid") Long bookid);

    // 查询书籍任务转台列表
    List<Book> queryBookTaskstatusList();

    // 查询书籍处理信息
    String queryHandlerinfo(Long id);

    // 代理下拉
    List<IdNameModel<Integer>> optionProxy();

    // 查询单条代理信息
    Proxy modelProxy(Integer id);
}
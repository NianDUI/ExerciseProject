package top.niandui.dao;

import org.springframework.stereotype.Repository;
import top.niandui.common.base.IBaseDao;

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
    // 更新任务状态
    void updateTaskstatus(Long bookid, Integer taskstatus);

    // 查询站点书籍数量
    int querySiteBookCount(String id);

    // 查询章节内容下载列表
    List<String> queryChapterDownloadList(Map params);
}
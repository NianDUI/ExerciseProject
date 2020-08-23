package top.niandui.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import top.niandui.common.base.IBaseDao;

/**
 * @author 李永达
 * @version 1.0
 * @title IParagraphDao.java
 * @description 段落
 * @time: 2020/3/28 14:55
 */
@Repository
public interface IParagraphDao<T> extends IBaseDao<T> {
    // 通过章节id删除
    void deleteByChapterId(String id);

    // 通过书籍id删除
    void deleteByBookId(String id);

    // 通过书籍和章节id删除
    void deleteByBookAndChapterId(@Param("bookid") Long bookid, @Param("chapterid") String chapterid);
}
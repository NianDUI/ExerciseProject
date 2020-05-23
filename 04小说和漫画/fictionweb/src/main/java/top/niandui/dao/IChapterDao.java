package top.niandui.dao;

import org.springframework.stereotype.Repository;
import top.niandui.common.base.IBaseDao;
import top.niandui.model.Book;
import top.niandui.model.Chapter;

/**
 * @author 李永达
 * @version 1.0
 * @title IChapterDao.java
 * @description 章节
 * @time: 2020/3/28 14:55
 */
@Repository
public interface IChapterDao<T> extends IBaseDao<T> {
    // 通过书籍id删除
    void deleteByBookId(String id);

    // 查询书籍最后一章节
    Chapter queryBookAsLastChapter(Long id);

    // 获取下一章
    Chapter nextModel(Long id);

    // 获取下一章id
    Long nextChapterid(Long id);

    // 获取上一章id
    Long prevChapterid(Long id);

    // 通过多个id查询数据
    Book queryBookByMultiId(String id);
}
package top.niandui.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.niandui.common.base.BaseServiceImpl;
import top.niandui.common.expection.ReStateException;
import top.niandui.common.model.IdNameModel;
import top.niandui.dao.IBookDao;
import top.niandui.dao.IChapterDao;
import top.niandui.dao.IConfigDao;
import top.niandui.dao.IParagraphDao;
import top.niandui.model.Book;
import top.niandui.model.Chapter;
import top.niandui.model.Config;
import top.niandui.model.vo.ChapterListReturnVO;
import top.niandui.model.vo.ChapterSearchVO;
import top.niandui.service.IChapterService;
import top.niandui.utils.WebClientUtil;

import java.util.List;

import static top.niandui.common.uitls.MethodUtils.addDefaultSort;

/**
 * @author 李永达
 * @version 1.0
 * @title ChapterServiceImpl.java
 * @description 章节Service实现类
 * @time: 2020/4/6 11:09
 */
@Service
public class ChapterServiceImpl extends BaseServiceImpl implements IChapterService {
    @Autowired
    private IChapterDao iChapterDao;
    @Autowired
    private IBookDao iBookDao;
    @Autowired
    private IConfigDao iConfigDao;
    @Autowired
    private IParagraphDao iParagraphDao;
    @Autowired
    private WebClientUtil webClientUtil;

    @Override
    public void create(Chapter chapter) throws Exception {
        // 检查重名
        checkName(new IdNameModel(chapter.getChapterid(), chapter.getName()));
        iChapterDao.create(chapter);
    }

    @Override
    public void update(Chapter chapter) throws Exception {
        // 检查重名
        checkName(new IdNameModel(chapter.getChapterid(), chapter.getName()));
        iChapterDao.update(chapter);
    }

    @Override
    public void delete(String id) throws Exception {
        // 删除段落
        iParagraphDao.deleteByChapterId(id);
        iChapterDao.delete(id);
    }

    @Override
    public PageInfo<ChapterListReturnVO> queryList(ChapterSearchVO searchVO) throws Exception {
        addDefaultSort(searchVO, "seqid", "ASC");
        PageHelper.startPage(searchVO.getPageNum(), searchVO.getPageSize(), getOrder(searchVO));
        return new PageInfo<ChapterListReturnVO>(iChapterDao.queryList(searchVO));
    }

    @Override
    public Chapter model(Long id) throws Exception {
        return (Chapter) iChapterDao.model(id);
    }

    @Override
    public List<IdNameModel> option() throws Exception {
        return iChapterDao.option();
    }

    @Override
    public void checkName(IdNameModel checkName) throws Exception {
        if (iChapterDao.checkName(checkName) > 0) {
            throw new RuntimeException("章节已存在");
        }
    }

    @Override
    public void reacquireChapter(Long id) throws Exception {
        Book book = (Book) iBookDao.model(id);
        isTaskStatus(book);
        iChapterDao.deleteByBookId(id.toString());
        iParagraphDao.deleteByBookId(id.toString());
        Config config = (Config) iConfigDao.model(book.getConfigid());
        // 更新任务状态
        iBookDao.updateTaskstatus(book.getBookid(), 1);
        // 第一次不跳过
        webClientUtil.getBook(config, book, 0, false);
    }

    @Override
    public void getFollowUpChapter(Long id) throws Exception {
        Book book = (Book) iBookDao.model(id);
        isTaskStatus(book);
        Config config = (Config) iConfigDao.model(book.getConfigid());
        Chapter chapter = iChapterDao.queryBookAsList(id);
        // 更新任务状态
        iBookDao.updateTaskstatus(book.getBookid(), 2);
        if (chapter != null) {
            book.setStarturl(chapter.getUrl());
            // 第一次跳过
            webClientUtil.getBook(config, book, chapter.getSeqid() + 1, true);
        } else {
            // 第一次不跳过
            webClientUtil.getBook(config, book, 0, false);
        }
    }

    private void isTaskStatus(Book book) throws Exception {
        if (book.getTaskstatus() == null) {
            return;
        }
        if (book.getTaskstatus() == 1) {
            throw new ReStateException("正在执行重新获取任务");
        } else if (book.getTaskstatus() == 2) {
            throw new ReStateException("正在执行获取后续任务");
        }
    }
}

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
import top.niandui.model.vo.*;
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
        isTaskStatus(iChapterDao.queryBookByMultiId(id));
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
    public List<IdNameModel<Long>> option() throws Exception {
        return iChapterDao.option();
    }

    @Override
    public void checkName(IdNameModel<Long> checkName) throws Exception {
        if (iChapterDao.checkName(checkName) > 0) {
            throw new RuntimeException("章节已存在");
        }
    }

    @Override
    public void reacquireAllChapter(Long id) throws Exception {
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
        Chapter chapter = iChapterDao.queryBookAsLastChapter(book.getBookid());
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

    @Override
    public void reacquireSingleChapter(Long id) throws Exception {
        Chapter chapter = (Chapter) iChapterDao.model(id);
        Book book = (Book) iBookDao.model(chapter.getBookid());
        isTaskStatus(book);
        Config config = (Config) iConfigDao.model(book.getConfigid());
        // 更新任务状态
        iBookDao.updateTaskstatus(book.getBookid(), 3);
        webClientUtil.getChapter(config, chapter);
    }

    private void isTaskStatus(Book book) throws Exception {
        if (book.getTaskstatus() == null) {
            return;
        }
        if (book.getTaskstatus() == 1) {
            throw new ReStateException("正在执行重新获取全部任务");
        } else if (book.getTaskstatus() == 2) {
            throw new ReStateException("正在执行获取后续章节任务");
        } else if (book.getTaskstatus() == 3) {
            throw new ReStateException("正在执行重新获取单章任务");
        }
    }

    @Override
    public ChapterInfoReturnVO queryChapterInfo(Long id) throws Exception {
        ChapterInfoReturnVO rv = new ChapterInfoReturnVO();
        // 获取本章
        rv.setChapter((Chapter) iChapterDao.model(id));
        // 获取下一章id
        rv.setNextid(iChapterDao.nextChapterid(id));
        // 获取上一章id
        rv.setPrevid(iChapterDao.prevChapterid(id));
        rv.setBook((Book) iBookDao.model(rv.getChapter().getBookid()));
        ParagraphSearchVO sv = new ParagraphSearchVO();
        sv.setChapterid(rv.getChapter().getChapterid());
        addDefaultSort(sv, "seqid", "ASC");
        rv.setParagraphList(iParagraphDao.queryList(sv));
        return rv;
    }

    @Override
    public void getSpecifiedAndFollowUpChapter(SpecifiedFollowUpGetVO getVO) throws Exception {
        Book book = (Book) iBookDao.model(getVO.getBookid());
        isTaskStatus(book);
        Config config = (Config) iConfigDao.model(book.getConfigid());
        Chapter chapter = iChapterDao.queryBookAsLastChapter(getVO.getBookid());
        // 更新任务状态
        iBookDao.updateTaskstatus(book.getBookid(), 2);
        long seqid;
        if (chapter != null) {
            seqid = chapter.getSeqid() + 1;
        } else {
            seqid = 0;
        }
        book.setStarturl(getVO.getUrl());
        // 第一次不跳过
        webClientUtil.getBook(config, book, seqid, false);
    }
}

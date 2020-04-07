package top.niandui.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import top.niandui.common.base.BaseServiceImpl;
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

import static top.niandui.utils.MethodUtils.addDefaultSort;

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

    @Async
    @Override
    public void reacquireChapter(Long id) throws Exception {
        iChapterDao.deleteByBookId(id.toString());
        iParagraphDao.deleteByBookId(id.toString());
        Book book = (Book) iBookDao.model(id);
        Config config = (Config) iConfigDao.model(book.getConfigid());
        // 第一次不跳过
        WebClientUtil.getBook(config, book, 0, false, iChapterDao, iParagraphDao);
    }

    @Override
    public void getFollowUpChapter(Long id) throws Exception {
        Book book = (Book) iBookDao.model(id);
        Config config = (Config) iConfigDao.model(book.getConfigid());
        Chapter chapter = iChapterDao.queryBookAsList(id);
        if (chapter != null) {
            book.setStarturl(chapter.getUrl());
            // 第一次跳过
            WebClientUtil.getBook(config, book, chapter.getSeqid() + 1, true, iChapterDao, iParagraphDao);
        } else {
            // 第一次不跳过
            WebClientUtil.getBook(config, book, 0, false, iChapterDao, iParagraphDao);
        }
    }
}

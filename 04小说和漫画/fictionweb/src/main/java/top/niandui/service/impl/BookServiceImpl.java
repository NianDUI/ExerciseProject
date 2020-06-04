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
import top.niandui.model.Paragraph;
import top.niandui.model.vo.BookListReturnVO;
import top.niandui.model.vo.BookSearchVO;
import top.niandui.model.vo.ChapterSearchVO;
import top.niandui.model.vo.ParagraphSearchVO;
import top.niandui.service.IBookService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static top.niandui.common.uitls.MethodUtil.addDefaultSort;
import static top.niandui.common.uitls.file.DownloadUtil.getDownloadOS;

/**
 * @author 李永达
 * @version 1.0
 * @title BookServiceImpl.java
 * @description 书籍Service实现类
 * @time: 2020/4/6 11:09
 */
@Service
public class BookServiceImpl extends BaseServiceImpl implements IBookService {
    @Autowired
    private IBookDao iBookDao;
    @Autowired
    private IConfigDao iConfigDao;
    @Autowired
    private IChapterDao iChapterDao;
    @Autowired
    private IParagraphDao iParagraphDao;

    @Override
    public void create(Book book) throws Exception {
        // 检查重名
        // checkName(new IdNameModel(book.getBookid(), book.getName()));
        iBookDao.create(book);
    }

    @Override
    public void update(Book book) throws Exception {
        // 检查重名
        // checkName(new IdNameModel(book.getBookid(), book.getName()));
        iBookDao.update(book);
    }

    @Override
    public void delete(String id) throws Exception {
        for (String sId : id.split(",")) {
            isTaskStatus((Book) iBookDao.model(Long.valueOf(sId)));
        }
        // 删除段落
        iParagraphDao.deleteByBookId(id);
        // 删除章节
        iChapterDao.deleteByBookId(id);
        iBookDao.delete(id);
    }

    @Override
    public PageInfo<BookListReturnVO> queryList(BookSearchVO searchVO) throws Exception {
        addDefaultSort(searchVO, "createtime", "DESC");
        PageHelper.startPage(searchVO.getPageNum(), searchVO.getPageSize(), getOrder(searchVO));
        return new PageInfo<BookListReturnVO>(iBookDao.queryList(searchVO));
    }

    @Override
    public Book model(Long id) throws Exception {
        return (Book) iBookDao.model(id);
    }

    @Override
    public List<IdNameModel<Long>> option() throws Exception {
        return iBookDao.option();
    }

    @Override
    public void checkName(IdNameModel<Long> checkName) throws Exception {
        if (iBookDao.checkName(checkName) > 0) {
            throw new RuntimeException("书籍已存在");
        }
    }

    @Override
    public void downloadBook(Long id, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Book book = (Book) iBookDao.model(id);
        isTaskStatus(book);
        Config config = (Config) iConfigDao.model(book.getConfigid());
        String titleNewLine = "", contentNewLine = "";
        for (int i = 0; i < config.getTitlelnnum(); i++) {
            titleNewLine += "\n";
        }
        for (int i = 0; i < config.getConlnnum(); i++) {
            contentNewLine += "\n";
        }
        try (
                OutputStream os = getDownloadOS(response, book.getName() + ".txt");
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os))
        ) {
            Map params = new HashMap<>(4, 1);
            params.put("bookid", book.getBookid());
            params.put("titleNewLine", titleNewLine);
            params.put("contentNewLine", contentNewLine);
            params.put("contentHead", "    ");
            List<String> chapterList = iBookDao.queryChapterDownloadList(params);
            for (String chapter : chapterList) {
                bw.write(chapter);
            }
            bw.flush();
        }
    }

    @Override
    public void downloadBook2(Long id, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Book book = (Book) iBookDao.model(id);
        isTaskStatus(book);
        Config config = (Config) iConfigDao.model(book.getConfigid());
        String titleNewLine = "", contentNewLine = "";
        for (int i = 0; i < config.getTitlelnnum(); i++) {
            titleNewLine += "\n";
        }
        for (int i = 0; i < config.getConlnnum(); i++) {
            contentNewLine += "\n";
        }
        try (
                OutputStream os = getDownloadOS(response, book.getName() + ".txt");
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os))
        ) {
            ChapterSearchVO chapSeaVO = new ChapterSearchVO();
            chapSeaVO.setBookid(book.getBookid());
            chapSeaVO.setOrderBy("\"seqid\"");
            chapSeaVO.setDescOrAsc("ASC");
            List<Chapter> chapList = iChapterDao.queryList(chapSeaVO);
            ParagraphSearchVO paraSeaVO = new ParagraphSearchVO();
            for (Chapter chapter : chapList) {
                paraSeaVO.setBookid(chapter.getBookid());
                paraSeaVO.setChapterid(chapter.getChapterid());
                bw.append(chapter.getName()).append(titleNewLine);
                List<Paragraph> paraList = iParagraphDao.queryList(paraSeaVO);
                for (Paragraph paragraph : paraList) {
                    bw.append("    ").append(paragraph.getContent()).append(contentNewLine);
                }
            }
            bw.flush();
        }
    }

    private void isTaskStatus(Book book) throws Exception {
        if (book.getTaskstatus() == null) {
            return;
        }
        if (book.getTaskstatus() == 1) {
            throw new ReStateException("正在执行重新获取任务,请稍后!");
        } else if (book.getTaskstatus() == 2) {
            throw new ReStateException("正在执行获取后续任务,请稍后!");
        }
    }
}

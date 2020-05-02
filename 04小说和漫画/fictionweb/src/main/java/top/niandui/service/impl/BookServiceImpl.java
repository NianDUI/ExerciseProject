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
import top.niandui.model.Config;
import top.niandui.model.vo.BookListReturnVO;
import top.niandui.model.vo.BookSearchVO;
import top.niandui.service.IBookService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        isTaskStatus((Book) iBookDao.model(id));
        // 删除章节
        iChapterDao.deleteByBookId(id);
        // 删除段落
        iParagraphDao.deleteByBookId(id);
        iBookDao.delete(id);
    }

    @Override
    public PageInfo<BookListReturnVO> queryList(BookSearchVO searchVO) throws Exception {
//        addDefaultSort(searchVO, "createtime", "DESC");
        PageHelper.startPage(searchVO.getPageNum(), searchVO.getPageSize(), getOrder(searchVO));
        return new PageInfo<BookListReturnVO>(iBookDao.queryList(searchVO));
    }

    @Override
    public Book model(Long id) throws Exception {
        return (Book) iBookDao.model(id);
    }

    @Override
    public List<IdNameModel> option() throws Exception {
        return iBookDao.option();
    }

    @Override
    public void checkName(IdNameModel checkName) throws Exception {
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
        response.reset();
        response.setHeader("Accept-Ranges", "bytes");
//        response.setHeader("Content-Length", String.valueOf(file.length()));
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(book.getName(), "UTF-8") + ".txt");
        try (
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(response.getOutputStream()))
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

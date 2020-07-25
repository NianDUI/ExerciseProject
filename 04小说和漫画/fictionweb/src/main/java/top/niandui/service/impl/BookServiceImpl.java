package top.niandui.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import top.niandui.common.base.BaseServiceImpl;
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
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static top.niandui.common.uitls.MethodUtil.*;
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
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private SqlSessionFactory sqlSessionFactory;

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
            checkTaskStatus(((Book) iBookDao.model(Long.valueOf(sId))).getTaskstatus());
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

    // 书籍copy导出外部SQL
    public static final String COPY_OUT_SQL = "(SELECT CASE ROW_NUMBER() OVER() % 2 WHEN 1 THEN t.\"content\" WHEN 0 THEN '' END " +
            " FROM ([contentSql]) t,(VALUES (0),(1)) repeat(\"repeat\"))";

    @Override
    public void downloadBook(Long id, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Book book = (Book) iBookDao.model(id);
        OutputStream os = getDownloadOS(response, book.getName() + ".txt");
        String statementId = "queryContentList";
        Map params = Collections.singletonMap("bookid", book.getBookid());
        String contentSql = getExecuteSql(sqlSessionFactory.getConfiguration(), statementId, params);
        String table = COPY_OUT_SQL.replace("[contentSql]", contentSql);
        copyOut(jdbcTemplate, os, table, "^", "UTF-8");
    }

    @Override
    public void downloadBook2(Long id, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Book book = (Book) iBookDao.model(id);
        checkTaskStatus(book.getTaskstatus());
        Config config = (Config) iConfigDao.model(book.getConfigid());
        String titleWrap = "", contentWrap = "";
        for (int i = 0; i < config.getTitlelnnum(); i++) {
            titleWrap += "\n";
        }
        for (int i = 0; i < config.getConlnnum(); i++) {
            contentWrap += "\n";
        }
        try (
                OutputStream os = getDownloadOS(response, book.getName() + ".txt");
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os, StandardCharsets.UTF_8))
        ) {
            List<Map> list = iBookDao.queryContentList(book.getBookid());
            for (Map map : list) {
                String content = convert(map.get("content"), Function.identity(), String.class);
                bw.append(content);
                Integer type = convert(map.get("type"), Integer::valueOf, Integer.class);
                if (type == 0) {
                    bw.append(titleWrap);
                } else {
                    bw.append(contentWrap);
                }
            }
            bw.flush();
        }
    }
}

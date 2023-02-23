package top.niandui.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import top.niandui.common.base.BaseServiceImpl;
import top.niandui.common.model.IdNameModel;
import top.niandui.common.uitls.MethodUtil;
import top.niandui.common.uitls.redis.RedisUtil;
import top.niandui.dao.IBookDao;
import top.niandui.dao.IChapterDao;
import top.niandui.dao.IConfigDao;
import top.niandui.dao.IParagraphDao;
import top.niandui.model.Book;
import top.niandui.model.Config;
import top.niandui.model.vo.BookListReturnVO;
import top.niandui.model.vo.BookSearchVO;
import top.niandui.service.IBookService;
import top.niandui.utils.HandleUtil;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

import static top.niandui.common.uitls.MethodUtil.*;
import static top.niandui.common.uitls.file.DownloadUtil.getDownloadOS;
import static top.niandui.config.PublicConstant.BOOK_TASK_STATUS;
import static top.niandui.utils.TaskStateUtil.checkTaskStatus;
import static top.niandui.utils.TaskStateUtil.getBookTaskStatus;

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
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private ObjectMapper json;

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
        String infoRaw = iBookDao.queryHandlerinfo(book.getBookid());
        iBookDao.update(book);
        Map infoMap = json.readValue(book.getHandlerinfo(), Map.class);
        infoMap.remove("proxyid");
        Map infoRawMap = json.readValue(infoRaw, Map.class);
        infoRawMap.remove("proxyid");
        if (!infoMap.equals(infoRawMap)) {
            infoMap.put("bookid", book.getBookid());
            /* 旧
            // 更新章节名称
            iChapterDao.updateName(infoMap);
            // 更新null章节名称为原名称
            iChapterDao.updateNullNameToRawname(book.getBookid());*/
            // 新处理方式 2022-1-9 19:47:59
            // 查询章节名列表
            List<Map> chapterNameList = iChapterDao.queryChapterNameList(Collections.singletonMap("bookid", book.getBookid()));
            if (chapterNameList.isEmpty()) {
                // 是空退出
                return;
            }
            // 获取标题处理器
            Function<String, String> titleHandler = HandleUtil.getTitleHandler(infoMap);
            // 章节序号
            int seqNum = 1;
            for (Map chapterName : chapterNameList) {
                // 章节序号，先取值在加1
                infoMap.put("seqNum", seqNum++);
                // 按照新的方式处理原标题
                String name = titleHandler.apply(convert(chapterName.get("rawname"), Objects::toString, String.class));
                chapterName.put("name", name);
            }
            // 通过id更新批量章节名
            MethodUtil.batch(chapterNameList, iChapterDao::updateBatchChapterNameById, 2000);
        }
    }

    @Override
    public void delete(String id) throws Exception {
        String[] ids = id.split(",");
        for (String sId : ids) {
            checkTaskStatus(getBookTaskStatus(Long.valueOf(sId)));
        }
        // 删除段落
        iParagraphDao.deleteByBookId(id);
        // 删除章节
        iChapterDao.deleteByBookId(id);
        iBookDao.delete(id);
        redisUtil.hDel(BOOK_TASK_STATUS, ids);
    }

    @Override
    public PageInfo<BookListReturnVO> queryList(BookSearchVO searchVO) throws Exception {
        addDefaultSort(searchVO, "b.\"bookid\"", "DESC");
        PageHelper.startPage(searchVO.getPageNum(), searchVO.getPageSize(), getOrder(searchVO));
        return new PageInfo<BookListReturnVO>(iBookDao.queryList(searchVO));
    }

    @Override
    public Book model(Long id) throws Exception {
        return (Book) iBookDao.model(id);
    }

    @Override
    public List<IdNameModel<Long>> option() throws Exception {
        PageHelper.orderBy(getOrder("bookid", "DESC"));
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
        String statementId = "queryContentList";
        Map params = Collections.singletonMap("bookid", book.getBookid());
        String contentSql = getExecuteSql(sqlSessionFactory.getConfiguration(), statementId, params);
        String table = COPY_OUT_SQL.replace("[contentSql]", contentSql);
        try (OutputStream os = getDownloadOS(response, book.getName() + ".txt")) {
            copyOut(jdbcTemplate, os, table, "^", "UTF-8");
        }
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
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
                    getDownloadOS(response, book.getName() + ".txt"), StandardCharsets.UTF_8))) {
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

    @Override
    public List<IdNameModel<Integer>> optionProxy() throws Exception {
        return iBookDao.optionProxy();
    }
}

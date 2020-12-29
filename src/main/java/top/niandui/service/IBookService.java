package top.niandui.service;

import com.github.pagehelper.PageInfo;
import top.niandui.common.model.IdNameModel;
import top.niandui.model.Book;
import top.niandui.model.vo.BookListReturnVO;
import top.niandui.model.vo.BookSearchVO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author 李永达
 * @version 1.0
 * @title IBookService.java
 * @description 书籍Service
 * @time: 2020/4/6 11:08
 */
public interface IBookService {
    // 新增
    void create(Book book) throws Exception;

    // 修改
    void update(Book book) throws Exception;

    // 删除
    void delete(String id) throws Exception;

    // 查询列表
    PageInfo<BookListReturnVO> queryList(BookSearchVO searchVO) throws Exception;

    // 查询单条
    Book model(Long id) throws Exception;

    // 查询下拉
    List<IdNameModel<Long>> option() throws Exception;

    // 重名校验
    void checkName(IdNameModel<Long> checkName) throws Exception;

    // 下载书籍
    void downloadBook(Long id, HttpServletRequest request, HttpServletResponse response) throws Exception;

    // 下载书籍2(多次查询sql)
    void downloadBook2(Long id, HttpServletRequest request, HttpServletResponse response) throws Exception;

    // 代理下拉
    List<IdNameModel<Integer>> optionProxy() throws Exception;
}
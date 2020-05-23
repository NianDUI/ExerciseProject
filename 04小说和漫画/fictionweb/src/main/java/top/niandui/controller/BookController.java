package top.niandui.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.niandui.common.base.BaseController;
import top.niandui.common.model.IdNameModel;
import top.niandui.common.model.PageList;
import top.niandui.common.model.ResponseData;
import top.niandui.model.Book;
import top.niandui.model.vo.BookListReturnVO;
import top.niandui.model.vo.BookSearchVO;
import top.niandui.service.IBookService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author 李永达
 * @version 1.0
 * @title BookController.java
 * @description 书籍
 * @time: 2020/4/6 11:11
 */
@RestController
@Api(tags = "书籍")
@RequestMapping("/api")
public class BookController extends BaseController {
    @Autowired
    private IBookService iBookService;

    @PostMapping("/saveBook")
    @ApiOperation(value = "保存书籍", notes = "<br>开发人：李永达<br>时间：2020/04/06<br>无id为新增,有id为修改")
    public ResponseData saveBook(@RequestBody @Validated Book book) throws Exception {
        if (book.getBookid() == null) {
            iBookService.create(book);
        } else {
            iBookService.update(book);
        }
        return ResponseData.ok();
    }

    @PostMapping("/checkBookName")
    @ApiOperation(value = "书籍重名校验", notes = "<br>开发人：李永达<br>时间：2020/04/06<br>")
    public ResponseData checkBookName(@RequestBody @Validated IdNameModel<Long> checkName) throws Exception {
        iBookService.checkName(checkName);
        return ResponseData.ok();
    }

    @GetMapping("/modelBook/{id}")
    @ApiOperation(value = "查询单个书籍", notes = "<br>开发人：李永达<br>时间：2020/04/06<br>")
    @ApiImplicitParams(@ApiImplicitParam(name = "id", value = "书籍id", dataType = "Long", required = true))
    public ResponseData<Book> modelBook(@PathVariable Long id) throws Exception {
        return ResponseData.ok(iBookService.model(id));
    }

    @PostMapping("/queryBookList")
    @ApiOperation(value = "查询书籍列表", notes = "<br>开发人：李永达<br>时间：2020/04/06<br>")
    public ResponseData<PageList<BookListReturnVO>> queryBookList(@RequestBody @Validated BookSearchVO bookSearchVO) throws Exception {
        return ResponseData.ok(iBookService.queryList(bookSearchVO));
    }

    @GetMapping("/optionBook")
    @ApiOperation(value = "书籍下拉", notes = "<br>开发人：李永达<br>时间：2020/04/06<br>")
    public ResponseData<List<IdNameModel<Long>>> optionBook() throws Exception {
        return ResponseData.ok(iBookService.option());
    }

    @GetMapping("/deleteBook/{id}")
    @ApiOperation(value = "删除书籍", notes = "<br>开发人：李永达<br>时间：2020/04/06<br>")
    @ApiImplicitParams(@ApiImplicitParam(name = "id", value = "书籍id,多个用逗号隔开", dataType = "String", required = true))
    public ResponseData deleteBook(@PathVariable String id) throws Exception {
        iBookService.delete(id);
        return ResponseData.ok();
    }

    @GetMapping("/downloadBook/{id}")
    @ApiOperation(value = "下载书籍(一次查询sql)", notes = "<br>开发人：李永达<br>时间：2020/04/06<br>")
    @ApiImplicitParams(@ApiImplicitParam(name = "id", value = "书籍id", dataType = "Long", required = true))
    public void downloadBook(@PathVariable Long id, HttpServletRequest request, HttpServletResponse response) throws Exception {
        iBookService.downloadBook(id, request, response);
    }

    @GetMapping("/downloadBook2/{id}")
    @ApiOperation(value = "下载书籍2(多次查询sql)", notes = "<br>开发人：李永达<br>时间：2020/04/06<br>")
    @ApiImplicitParams(@ApiImplicitParam(name = "id", value = "书籍id", dataType = "Long", required = true))
    public void downloadBook2(@PathVariable Long id, HttpServletRequest request, HttpServletResponse response) throws Exception {
        iBookService.downloadBook2(id, request, response);
    }
}

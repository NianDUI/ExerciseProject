package top.niandui.controller;

import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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

import java.util.List;

/**
 * @author 李永达
 * @version 1.0
 * @title BookController.java
 * @description 书籍
 * @time: 2020/4/6 11:11
 */
@RestController
@Tag(name = "书籍")
@RequestMapping("/api")
@ApiSupport(author = "李永达")
public class BookController extends BaseController {
    @Autowired
    private IBookService iBookService;

    @PostMapping("/saveBook")
    @Operation(summary = "保存书籍", description = "时间：2020/04/06<br>无id为新增,有id为修改")
    public ResponseData saveBook(@RequestBody @Validated Book book) throws Exception {
        if (book.getBookid() == null) {
            iBookService.create(book);
        } else {
            iBookService.update(book);
        }
        return ResponseData.ok();
    }

    @PostMapping("/checkBookName")
    @Operation(summary = "书籍重名校验", description = "时间：2020/04/06")
    public ResponseData checkBookName(@RequestBody @Validated IdNameModel<Long> checkName) throws Exception {
        iBookService.checkName(checkName);
        return ResponseData.ok();
    }

    @GetMapping("/modelBook/{id}")
    @Operation(summary = "查询单个书籍", description = "时间：2020/04/06")
    @Parameters(@Parameter(name = "id", description = "书籍id", required = true))
    public ResponseData<Book> modelBook(@PathVariable Long id) throws Exception {
        return ResponseData.ok(iBookService.model(id));
    }

    @PostMapping("/queryBookList")
    @Operation(summary = "查询书籍列表", description = "时间：2020/04/06")
    public ResponseData<PageList<BookListReturnVO>> queryBookList(@RequestBody @Validated BookSearchVO bookSearchVO) throws Exception {
        return ResponseData.ok(iBookService.queryList(bookSearchVO));
    }

    @GetMapping("/optionBook")
    @Operation(summary = "书籍下拉", description = "时间：2020/04/06")
    public ResponseData<List<IdNameModel<Long>>> optionBook() throws Exception {
        return ResponseData.ok(iBookService.option());
    }

    @GetMapping("/deleteBook/{id}")
    @Operation(summary = "删除书籍", description = "时间：2020/04/06")
    @Parameters(@Parameter(name = "id", description = "书籍id,多个用逗号隔开", required = true))
    public ResponseData deleteBook(@PathVariable String id) throws Exception {
        iBookService.delete(id);
        return ResponseData.ok();
    }

    @GetMapping("/downloadBook/{id}")
    @Operation(summary = "下载书籍(一次查询sql)", description = "时间：2020/04/06")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_OCTET_STREAM_VALUE))
    @Parameters(@Parameter(name = "id", description = "书籍id", required = true))
    public void downloadBook(@PathVariable Long id, HttpServletRequest request, HttpServletResponse response) throws Exception {
        iBookService.downloadBook(id, request, response);
    }

    @GetMapping("/downloadBook2/{id}")
    @Operation(summary = "下载书籍2(多次查询sql)", description = "时间：2020/04/06")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_OCTET_STREAM_VALUE))
    @Parameters(@Parameter(name = "id", description = "书籍id", required = true))
    public void downloadBook2(@PathVariable Long id, HttpServletRequest request, HttpServletResponse response) throws Exception {
        iBookService.downloadBook2(id, request, response);
    }
}

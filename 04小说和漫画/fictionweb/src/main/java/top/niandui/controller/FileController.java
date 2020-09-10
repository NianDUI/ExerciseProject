package top.niandui.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.niandui.common.model.ResponseData;
import top.niandui.model.Papers;
import top.niandui.service.IFileService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @Title: FileController.java
 * @description: 文件
 * @time: 2020/8/20 16:47
 * @author: liyongda
 * @version: 1.0
 */
@Slf4j
@RestController
@Api(tags = "文件")
@RequestMapping("/api/file")
public class FileController {
    @Autowired
    private IFileService iFileService;

    @GetMapping("/read")
    @ApiOperation(value = "读取文件", notes = "<br>开发人：李永达<br>时间：2020/08/21<br>")
    @ApiImplicitParam(value = "路径", name = "path", dataType = "String", required = true)
    public void read(@RequestParam String path, HttpServletResponse response) throws Exception {
        iFileService.read(path, response);
    }

    @GetMapping("/list/**")
    @ApiOperation(value = "文件列表", notes = "<br>开发人：李永达<br>时间：2020/09/09<br>")
    public ResponseData<List<Papers>> list(HttpServletRequest request) throws Exception {
        return ResponseData.ok(iFileService.list(request));
    }

    @GetMapping("/download/**")
    @ApiOperation(value = "下载文件", notes = "<br>开发人：李永达<br>时间：2020/04/06<br>支持断点续传", produces = "application/octet-stream")
    public void download(HttpServletRequest request, HttpServletResponse response) throws Exception {
        iFileService.download(request, response);
    }
}

package top.niandui.controller;

import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.niandui.common.model.ResponseData;
import top.niandui.model.Papers;
import top.niandui.service.IFileService;

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
@Tag(name = "文件")
@RequestMapping("/api/file")
@ApiSupport(author = "李永达")
public class FileController {
    @Autowired
    private IFileService iFileService;

    @GetMapping("/read")
    @Operation(summary = "读取文件", description = "时间：2020/08/21")
    @Parameter(description = "路径", name = "path", required = true)
    public void read(@RequestParam String path, HttpServletResponse response) throws Exception {
        iFileService.read(path, response);
    }

    @GetMapping("/list/**")
    @Operation(summary = "文件列表", description = "时间：2020/09/09")
    public ResponseData<List<Papers>> list(HttpServletRequest request) throws Exception {
        return ResponseData.ok(iFileService.list(request));
    }

    @GetMapping("/download/**")
    @Operation(summary = "下载文件", description = "时间：2020/04/06<br>支持断点续传"
            , responses = @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_OCTET_STREAM_VALUE))
    )
    public void download(HttpServletRequest request, HttpServletResponse response) throws Exception {
        iFileService.download(request, response);
    }
}

package top.niandui.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.niandui.config.ConfigInfo;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.nio.file.Files;

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
    private ConfigInfo configInfo;

    @GetMapping("/read")
    @ApiOperation(value = "读取文件", notes = "<br>开发人：李永达<br>时间：2020/08/21<br>")
    @ApiImplicitParam(value = "路径", name = "path", dataType = "String", required = true)
    public void read(@RequestParam String path, HttpServletResponse response) throws Exception {
        if (!StringUtils.isEmpty(path)) {
            File file = new File(configInfo.getFilePath(), path.replace("..", "."));
            if (file.exists() && file.isFile()) {
                ServletOutputStream os = response.getOutputStream();
                Files.copy(file.toPath(), os);
                os.close();
                log.info("读取文件：" + file.getAbsolutePath());
            }
        }
    }
}

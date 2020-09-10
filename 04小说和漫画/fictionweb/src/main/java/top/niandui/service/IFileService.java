package top.niandui.service;

import top.niandui.model.Papers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @Title: IFileService.java
 * @description: 文件Service
 * @time: 2020/9/9 11:07
 * @author: liyongda
 * @version: 1.0
 */
public interface IFileService {
    // 读取文件
    void read(String path, HttpServletResponse response) throws Exception;

    // 列表
    List<Papers> list(HttpServletRequest request) throws Exception;

    // 列表
    List<Papers> list(String path) throws Exception;

    // 下载文件
    void download(HttpServletRequest request, HttpServletResponse response) throws Exception;
}

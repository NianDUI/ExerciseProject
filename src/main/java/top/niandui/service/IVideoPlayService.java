package top.niandui.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import top.niandui.model.vo.VideoPlaySourceVO;

public interface IVideoPlayService {
    VideoPlaySourceVO resolveSource(HttpServletRequest request) throws Exception;

    VideoPlaySourceVO resolveSourceByPath(String path) throws Exception;

    void streamHls(String cacheKey, String fileName, HttpServletResponse response) throws Exception;
}

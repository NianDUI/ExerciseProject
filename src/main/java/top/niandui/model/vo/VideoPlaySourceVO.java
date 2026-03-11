package top.niandui.model.vo;

import lombok.Data;

/**
 * 视频播放源
 */
@Data
public class VideoPlaySourceVO {
    private String status;
    private String title;
    private String sourceUrl;
    private String mimeType;
    private Boolean transcoded;
    private String message;
}

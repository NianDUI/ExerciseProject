package top.niandui.common.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import top.niandui.common.base.BaseModel;
import top.niandui.common.outher.StatusCode;

/**
 * 公共返回数据
 *
 * @author liyongda
 * @version 1.0
 * @date 2020/3/22 16:45
 */
@Data
@Schema(description = "公共返回数据")
public class ResponseData<T> extends BaseModel {
    @Schema(description = "响应码")
    private int code = StatusCode.EXECUTE_SUCCESS;
    @Schema(description = "响应消息")
    private String message = "";
    @Schema(description = "响应内容")
    private T data;

    public ResponseData() {
    }

    public ResponseData(T data) {
        this.data = data;
    }

    public ResponseData(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public static ResponseData ok() {
        return new ResponseData();
    }

    public static <T> ResponseData ok(T data) {
        return new ResponseData(data);
    }

    public static ResponseData fail(int code, String message) {
        return new ResponseData(code, message);
    }
}

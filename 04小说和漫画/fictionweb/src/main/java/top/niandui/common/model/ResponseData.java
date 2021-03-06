package top.niandui.common.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel(description = "公共返回数据")
public class ResponseData<T> extends BaseModel {
    @ApiModelProperty(value = "响应码")
    private int code = StatusCode.EXECUTE_SUCCESS;
    @ApiModelProperty(value = "响应消息")
    private String message = "";
    @ApiModelProperty(value = "响应内容")
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

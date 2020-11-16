package top.niandui.common.outher;

/**
 * @Title: StatusCode.java
 * @description: 统一状态码
 * @time: 2020/3/22 16:48
 * @author: liyongda
 * @version: 1.0
 */
public class StatusCode {
    // 成功状态码
    public static final int EXECUTE_SUCCESS = 200;
    // 失败状态码
    public static final int EXECUTE_FAIL = 500;

    // token错误
    public static final int TOKEN_ERROR = 401;

    // 参数格式错误码
    public static final int PARAM_FORMAT_ERROR = 1001;
    // 状态错误码
    public static final int RESTATE = 1002;
}

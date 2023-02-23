package top.niandui.common.config;

import jakarta.servlet.ServletException;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;
import top.niandui.common.expection.ReStateException;
import top.niandui.common.expection.TokenCheckException;
import top.niandui.common.outher.StatusCode;

import java.util.Map;

/**
 * 错误属性
 *
 * @author liyongda
 * @version 1.0
 * @date 2020/12/31 9:48
 */
@Component
public class ErrorAttributes extends DefaultErrorAttributes {
    @Override
    public Map<String, Object> getErrorAttributes(WebRequest webRequest, ErrorAttributeOptions options) {
        Map<String, Object> error = super.getErrorAttributes(webRequest, options);
        Throwable e = getError(webRequest);
        if (e instanceof ServletException && e.getCause() != null) {
            e = e.getCause();
            while (e instanceof ServletException && e.getCause() != null) {
                e = e.getCause();
            }
            // 添加返回code属性
            error.put("code", getExceptionCode(e));
            Object message = error.get("message");
            if (message != null && message.toString().length() == 0) {
                error.put("message", e.getMessage());
            }
        }
        return error;
    }

    /**
     * 获取错误相应的code码
     *
     * @param e 错误对象
     * @return code码
     */
    private int getExceptionCode(Throwable e) {
        if (e instanceof MethodArgumentNotValidException) {
            return StatusCode.PARAM_FORMAT_ERROR;
        } else if (e instanceof TokenCheckException) {
            return StatusCode.TOKEN_ERROR;
        } else if (e instanceof ReStateException) {
            return StatusCode.RESTATE;
        } else {
            return StatusCode.EXECUTE_FAIL;
        }
    }

}

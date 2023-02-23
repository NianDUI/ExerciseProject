package top.niandui.common.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import top.niandui.common.expection.ReStateException;
import top.niandui.common.expection.TokenCheckException;
import top.niandui.common.model.ResponseData;
import top.niandui.common.outher.StatusCode;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

/**
 * 全局异常处理器
 *
 * @author liyongda
 * @version 1.0
 * @date 2020/4/2 11:52
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    @Autowired
    private ObjectMapper json;

    /**
     * 全局异常处理
     *
     * @param response 响应流对象
     * @param e        错误对象
     */
    @ExceptionHandler({Exception.class})
    public void exceptionHandler(HttpServletResponse response, Exception e) {
        log.error("系统异常：", e);
        try (OutputStream os = response.getOutputStream()) {
            response.reset();
            response.setContentType("application/json;charset=utf-8");
            ResponseData rd;
            if (e instanceof MethodArgumentNotValidException) {
                rd = ResponseData.fail(StatusCode.PARAM_FORMAT_ERROR, ((MethodArgumentNotValidException) e).getBindingResult()
                        .getFieldError().getDefaultMessage());
            } else if (e instanceof TokenCheckException) {
                rd = ResponseData.fail(StatusCode.TOKEN_ERROR, e.getMessage());
            } else if (e instanceof ReStateException) {
                rd = ResponseData.fail(StatusCode.RESTATE, e.getMessage());
            } else if (e instanceof FeignException fe) {
                // Feign内部服务调用错误处理。在配置文件中设置feign.hystrix.enabled: false，可以拦截到
                try {
                    Map errInfo = json.readValue(fe.contentUTF8(), Map.class);
                    rd = ResponseData.fail((int) errInfo.get("code"), (String) errInfo.get("message"));
                } catch (Exception ex) {
                    rd = ResponseData.fail(StatusCode.EXECUTE_FAIL, e.getMessage());
                    log.error("Feign异常转换异常: " + ex.getMessage(), ex);
                }
            } else {
                rd = ResponseData.fail(StatusCode.EXECUTE_FAIL, e.getMessage());
            }
            json.writeValue(os, rd);
        } catch (IOException ex) {
            log.error("全局错误处理异常：", ex);
        }
    }

    /**
     * 全局Feign内部服务调用错误处理
     *
     * @param response 响应流
     * @param e        Feign调用错误
     */
//    @ExceptionHandler({FeignException.class})
    public void feignExceptionHandler(HttpServletResponse response, FeignException e) {
        response.setStatus(StatusCode.EXECUTE_FAIL);
        response.setContentType("application/json;charset=utf-8");
        Object message = "";
        ResponseData rd;
        try {
            Map errInfo = json.readValue(e.contentUTF8(), Map.class);
            rd = ResponseData.fail((int) errInfo.get("code"), (String) errInfo.get("message"));
            message = errInfo.get("message");
        } catch (Exception ex) {
            rd = ResponseData.fail(StatusCode.EXECUTE_FAIL, e.getMessage());
            log.error("Feign异常转换异常: " + ex.getMessage(), ex);
        }
        log.error("系统异常: " + message, e);
        try (OutputStream os = response.getOutputStream()) {
            json.writeValue(os, rd);
        } catch (IOException ex) {
            log.error("Feign错误处理异常", ex);
        }
    }
}

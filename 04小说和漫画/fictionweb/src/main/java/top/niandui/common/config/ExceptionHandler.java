package top.niandui.common.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import top.niandui.common.expection.ReStateException;
import top.niandui.common.model.ResponseData;
import top.niandui.common.outher.StatusCode;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @Title: ExceptionHandler.java
 * @description: 异常处理器
 * @time: 2020/4/2 11:52
 * @author: liyongda
 * @version: 1.0
 */
@Slf4j
@ControllerAdvice
public class ExceptionHandler {
    private ObjectMapper mapper = new ObjectMapper();

    @org.springframework.web.bind.annotation.ExceptionHandler({Exception.class})
    public void exceptionHandler(Exception e, HttpServletResponse response) {
        OutputStream os = null;
        try {
            response.reset();
            response.setContentType("application/json;charset=utf-8");
            os = response.getOutputStream();
            ResponseData rd;
            if (e instanceof MethodArgumentNotValidException) {
                rd = ResponseData.fail(StatusCode.PARAM_FORMAT_ERROR, ((MethodArgumentNotValidException) e).getBindingResult()
                        .getFieldError().getDefaultMessage());
            } else if (e instanceof ReStateException) {
                rd = ResponseData.fail(StatusCode.RESTATE, e.getMessage());
            } else {
                rd = ResponseData.fail(StatusCode.EXECUTE_FAIL, e.getMessage());
            }
            mapper.writeValue(os, rd);
        } catch (IOException ex) {
            ex.printStackTrace();
            log.error(ex.toString());
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                    log.error(ex.toString());
                }
            }
        }
    }
}

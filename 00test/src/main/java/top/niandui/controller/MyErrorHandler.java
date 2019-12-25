package top.niandui.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import top.niandui.exception.MyException;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 自定义错误处理器
 */
@ControllerAdvice
public class MyErrorHandler {

    @ExceptionHandler(MyException.class)
    public String myExceptionHandler(Exception e, HttpServletRequest request) {
        request.setAttribute("javax.servlet.error.status_code", 500);

        Map<String, Object> map = new HashMap<>();
        map.put("info", "自定义错误处理器 MyErrorHandler.myExceptionHandler() 传递的信息");

        request.setAttribute("MyErrorHandler", map);
        return "forward:/error";
    }

}

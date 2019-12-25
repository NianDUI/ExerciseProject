package top.niandui.controller;

import com.sun.xml.internal.ws.client.ResponseContextReceiver;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import top.niandui.annotation.MyToken;
import top.niandui.exception.MyException;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@RestController
public class TestController {

    @GetMapping("/testDate")
    public Date testDate(Date date) {
        System.out.println(date);
        return date;
    }

    @GetMapping("/testMyToken")
    public String testMyToken(@MyToken String token) {
        System.out.println(token);
        return token;
    }

    @GetMapping("/testRequest")
    public String testRequest(HttpServletRequest request1) {
        System.out.println(request1);
        HttpServletRequest request2 = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        System.out.println(request2);
        return "ok";
    }

    @RequestMapping("/testMyError")
    public String testMyError() throws MyException {
        throw new MyException("TestController.testMyError() 错误！");
    }
}

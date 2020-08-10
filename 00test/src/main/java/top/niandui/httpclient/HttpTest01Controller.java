package top.niandui.httpclient;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @Title: HttpTest01Controller.java
 * @description: Http请求测试
 * @time: 2020/7/29 17:54
 * @author: liyongda
 * @version: 1.0
 */
@Controller
@RequestMapping("/uniportal")
public class HttpTest01Controller {

    @ResponseBody
    @GetMapping("/frame/welcome.action")
    public String welcome(HttpSession session) {
        System.out.println("session = " + session);
        return "welcomeHTMLasfdasdfasdcontextDestroyed...销毁Servlet容器65461as6d51f6";
    }

    @ResponseBody
    @PostMapping("/frame/login.action")
    public String login(@RequestParam Boolean authCodeable, @RequestParam String userName
            ,  @RequestParam String password, @RequestParam String validateCode
            , @RequestParam("frame.login.label.login") String info, HttpSession session) {
        System.out.println("session = " + session);
        System.out.println("authCodeable = " + authCodeable);
        System.out.println("userName = " + userName);
        System.out.println("password = " + password);
        System.out.println("validateCode = " + validateCode);
        System.out.println("info = " + info);
        return "loginasfadfasdfasd.asdf54a1sdf\nasdfad\nasdf";
    }

    @GetMapping("/authimg")
    public void authimg(HttpServletRequest request, HttpServletResponse response) throws Exception {
        System.out.println("session = " + request.getSession());
        response.setHeader("Content-Type", "image/jpeg");
        ServletOutputStream os = response.getOutputStream();
        String info = "4cf\n" +
                "     \u0010JFIF \u0001\u0002  \u0001 \u0001     C \b\u0006\u0006 \u0006\u0005\b   \t\t\b\n" +
                "\f\u0014\n" +
                "\f\n" +
                "\n" +
                "\f\u0019\u0012\u0013\u000F\u0014\u001D\u001A\u001F\u001E\u001D\u001A\u001C\u001C $.' \",#\u001C\u001C(7),01444\u001F'9=82<.342   C\u0001\t\t\t\f\n" +
                "\f\u0018\n" +
                "\n" +
                "\u00182!\u001C!222222222222222\n";
        os.write(info.getBytes());
    }
}

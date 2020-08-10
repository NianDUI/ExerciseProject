package top.niandui.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import top.niandui.annotation.MyToken;
import top.niandui.exception.MyException;

import javax.servlet.http.HttpServletRequest;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
public class TestController {
//    @Value("${baseUrl}")
    private String baseUrl;
//    @Value("${urlThreadCommandFileTrans}")
    private String urlThreadCommandFileTrans;

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
        System.out.println("baseUrl = " + baseUrl);
        System.out.println("urlThreadCommandFileTrans = " + urlThreadCommandFileTrans);
        System.out.println(request1);
        HttpServletRequest request2 = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        System.out.println(request2);
        return "ok";
    }

    @RequestMapping("/testMyError")
    public String testMyError() throws MyException {
        throw new MyException("TestController.testMyError() 错误！");
    }

    @PostMapping("/threadCommandFileTrans")
    public Object threadCommandFileTrans(
            @RequestParam(value = "f_file") MultipartFile[] f_files,
            @RequestParam(value = "f_serviceid") Integer f_serviceid,
            @RequestParam(value = "f_template") String f_template,
            @RequestParam(value = "f_threadnum") Integer f_threadnum) throws Exception {

        System.out.println("f_serviceid = " + f_serviceid);
        System.out.println("f_template = " + f_template);
        System.out.println("f_threadnum = " + f_threadnum);

        for (MultipartFile f_file : f_files) {
            String originalFilename = f_file.getOriginalFilename();
            System.out.println("originalFilename = " + originalFilename);

            InputStream is = f_file.getInputStream();
            FileOutputStream os = new FileOutputStream("d:/测试上传文件_" + originalFilename);
            int l;
            byte[] bytes = new byte[1024];
            while ((l = is.read(bytes)) != -1) {
                os.write(bytes, 0, l);
            }
            os.close();
            is.close();
        }


        Map rv = new HashMap();
        rv.put("data", "成功");
        rv.put("message", "我们成功了");
        return rv;
    }
}

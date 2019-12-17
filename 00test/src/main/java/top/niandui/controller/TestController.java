package top.niandui.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
public class TestController {

    @GetMapping("/testDate")
    public Date testDate(Date date) {
        System.out.println(date);
        return date;
    }
}

package top.niandui.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.val;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Title: UserController.java
 * @description: TODO
 * @time: 2019/11/21 11:27
 * @author: liyongda
 * @version: 1.0
 */
@RestController
@RequestMapping("/user")
@Api(value = "user模块", tags = "user模块 tags")
public class UserController {

    @GetMapping("/testGet")
    @ApiOperation(value = "测试接口testGet", notes = "notes")
    public String testGet() {
        System.out.println("执行了。。。 UserController.testGet()");

        return "success";
    }

    @PostMapping("/testPost")
    @ApiOperation(value = "测试接口testPost", notes = "notes")
    public String testPost() {
        System.out.println("执行了。。。 UserController.testPost()");

        return "success";
    }

}

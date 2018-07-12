package com.zym.Design1.tttest;

import com.zym.Design1.entity.Notice;
import com.zym.Design1.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by YM on 2018/4/16.
 */
//@Controller
public class TestController {

    @Autowired
    private TestServiceA testServiceA;
    @Autowired
    private TestServiceB testServiceB;

    @ResponseBody
    @RequestMapping("/testA")
    public String test() {
        testServiceA.addUser(new User());
        testServiceA.add1User();
        return "test controller test()";
    }

    @ResponseBody
    @RequestMapping("/testB")
    public String testB() {
        testServiceB.addNotice(new Notice());
        return "test controller test()";
    }

    @ResponseBody
    @RequestMapping("/testC")
    public String testc() {
        testServiceB.addNotice3(new Notice());
        return "test controller test()";
    }

}

package com.zym.Design1.tttest;

import com.zym.Design1.entity.Notice;
import com.zym.Design1.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by YM on 2018/4/16.
 */
//@Controller
//@Transactional
public class TestController2 {

    @Autowired
    private TestServiceA testServiceA;
    @Autowired
    private TestServiceB testServiceB;

    @ResponseBody
    @RequestMapping("/testA2")
    public String test() {
        testServiceA.addUser(new User());
        testServiceA.add1User();
        return "test controller test()";
    }

    @ResponseBody
    @RequestMapping("/testB2")
    public String testB() {
        testServiceB.addNotice(new Notice());
        return "test controller test()";
    }

    @ResponseBody
    @RequestMapping("/testC2")
    public String testc() {
        testServiceB.addNotice3(new Notice());
        testServiceA.addUser(new User());
        return "test controller test()";
    }

}

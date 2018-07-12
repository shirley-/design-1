package com.zym.Design1;

import com.alibaba.fastjson.JSONObject;
import com.zym.Design1.dao.UserMapper;
import com.zym.Design1.entity.User;
import com.zym.Design1.service.OrgService;
import com.zym.Design1.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;


@RunWith(SpringRunner.class)
@SpringBootTest
public class TestService {
    @Autowired
    private OrgService orgService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    @Test
    public void test1() {
        Map<Integer, User> map = new HashMap<>();
        orgService.getDescendantsMap(2, map);
        System.out.println("123");
    }
    @Test
    public void test2() {
        Map<Integer, User> map = new HashMap<>();
        orgService.getAncestorsMap(9, map);
        System.out.println("123");
    }


    @Test
    public void test3() {
        User user = userService.getUserById(6);
        JSONObject.toJSONString(user);
        System.out.println("123");
    }



}

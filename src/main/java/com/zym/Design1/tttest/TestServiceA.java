package com.zym.Design1.tttest;

import com.zym.Design1.dao.UserMapper;
import com.zym.Design1.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * Created by YM on 2018/4/16.
 */
//@Service
//@Transactional
public class TestServiceA {

    @Autowired
    private UserMapper userMapper;

    public void addUser(User user) {
        user.setDepositBank("serviceA adduser");
        user.setUid("serviceA ok");
        userMapper.insertSelective(user);
        User u2 = new User();
        u2.setDepositBank("serviceA 回滚");
        u2.setUid("xx01");
        userMapper.insertSelective(u2);
    }

    public void add1User() {
        User u = new User();
        u.setUid("22" + new Date());
        userMapper.insertSelective(u);
    }


}

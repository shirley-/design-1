package com.zym.Design1.service;

import com.zym.Design1.dao.UserMapper;
import com.zym.Design1.entity.User;
import com.zym.Design1.entity.UserExample;
import com.zym.Design1.util.ConstantUtil;
import com.zym.Design1.util.RedisKeyUtil;
import com.zym.Design1.util.ResultUtil;
import com.zym.Design1.util.ProjectUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Created on 2018/3/6.
 */
@Slf4j
@Service
public class LoginService {

    @Autowired
    private UserService userService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Transactional
    public Map<String, Object> login(String uid, String password) {
        Map<String, Object> resultMap = new HashMap<>();
        User userByUid = userService.getUserByUid(uid);
        //name对不对
        if(userByUid == null ) {
            resultMap.put("code", ResultUtil.ERROR);
        }else {
            //name对
            //验证password
            if(userService.checkPassword(userByUid, password)) {
                //sessionId
                resultMap.put("code", ResultUtil.SUCCESS);
                String ticket = addLoginTicket(userByUid.getId());
                resultMap.put("ticket", ticket);
                resultMap.put("role", userByUid.getRole());

            } else {
                resultMap.put("code", ResultUtil.ERROR);
            }
        }
        log.info("userUid: {}, resultMap:{} ", uid, resultMap);
        return resultMap;
    }

    @Transactional
    public void logout(String ticket) {
        String ticketKey = RedisKeyUtil.getUserSessionKey(ticket);
        redisTemplate.delete(ticketKey);
        log.info("delete redis key: {}", ticket);
    }


    @Transactional
    private String addLoginTicket(Integer userId) {
        String ticket = UUID.randomUUID().toString();
        log.info(ticket);
        String ticketKey = RedisKeyUtil.getUserSessionKey(ticket);
        redisTemplate.opsForValue().set(ticketKey, String.valueOf(userId), ConstantUtil.COOKIE_TIMEOUT, TimeUnit.SECONDS);
        log.info("key- ticketKey:{}, value- userId:{}", ticketKey, userId);
        return ticket;
    }


}

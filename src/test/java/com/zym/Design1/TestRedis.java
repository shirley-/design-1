package com.zym.Design1;

import com.zym.Design1.entity.User;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by YM on 2018/4/5.
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestRedis {

    @Autowired
    public RedisTemplate redisTemplate;

    @org.junit.Test
    public void test1() {
        User u = new User();
        u.setId(123);
        u.setName("eee");
        Map<String, User> map = new HashMap<>();
        map.put(u.getName(), u);
        redisTemplate.opsForHash().putAll("test", map);
        List list = redisTemplate.opsForHash().values("test");
        System.out.println(((User)list.get(0)).getId());
    }
}

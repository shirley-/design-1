package com.zym.Design1.service;

import com.zym.Design1.util.ConstantUtil;
import com.zym.Design1.util.RedisKeyUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;


/**
 * Created on 2018/4/13.
 */
@Service
@Slf4j
public class PhaseService {
    //REDIS EARLY_PHASE = 0 前期；  后期 1

    @Autowired
    private StringRedisTemplate redisTemplate;

    public void closeEarlyPhase() {
        log.info("closeEarlyPhase");
        redisTemplate.opsForValue().set(RedisKeyUtil.getPhaseKey(), ConstantUtil.STATE_INVALID);
    }

    public String getEarlyPhaseState() {
        String s = redisTemplate.opsForValue().get(RedisKeyUtil.getPhaseKey());
        log.info("getEarlyPhaseState:{}", s);
        return s;
    }

}

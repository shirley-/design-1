package com.zym.Design1.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zym.Design1.dao.CurrencyChangeMapper;
import com.zym.Design1.entity.CurrencyChange;
import com.zym.Design1.entity.CurrencyChangeExample;
import com.zym.Design1.util.RedisKeyUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created on 2018/3/18.
 */
@Slf4j
@Service
public class CurrencyService {
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private CurrencyChangeMapper currencyChangeMapper;

    @Transactional
    public void setCurrency(BigDecimal price) {
        redisTemplate.opsForValue().set(RedisKeyUtil.getCurrencyKey(), String.valueOf(price));
    }

    public BigDecimal getCurrency() {
        return new BigDecimal(redisTemplate.opsForValue().get(RedisKeyUtil.getCurrencyKey())==null?
                "0" : redisTemplate.opsForValue().get(RedisKeyUtil.getCurrencyKey()));
    }

    /*public void setCurrencyCount(Integer count) {
        redisTemplate.opsForValue().set(RedisKeyUtil.getCurrencyCountKey(), String.valueOf(count));
    }*/

    public Double getCurrencyCount() {
        log.info("CURRENCT_COUNT: {}", redisTemplate.opsForValue().get(RedisKeyUtil.getCurrencyCountKey())==null? "0" :redisTemplate.opsForValue().get(RedisKeyUtil.getCurrencyCountKey()));
        return Double.valueOf(redisTemplate.opsForValue().get(RedisKeyUtil.getCurrencyCountKey())==null? "0" :redisTemplate.opsForValue().get(RedisKeyUtil.getCurrencyCountKey()));
    }

    @Transactional
    public Double incrementCurrencyCount(BigDecimal addCount) {
        log.info("CURRENCY_COUNT, increase:{}, total:{}", addCount, redisTemplate.opsForValue().get(RedisKeyUtil.getCurrencyCountKey()));
        return redisTemplate.opsForValue().increment(RedisKeyUtil.getCurrencyCountKey(), addCount.setScale(4, BigDecimal.ROUND_HALF_DOWN).doubleValue());
    }

    public JSONArray getTrendData() {
        List<CurrencyChange> currencyChanges = currencyChangeMapper.selectByExample(new CurrencyChangeExample());
        if(currencyChanges!=null && currencyChanges.size()>0) {
            JSONArray jsonArray = new JSONArray();
            for(CurrencyChange change : currencyChanges) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("name", change.getDate());
                JSONArray js = new JSONArray();
                js.add(change.getDate());
                js.add(change.getPrice());
                jsonObject.put("value", js);
                jsonArray.add(jsonObject);
            }
            if(currencyChanges.size()==1) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("name", new Date());
                JSONArray js = new JSONArray();
                js.add(jsonObject.get("name"));
                js.add(currencyChanges.get(currencyChanges.size()-1).getPrice());
                jsonObject.put("value", js);
                jsonArray.add(jsonObject);
            }
            return jsonArray;
        }
        return  new JSONArray();
    }

    @Transactional
    public void recordCurrencyPrice(BigDecimal price) {
        CurrencyChange change = new CurrencyChange();
        change.setDate(new Date());
        change.setPrice(price);
        currencyChangeMapper.insertSelective(change);
        setCurrency(price);//设置vc
    }




}

/*
package com.zym.Design1.service;

import com.zym.Design1.dao.TradeRewardMapper;
import com.zym.Design1.entity.TradeReward;
import com.zym.Design1.util.ConstantUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

*/
/**
 * Created on 2018/4/8.
 *//*

@Service
public class TradeRewardService {
    @Autowired
    private TradeRewardMapper tradeRewardMapper;

    public void addTradeReward(Integer userId, Integer entityId, String entityUid, BigDecimal amount, Long tradeId) {
        TradeReward tradeReward = new TradeReward();
        tradeReward.setUserId(userId);
        tradeReward.setEntityId(entityId);
        tradeReward.setEntityUid(entityUid);
        tradeReward.setTradeId(tradeId);
        tradeReward.setDate(new Date());
        tradeReward.setAmount(amount);
        tradeRewardMapper.insertSelective(tradeReward);
    }



}
*/

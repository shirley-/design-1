package com.zym.Design1.service;

import com.alibaba.fastjson.JSON;
import com.zym.Design1.bean.UserLevelRule;
import com.zym.Design1.dao.UserBonusMapper;
import com.zym.Design1.entity.*;
import com.zym.Design1.util.ConstantUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created on 2018/4/15.
 */
@Service
@Slf4j
public class UserBonusService {

    @Autowired
    private UserBonusMapper userBonusMapper;

    @Autowired
    private TradeService tradeService;


    @Transactional
    public void updateEcoinOpt(User user) {

        UserBonusExample example = new UserBonusExample();
        example.createCriteria().andUserIdEqualTo(user.getId()).andTypeEqualTo(ConstantUtil.TYPE_EARLY_PHASE);
        List<UserBonus> userBonuses = userBonusMapper.selectByExample(example);
        if(userBonuses != null && userBonuses.size()==1) {
            UserBonus userBonus = userBonuses.get(0);
            userBonus.setRuleId(user.getRuleId());
            userBonus.setStartDate(new Date());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(userBonus.getStartDate());
            Map<Integer, Rule> levelCreditMap = UserLevelRule.getRuleMap();
            Integer days = levelCreditMap.get(user.getRuleId()).getDays();//规定释放天数
            calendar.add(Calendar.DAY_OF_YEAR, days);
            userBonus.setEndDate(calendar.getTime());//释放end_date
            Trade trade = tradeService.getTradeOfEarlyPhase(user.getId());
            //每天的奖励积分
            userBonus.setDailyBonus(levelCreditMap.get(user.getRuleId()).getBonus().multiply(trade.getEcoin()));//0.002*350=0.7
            int num = userBonusMapper.updateByPrimaryKey(userBonus);
            if(num!=1) {
                log.error("updateEcoinOpt错误，num:{}, userBonus:{}", num , JSON.toJSONString(userBonus));
                throw new RuntimeException(" 修改选择错误");
            }
            log.info("userBonus:{}, trade:{}", JSON.toJSONString(userBonus), JSON.toJSONString(trade));
        }else {
            log.error("userBonuses:{}", userBonuses==null?"null":JSON.toJSONString(userBonuses));
            throw new RuntimeException("修改出错");
        }
    }

    //新增每天释放规则
    @Transactional
    public UserBonus addReleaseRule(User user, BigDecimal ecoin, Trade trade, String phase) {
        Map<Integer, Rule> levelCreditMap = UserLevelRule.getRuleMap();
        //user_total_credit
        //加入用户-积分关系，为了每天释放积分时 上级的释放计算
        UserBonus userBonus = new UserBonus();
        userBonus.setState(ConstantUtil.STATE_VALID);
        userBonus.setUserId(user.getId());

        userBonus.setStartDate(new Date());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(userBonus.getStartDate());
        Integer days ;//规定释放天数
        if(phase.equals(ConstantUtil.TYPE_EARLY_PHASE)) {
            userBonus.setType(ConstantUtil.TYPE_EARLY_PHASE);//前期
            userBonus.setRuleId(user.getRuleId());//ruleId
            days = levelCreditMap.get(user.getRuleId()).getDays();//规定释放天数
            userBonus.setFrozenCoin(user.getReleaseFrozenCoin());//规定释放总积分
            //每天的奖励积分
            userBonus.setDailyBonus(levelCreditMap.get(user.getRuleId()).getBonus().multiply(ecoin));//0.002*ecoin
        }else {//LATE_PHASE 后期
            userBonus.setType(ConstantUtil.TYPE_LATE_PHASE);//后期
            userBonus.setRuleId(trade.getRuleId());//ruleId
            days = levelCreditMap.get(trade.getRuleId()).getDays();//规定释放天数
            userBonus.setFrozenCoin(trade.getAmount());//规定释放总货币
            //每天的奖励积分
            userBonus.setDailyBonus(levelCreditMap.get(trade.getRuleId()).getBonus().multiply(ecoin));//0.002*ecoin
        }
        calendar.add(Calendar.DAY_OF_YEAR, days);
        userBonus.setTradeId(trade.getId());
        userBonus.setEndDate(calendar.getTime());//释放end_date

        userBonusMapper.insertSelective(userBonus);//插入用户-积分信息
        log.info("插入用户-释放奖励信息, userBonus:{}", JSON.toJSONString(userBonus));
        return userBonus;
    }

    public List<UserBonus> selectByExample(UserBonusExample example) {
        return userBonusMapper.selectByExample(example);
    }

    public List<UserBonus> getEarlyPhaseBonus(Integer userId) {
        UserBonusExample userBonusExample = new UserBonusExample();
        userBonusExample.createCriteria().andUserIdEqualTo(userId).andStateEqualTo(ConstantUtil.STATE_VALID)//有效的
                .andTypeEqualTo(ConstantUtil.TYPE_EARLY_PHASE);//  type前期的
        return userBonusMapper.selectByExample(userBonusExample);
    }

    public List<UserBonus> getUserBonus(Integer userId) {
        UserBonusExample userBonusExample = new UserBonusExample();
        userBonusExample.createCriteria().andUserIdEqualTo(userId).andStateEqualTo(ConstantUtil.STATE_VALID);//有效的
        return userBonusMapper.selectByExample(userBonusExample);
    }



}

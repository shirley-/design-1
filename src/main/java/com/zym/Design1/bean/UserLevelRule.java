package com.zym.Design1.bean;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zym.Design1.dao.OrgRuleMapper;
import com.zym.Design1.dao.RuleMapper;
import com.zym.Design1.entity.OrgRule;
import com.zym.Design1.entity.OrgRuleExample;
import com.zym.Design1.entity.Rule;
import com.zym.Design1.entity.RuleExample;
import com.zym.Design1.util.ConstantUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created on 2018/3/18.
 */
@Slf4j
@Component
public class UserLevelRule implements InitializingBean, ApplicationContextAware {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private RuleMapper ruleMapper;

    @Autowired
    private OrgRuleMapper orgRuleMapper;

    private static Map<Integer, Rule> userCreditMap;
    private static Map<Integer, Integer> levelNumMap;
    private static List<Rule> ruleListEarlyPhase;
    private static List<Rule> ruleListLatePhase;

    @Override
    public void afterPropertiesSet() throws Exception {
        List<Rule> userLevels = ruleMapper.selectByExample(new RuleExample());
        userCreditMap = userLevels.stream().collect(Collectors.toMap(Rule::getId, rule->rule ));
        log.info("userCreditMap size: {}", userCreditMap.size());
        List<OrgRule> orgRules = orgRuleMapper.selectByExample(new OrgRuleExample());
        levelNumMap = orgRules.stream().collect(Collectors.toMap(OrgRule::getLevel, OrgRule::getNum));
        log.info("levelNumMap size: {}", levelNumMap.size());
        ruleListEarlyPhase = setEarlyRuleList();
        ruleListLatePhase = setLateRuleList();
        log.info("ruleListEarlyPhase size: {}", ruleListEarlyPhase.size());
        log.info("ruleListLatePhase size: {}", ruleListLatePhase.size());
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public static Map<Integer, Rule> getRuleMap() {
        return userCreditMap;
    }

    public static Map<Integer, Integer> getOrgRule() {return levelNumMap;}

    public static List<Rule> getRuleList() {return ruleListEarlyPhase;}

    public static List<Rule> getLatePhaseRuleList() {return ruleListLatePhase;}

    private List<Rule> setEarlyRuleList() {
        Map<Integer, Rule> ruleMap = getRuleMap();
        List<Rule> ruleListEarlyPhase = new ArrayList<>();
        for(Map.Entry<Integer, Rule> entry : ruleMap.entrySet()) {
            Rule rule = entry.getValue();
            if(rule.getType().equals(ConstantUtil.TYPE_EARLY_PHASE)) {
                rule.setRemark("套餐:" + rule.getRuleName()+", 锁仓天数:"+rule.getDays());
                ruleListEarlyPhase.add(rule);
            }
        }
        ruleListEarlyPhase.sort((o1, o2)->o1.getRuleName().compareTo(o2.getRuleName()));
        return ruleListEarlyPhase;
    }

    private List<Rule> setLateRuleList() {
        Map<Integer, Rule> ruleMap = getRuleMap();
        List<Rule> ruleListEarlyPhase = new ArrayList<>();
        for(Map.Entry<Integer, Rule> entry : ruleMap.entrySet()) {
            Rule rule = entry.getValue();
            if(rule.getType().equals(ConstantUtil.TYPE_LATE_PHASE)) {
                rule.setRemark("锁仓天数:"+rule.getDays());
                ruleListEarlyPhase.add(rule);
            }
        }
        ruleListEarlyPhase.sort((o1, o2)->o1.getRuleName().compareTo(o2.getRuleName()));
        return ruleListEarlyPhase;
    }

}

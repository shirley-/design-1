package com.zym.Design1.service;

import com.zym.Design1.bean.UserLevelRule;
import com.zym.Design1.entity.Rule;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created on 2018/4/10.
 */
@Service
public class RuleService {

    public List<Rule> getRuleList() {
        return  UserLevelRule.getRuleList();
    }

    public List<Rule> getLatePhaseRuleList() {
        return  UserLevelRule.getLatePhaseRuleList();
    }
}

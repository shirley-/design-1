package com.zym.Design1.bean.vo;

import com.zym.Design1.entity.DailyReleaseLog;
import com.zym.Design1.entity.Rule;
import com.zym.Design1.entity.User;

import java.io.Serializable;

/**
 * Created on 2018/4/4.
 */
public class FinVO extends DailyReleaseLog implements Serializable{
    private static final long serialVersionUID = 1L;
    private Rule rule;
    private User user;
    private String source;


    public Rule getRule() {
        return rule;
    }

    public void setRule(Rule rule) {
        this.rule = rule;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}

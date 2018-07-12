package com.zym.Design1.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zym.Design1.bean.UserLevelRule;
import com.zym.Design1.bean.ViewObject;
import com.zym.Design1.dao.OrgMapper;
import com.zym.Design1.entity.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Created on 2018/3/12.
 */
@Slf4j
@Service
@CacheConfig(cacheNames = "OrgService")
public class OrgService {
    @Autowired
    private OrgMapper orgMapper;

    @Autowired
    private UserService userService;


    private static ThreadLocal<Integer> childrenCount = new ThreadLocal<>();

    /*public void getOrgOfId(Integer id) {
        ViewObject vo = new ViewObject();
        getDescendantsOfId(id, vo);
        System.out.println(vo);
        System.out.println(JSONObject.toJSON(vo));
    }*/

    /**
     * bootstrap treeview
     * 封装最外面一层json，（附带  计数childrenCount）
     * @param id
     * @return
     */
    @Cacheable(value = "getOrgOfIdForTreeviewJson", key = "'userId_'+#id")
    public String getOrgOfIdForTreeviewJson(Integer id) {
        JSONArray jsonArray = new JSONArray();
        JSONObject vo = new JSONObject();
        childrenCount.set(0);
        ((OrgService)AopContext.currentProxy()).getDescendantsOfIdForTreeview(id, vo);
        vo.put("childrenCount", childrenCount.get()-1);
        log.info("children count:{}", childrenCount.get()-1);
        jsonArray.add(vo);
        log.info(jsonArray.toString());
        return jsonArray.toString();
    }

    @Cacheable(value = "getOrgOfIdForTreeChartJson", key =  "'userId_'+#id")
    public String getOrgOfIdForTreeChartJson(Integer id) {
        JSONObject vo = new JSONObject();
        getDescendantsOfIdForTreeChart(id, vo);
        log.info(vo.toString());
        return vo.toString();
    }

  /*  public List<Org>  getChildrenAndParentsOfId(Integer id) {
        OrgExample example = new OrgExample();
        example.createCriteria().andUserIdEqualTo(id);
        return orgMapper.selectByExample(example);
    }*/

    public Org getParentOfId(Integer id) {
        OrgExample example = new OrgExample();
        example.createCriteria().andChildIdEqualTo(id);
        List<Org> organizations = orgMapper.selectByExample(example);
        if(organizations !=null && organizations.size() ==1) {
            return organizations.get(0);
        }
        return null;
    }

    @Cacheable(value = "getChildrenOfId", key =  "'userId_'+#id")
    public List<Org> getChildrenOfId(Integer id) {
        OrgExample example = new OrgExample();
        example.createCriteria().andUserIdEqualTo(id);
        return orgMapper.selectByExample(example);
    }
/*
    public void getDescendantsOfId(Integer id, ViewObject vo) {
        User user = userService.selectByPrimaryKey(id);
        String userInfo =getDisplayUserInfo(user);
        vo.set("text", userInfo);
        List<Org> childrenOrgList = getChildrenOfId(id);
        if(childrenOrgList != null) {
            List<ViewObject> voList = new ArrayList<>(childrenOrgList.size());
            for( Org child : childrenOrgList) {
                ViewObject voChild = new ViewObject();
                getDescendantsOfId(child.getRelationId(), voChild);
                voList.add(voChild);
            }
            vo.set("children", voList);
        }
    }*/

    /**
     * scheduleTasks定时任务用，计数用
     * 统计id用户的全部孩子有多少个，都是谁
     * @param id
     * @param idLevelMap
     */
//    @Cacheable(value = "descendantsMap", key = "#id")
    public void getDescendantsMap(Integer id, Map<Integer, User> idLevelMap) {
        User user = userService.getUserById(id);
        idLevelMap.put(user.getId(), user);
        List<Org> childrenOrgList = getChildrenOfId(id);
        if(childrenOrgList != null) {
            List<ViewObject> voList = new ArrayList<>(childrenOrgList.size());
            for( Org child : childrenOrgList) {
                ViewObject voChild = new ViewObject();
                getDescendantsMap(child.getChildId(), idLevelMap);
                voList.add(voChild);
            }
        }
    }

    /**
     * bootstrap treeview图
     * @param id
     * @param vo
     */
//    @Cacheable(value = "descendantsOfIdForTreeview", key = "#id")
    public void getDescendantsOfIdForTreeview(Integer id, JSONObject vo) {
        childrenCount.set(childrenCount.get()+1);
        User user = userService.getUserById(id);
        String userInfo =getDisplayUserInfo(user);
        vo.put("text", userInfo);
        vo.put("icon", "glyphicon glyphicon-user");
        vo.put("selectable", false);
        vo.put("showBorder", false);//设置值未生效
        vo.put("state.expanded", true);
        JSONObject jo = new JSONObject();
        jo.put("expanded", true);
        vo.put("state", jo);
        vo.put("borderColor", "#FFFFFF");
        vo.put("back", "#FFFFFF");
        vo.put("collapseIcon", "glyphicon glyphicon-chevron-right");
        vo.put("expandIcon", "glyphicon glyphicon-chevron-down");
        vo.put("color", "#48b");
        List<Org> childrenOrgList = getChildrenOfId(id);
        if(childrenOrgList != null) {
            JSONArray jsonArray = new JSONArray();
            for( Org child : childrenOrgList) {
                JSONObject voChild = new JSONObject();
                getDescendantsOfIdForTreeview(child.getChildId(), voChild);
                jsonArray.add(voChild);
            }
            if(jsonArray.size() >0 ){
                vo.put("nodes", jsonArray);
            }
        }
    }

    /**
     * Echart图
     * @param id
     * @param vo
     */
//    @Cacheable(value = "descendantsOfIdForTreeChart", key = "#id")
    public void getDescendantsOfIdForTreeChart(Integer id, JSONObject vo) {
        User user = userService.getUserById(id);
//        vo.put("type", "tree");
        vo.put("value", getDisplayUserInfo(user).replace("|", "\n"));//c
        vo.put("name", getDisplayUserInfo(user).replace("|", "\n"));//b
//        JSONObject jo = new JSONObject();
        vo.put("symbol", "rect");
        vo.put("symbolSize", Arrays.asList(80,40));

        JSONObject jo = new JSONObject();
        JSONObject jo1 = new JSONObject();
        jo1.put("color", "#48b");
        jo1.put("borderColor", "#48b");
        jo1.put("borderWidth", 0);
        jo.put("normal", jo1);

//        jo1 = new JSONObject();
//        jo1.put("color", "#ff0000");
//        jo1.put("borderColor", "#ff0000");
//        jo.put("emphasis", jo1);
//        vo.put("itemStyle", jo);

        jo = new JSONObject();
        jo1 = new JSONObject();
        jo1.put("show", true);
//        jo1.put("position", Arrays.asList(10, 10));
//        jo.put("distance", 5);
//        jo1.put("formatter", "{b}:{c}");
        jo1.put("fontSize", 12);
//        jo1.put("padding", Arrays.asList(3));
        jo.put("normal", jo1);
        vo.put("label", jo);




       /* jo = new JSONObject();
        jo.put("position", Arrays.asList("50", "50"));
        jo.put("formatter", "{b}:{c}");
        jo1 = new JSONObject();
        jo1.put("color", "#00ff00");
        jo.put("textStyle", jo1);
        vo.put("tooltip", jo);*/

//        jo = new JSONObject();
//        jo.put("name", user.getId());/
//        jo.put("value", getDisplayUserInfo(user));


        List<Org> childrenOrgList = getChildrenOfId(id);
        if(childrenOrgList != null) {
            JSONArray jsonArray = new JSONArray();
            for( Org child : childrenOrgList) {
                JSONObject voChild = new JSONObject();
                getDescendantsOfIdForTreeChart(child.getChildId(), voChild);
                jsonArray.add(voChild);
            }
            if(jsonArray.size() >0 ){
//                jo.put("children", jsonArray);
                vo.put("children", jsonArray);

            }
//            vo.put("data", jo);
        }
    }
//http://echarts.baidu.com/option3.html#series-tree.orient.symbol

    private String getDisplayUserInfo(User user) {
        return user.getUid() + " | " +user.getName();
    }

    //纯上级
    public void getAncestorsMap(Integer id, Map<Integer, User> ancestorMap) {
        OrgExample orgExample = new OrgExample();
        orgExample.createCriteria().andChildIdEqualTo(id);
        List<Org> orgs = orgMapper.selectByExample(orgExample);
        if(orgs!= null && orgs.size()==1) {
            Integer parent = orgs.get(0).getUserId();
            ancestorMap.put(parent, userService.getUserById(parent));
            getAncestorsMap(parent, ancestorMap);
        }
    }

    /**
     * 加入人脉关系
     * @param oldMember 介绍人
     * @param newMember 新注册会员
     */
    @Transactional
    public void addMemberRecommend(User oldMember, User newMember)  {
        checkOrgRule(oldMember, newMember);
        Org org = new Org();
        org.setUserId(oldMember.getId());
        org.setChildId(newMember.getId());
        log.info("org:{}", org);
        orgMapper.insertSelective(org);
    }

    /**
     * 检查是否满足人脉限制
     * @param oldMember
     * @param newMember
     */
    public void checkOrgRule(User oldMember, User newMember) {
        Map<Integer, Integer> orgRule = UserLevelRule.getOrgRule();
        Integer maxNum = orgRule.get(oldMember.getLevel()+1);
        if(maxNum==0) {
            log.error("当前推荐人无法推荐新会员, maxNum, oldMember:{}", maxNum, JSON.toJSONString(oldMember));
            throw new RuntimeException("当前推荐人无法推荐新会员");
        }
        OrgExample example = new OrgExample();
        example.createCriteria().andUserIdEqualTo(oldMember.getId());

        Long num = orgMapper.countByExample(example);
        if( num.compareTo(new Long(maxNum))>=0) {
            log.error("当前推荐人的推荐名额已满, num:{}, oldMember:{}", num, JSON.toJSONString(oldMember));
            throw new RuntimeException("当前推荐人的推荐名额已满");
        }
    }

    @Transactional
    public void deleteOrg(Integer userId) {
        OrgExample orgExample = new OrgExample();
        orgExample.or().andUserIdEqualTo(userId);
        orgExample.or().andChildIdEqualTo(userId);
        log.info("userId:{}", userId);
        orgMapper.deleteByExample(orgExample);
    }



}

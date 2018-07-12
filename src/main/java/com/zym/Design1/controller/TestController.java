/*
package com.zym.Design1.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

*/
/**
 * Created on 2018/3/2.
 *//*

@Slf4j
public class TestController {

    @RequestMapping("/test")
    public String testIndex() {
        log.info("testIndex...");
        return "index";
    }

    @RequestMapping("/starter")
    public String testStarter() {
        return "starter";
    }

    @RequestMapping("/login1")
    public String testLogin() {
        return "login";
    }

    @RequestMapping("/signUp1")
    public String testSignUp() {
        return "signUp";
    }

    @RequestMapping("/data")
    public String testData() {
        return "data";
    }

    @RequestMapping("/test1")
    public String testTest1() {
        return "test1";
    }

    @RequestMapping("/test2")
    public String testTest2() {
        return "test2";
    }

    @RequestMapping("/test3")
    public String testTest3() {
        return "test3";
    }


    @RequestMapping("/testImg")
    public String testImg() {
        return "testImg";
    }



    */
/**
     * {
     "total":20,
     "rows":[
             {
                 "id":1,
                 "name":"张三",
                 "age":22
             },
            ...
        ]
     }
     * @return
     *//*

    @ResponseBody
    @RequestMapping("/list1")
    public JSONObject testList() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("total", 20);
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject1 = new JSONObject();
        jsonObject1.put("name","ym");
        jsonObject1.put("stargazers_count","1");
        jsonObject1.put("forks_count","2");
        jsonObject1.put("description","aaaaaa");
        jsonArray.add(jsonObject1);
        JSONObject jsonObject2 = new JSONObject();
        jsonObject2.put("name","www");
        jsonObject2.put("stargazers_count","5");
        jsonObject2.put("forks_count","3");
        jsonObject2.put("description","xxxx");
        jsonArray.add(jsonObject2);
        jsonObject.put("rows", jsonArray);
        log.info(jsonObject.toJSONString());
        return jsonObject;
    }

    @ResponseBody
    @RequestMapping("/list")
    public JSONArray testList2() {
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject1 = new JSONObject();
        jsonObject1.put("name","ym");
        jsonObject1.put("stargazers_count","1");
        jsonObject1.put("forks_count","2");
        jsonObject1.put("description","aaaaaa");
        jsonArray.add(jsonObject1);
        JSONObject jsonObject2 = new JSONObject();
        jsonObject2.put("name","www");
        jsonObject2.put("stargazers_count","5");
        jsonObject2.put("forks_count","3");
        jsonObject2.put("description","xxxx");
        jsonArray.add(jsonObject2);
        log.info(jsonArray.toJSONString());
        return jsonArray;
    }

}
*/

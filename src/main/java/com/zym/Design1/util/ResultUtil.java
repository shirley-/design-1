package com.zym.Design1.util;

import com.alibaba.fastjson.JSONObject;

import java.util.Map;

/**
 * Created by YM on 2018/3/6.
 */
public class ResultUtil {

    public static int ERROR = 1;
    public static int SUCCESS = 0;
    public static int NOT_LOGIN = 999;

    public static String ERROR_JSON = "J";
    public static String ERROR_VIEW = "V";

    public static String getJSONString(int code) {
        JSONObject json = new JSONObject();
        json.put("code", code);
        return json.toJSONString();
    }

    public static String getJSONString(int code, String msg) {
        JSONObject json = new JSONObject();
        json.put("code", code);
        json.put("msg", msg);
        return json.toJSONString();
    }

    public static String getJSONString(int code, Map<String, Object> map) {
        JSONObject json = new JSONObject();
        json.put("code", code);
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            json.put(entry.getKey(), entry.getValue());
        }
        return json.toJSONString();
    }

    public static String getValidString(boolean valid) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("valid", valid);
        return jsonObject.toJSONString();
    }

}

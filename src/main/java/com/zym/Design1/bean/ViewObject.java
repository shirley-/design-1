package com.zym.Design1.bean;

import java.util.HashMap;

import java.util.HashMap;
import java.util.Map;

/**
 * Created on 2018/03/13.
 */

//model 与 view 的中间层
public class ViewObject {

    private Map<String, Object> objs = new HashMap<>();

    public Object get(String key) {
        return objs.get(key);
    }

    public void set(String key, Object value) {
        this.objs.put(key, value);
    }
}

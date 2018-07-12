package com.zym.Design1.Test2;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

//@RunWith(SpringRunner.class)
//@SpringBootTest
public class Test {

    @org.junit.Test
    public void test1() {
        String example = "xxppsolxkxpsoxppxo";
        String[] str = example.split("");
        Map<String, Integer> map = new HashMap<>();
        for(int i=0;i<str.length;i++) {
            if(map.containsKey(str[i])) {
                map.put(str[i], map.get(str[i])+1);
            }else {
                map.put(str[i], 1);
            }
        }
        TreeMap<String, Integer> treeMap = new TreeMap<>((o1, o2)-> {
                return map.get(o2).compareTo(map.get(o1));
        });
        treeMap.putAll(map);
        System.out.println(treeMap.higherKey(treeMap.firstKey()));
        System.out.println(treeMap);
        System.out.println(map);

        Iterator iterator = treeMap.keySet().iterator();
        while (iterator.hasNext()) {
            String key = (String) iterator.next();
            System.out.println(key);
            // 获取Value
            // treeMap.get(key);
        }
    }



    @org.junit.Test
    public void test2() {
        Resource res = new FileSystemResource("E:\\QQfile\\Server.java");
        if(res.exists()) {
            try {
                BufferedReader bufferedReader = new BufferedReader(new FileReader(res.getFile()));
                System.out.println(bufferedReader.lines().count());
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    @org.junit.Test
    public void test3() {
        int a = 1;
        Integer b = a;
        System.out.println(b.getClass().toString());
        System.out.println(b.getClass().isPrimitive());
    }

}


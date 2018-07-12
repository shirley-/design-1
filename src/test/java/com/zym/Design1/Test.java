package com.zym.Design1;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

//@RunWith(SpringRunner.class)
//@SpringBootTest
public class Test {

    @org.junit.Test
    public void test1() throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = simpleDateFormat.parse("2018-03-29 18:33:00");
//        Date now = new Date();
        //9-12  14-17
        if (now.getHours() < 9 || (now.getHours() > 11 && now.getHours() < 14) || (now.getHours() > 16)
                || (now.getHours() == 11 && now.getMinutes() > 30)) {
            throw new RuntimeException("不在交易时间");
        }
    }

    @org.junit.Test
    public void test2() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();
        System.out.println(simpleDateFormat.format(now));
    }


    @org.junit.Test
    public void test32() throws NoSuchMethodException, NoSuchFieldException, InvocationTargetException, IllegalAccessException {
        Map<String, String> map = new HashMap<String, String>();
        map.put("hollis", "hollischuang");

        Class<?> mapType = map.getClass();
        Method capacity = mapType.getDeclaredMethod("capacity");
        capacity.setAccessible(true);
        System.out.println("capacity : " + capacity.invoke(map));

        Field size = mapType.getDeclaredField("size");
        size.setAccessible(true);
        System.out.println("size : " + size.get(map));
    }

    @org.junit.Test
    public void testHashMap() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        Map<MapKey, String> map = new HashMap<MapKey, String>();

        //第一阶段
        /*for (int i = 0; i < 6; i++) {
            map.put(new MapKey(String.valueOf(i)), "A");
        }*/


        Class<?> mapType = map.getClass();
        Method capacity = mapType.getDeclaredMethod("capacity");
        capacity.setAccessible(true);
        System.out.println("capacity : " + capacity.invoke(map));
//        Class<?> mapType2 = map.getClass();
//        Method capacity2 = mapType2.getDeclaredMethod("threshold");
//        capacity2.setAccessible(true);
//        System.out.println("threshold : " + capacity2.invoke(map));

        //第二阶段
        for (int i = 0; i < 10; i++) {
            System.out.println("i:" + i);
            map.put(new MapKey(String.valueOf(i)), "A");
            System.out.println(map.size());
            System.out.println("capacity : " + capacity.invoke(map));
//            System.out.println("threshold : " + capacity2.invoke(map));
        }



        //第三阶段
        for (int i = 0; i < 50; i++) {
            map.put(new MapKey(String.valueOf(i)), "A");
        }



        //第四阶段
        map.put(new MapKey("X"), "B");
        map.put(new MapKey("Y"), "B");
        map.put(new MapKey("Z"), "B");

        System.out.println(map);
    }

    @org.junit.Test
    public void TestThread()  {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(5, 10, 200, TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<Runnable>(5));

        for (int i = 0; i < 15; i++) {
            MyTask myTask = new MyTask(i);
            executor.execute(myTask);
            System.out.println("线程池中线程数目：" + executor.getPoolSize() + "，队列中等待执行的任务数目：" +
                    executor.getQueue().size() + "，已执行玩别的任务数目：" + executor.getCompletedTaskCount());
        }
        executor.shutdown();

    }



}
class MyTask implements Runnable {
    private int taskNum;

    public MyTask(int num) {
        this.taskNum = num;
    }

    @Override
    public void run() {
        System.out.println("正在执行task "+taskNum);
        try {
            Thread.currentThread().sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("task "+taskNum+"执行完毕");
    }
}

class MapKey {

    private static final String REG = "[0-9]+";

    private String key;

    public MapKey(String key) {
        this.key = key;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MapKey mapKey = (MapKey) o;

        return !(key != null ? !key.equals(mapKey.key) : mapKey.key != null);

    }

    @Override
    public int hashCode() {
        if (key == null)
            return 0;
        Pattern pattern = Pattern.compile(REG);
        if (pattern.matcher(key).matches())
            return 1;
        else
            return 2;
    }

    @Override
    public String toString() {
        return key;
    }
}


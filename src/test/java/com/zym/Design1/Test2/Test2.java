package com.zym.Design1.Test2;

import com.zym.Design1.entity.User;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

//@RunWith(SpringRunner.class)
//@SpringBootTest
public class Test2 {

    @org.junit.Test
    public void test1() {
        List<Integer> list = new ArrayList<Integer>();
        list.add(0);
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        list.add(5);
        list.add(6);
        list.add(7);
        //正常循环
        for (int i = 0; i < list.size(); i++) {
            System.out.println("i的值:" + i + " 对应的数字:" + list.get(i));
        }
        System.out.println("没有remove前list的项:"+list.size());

        //边循环边删除
        for (int i = list.size() -1 ; i >= 0; i--) {
            System.out.println("i的值  " + i + " 对应的数字 " + list.get(i));
            if(list.get(i) == 3) {
                list.remove(list.get(i));
                i++;
            }
        }
        System.out.println("remove后list的项:"+list.size());

        System.out.println("==========remove后的list==========");
        for (int i = 0; i < list.size(); i++) {
            System.out.println("i的值  " + i + " 对应的数字 " + list.get(i));
        }
    }



    @org.junit.Test
    public void test2() {
        ArrayList<String> listA= new ArrayList<String>();
        listA.add("Tom");
        ArrayList<String> listB= new ArrayList<String>();
        listB.add("Tom1");
        listA.retainAll(listB);
        if(listA.size()>0){
            System.out.println("这两个集合有相同的交集");
        }else{
            System.out.println("这两个集合没有相同的交集");
        }

    }


    @org.junit.Test
    public void test3() {
        List<Integer> list = new ArrayList<Integer>();
        list.add(0);
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        list.add(5);
        list.add(6);
        list.add(7);
        Integer[] a = new Integer[14];

        for(Integer i:a) {
            i=100;
        }
        Arrays.stream(a).forEach(System.out::println);
        System.out.println();
        Arrays.stream(list.toArray(a)).forEach(System.out::println);
    }

    @org.junit.Test
    public void test4() {
        int[] a = new int[]{1,2,3,4,5};
        BigInteger bigInteger = new BigInteger("12345",8);
        System.out.println(bigInteger.bitCount());
        System.out.println(bigInteger.bitLength());
        System.out.println(bigInteger);
    }

    @org.junit.Test
    public void test5() {
        ExecutorService exec = Executors.newFixedThreadPool(10);
        ArrayList<Future<String>> results = new ArrayList<Future<String>>();

        for (int i = 0; i < 10; i++) {
            String email = "asd" + i + "@qq.com";
//            exec.submit()
            results.add(exec.submit(new TaskWithResult(email)));
        }

        boolean isDone = false;
        while (!isDone) {
            isDone = true;
            for (Future<String> future : results) {
                if (!future.isDone()) {
                    isDone = false;
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                    }
                    break;
                }
            }
        }
        exec.shutdown();
    }
}


class TaskWithResult implements Callable<String> {

    private String email;

    public TaskWithResult(String email) {
        this.email = email;
    }

    @Override
    public String call() {
        System.out.println(email);
        return null;
    }

}

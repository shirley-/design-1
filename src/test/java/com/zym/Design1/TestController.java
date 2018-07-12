package com.zym.Design1;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//@RestController
@Slf4j
public class TestController {
    @Autowired
    QuartzTest q;
    //添加一个倒计时任务
    @RequestMapping("/add")
    public Object add(String name) {
        log.info("TestController add...");
        return q.addJob(name, MyJob.class, "我是你的人", 20, "001");
    }
    //删除一个倒计时任务
    @RequestMapping("/remove")
    public Object remove(String name) {
        log.info("TestController remove...");
        return q.closeJob(name, "001");
    }
    //从数据库加载还未执行的任务（spring容器初始化的时候会自动加载）
    @RequestMapping("/resume")
    public Object resume(String name) {
        log.info("TestController resume...");
        q.resumeJob();
        return "OK";
    }
}

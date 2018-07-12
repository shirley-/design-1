package com.zym.Design1.schedule;

import com.zym.Design1.entity.User;
import com.zym.Design1.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created on 2018/3/18.
 */
@Slf4j
@Component
public class ScheduledTasks {

    @Autowired
    private UserService userService;


    @Scheduled(cron = "30 00 1 1/1 * *")
    public void doDailyTask() {
        long start = System.currentTimeMillis();
        log.info("quartz task starting...");
        List<User> users = userService.getAllMembers();
        for(User user : users) {
            /*if(user.getId()!=13) {
                continue;
            }*/
            try{
                userService.quartzTask(user);
                log.info("complete userId:{}",user.getId());
            }catch (Exception e) {
                log.error("user:{}, error:{}",user.getId(), e.getMessage());
                e.printStackTrace();
            }
        }
        log.info("quartz task end... duration : {} s", (System.currentTimeMillis()-start)/1000);
    }
}

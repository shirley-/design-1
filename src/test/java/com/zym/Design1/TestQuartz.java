package com.zym.Design1;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

import static org.quartz.SimpleScheduleBuilder.simpleSchedule;

/**
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class TestQuartz {

    @Test
    public  void test() {
        SchedulerFactory sf = new StdSchedulerFactory();
        try {
            Scheduler sched = sf.getScheduler();
            Date nowTime = new Date();
            System.out.println(nowTime);
            //将分秒数进位取整nowTime=15:40:28 runTime=15:41:00
            Date runTime = DateBuilder.evenMinuteDate(nowTime);
            log.info(runTime.toString());
            System.out.println(runTime);
            JobDetail job = JobBuilder.newJob(HelloJob.class).withIdentity("job1", "group1").build();
            //单次定时任务
            //Trigger trigger = TriggerBuilder.newTrigger().withIdentity("trigger1", "group1").startAt(runTime).build();
            //多次循环定时任务"0/20 * * * * ?"从*年*月*日  *：*：00秒开始每20秒执行一次
            CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity("trigger1", "group1")
                    .withSchedule(CronScheduleBuilder.cronSchedule("0/20 * * * * ?"))
                    .build();
            Date ft = sched.scheduleJob(job, trigger);

            System.out.println(job.getKey() + " has been scheduled to run at: " + ft + " and repeat based on expression: "
                    + trigger.getCronExpression());
            log.info(job.getKey() + " has been scheduled to run at: " + ft + " and repeat based on expression: "
                    + trigger.getCronExpression());
            sched.start();
            Thread.sleep(60000*5);
            sched.shutdown(true);
        } catch (Exception e) {
            //
        }
    }
}

//继承Job类的具体定时任务
@Slf4j
 class HelloJob implements Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        System.out.println("hello job......start:"+new Date());
        log.info("hello job......start:"+new Date());
    }
}

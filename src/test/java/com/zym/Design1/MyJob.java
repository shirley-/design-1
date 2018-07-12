package com.zym.Design1;

import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by YM on 2018/3/18.
 */
@Slf4j
public class MyJob implements Job {
    @Override
    public void execute(JobExecutionContext context)
            throws JobExecutionException {
        System.out.println("Hello quzrtz  "+
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ").format(new Date()));
        log.info("Hello quzrtz  "+
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ").format(new Date()));

    }

}

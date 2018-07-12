package com.zym.Design1;

import com.zym.Design1.entity.User;
import com.zym.Design1.service.TradeService;
import com.zym.Design1.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Date;
import java.util.concurrent.CountDownLatch;

/**
 * Created by YM on 2018/4/23.
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class TestSync {

    @Autowired
    private TradeService tradeService;

    @Autowired
    private UserService userService;

    @org.junit.Test
    public void test1() {
        final CountDownLatch latch = new CountDownLatch(1);
        for(int i=0; i<5; i++) {
            final int y=i+1;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        latch.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    User user = userService.getUserByUid("xx0" + (y));
                    log.info("thread: {} , user : {}",  Thread.currentThread().getId(), user.getUid());
                    tradeService.wantToBuy(user, BigDecimal.valueOf(1.2), BigDecimal.valueOf(90), 4);
                    log.info("finish xx0{}", y);

                }
            }).start();
        }

        latch.countDown();
    }

    @org.junit.Test
    public void test2() {
        final CountDownLatch latch = new CountDownLatch(1);
        for(int i=6; i<10; i++) {
            final int y=i;
            new Thread(()-> {
                    try {
                        latch.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

//                    synchronized (TestSync.class) {
                        User user = userService.getUserById(Integer.valueOf(6));
//                    log.info("thread: {} , user : {}",  Thread.currentThread().getId(), user.getUid());
//                    user.setAddress(new Date().toString());
                        userService.updateUser(user);
//                    log.info("finish {}", user.getUid());
//                    }

                }
            ).start();
        }

        latch.countDown();
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

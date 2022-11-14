package com.fjw.learn;

import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 学习Schedule
 */
public class ScheduleTest {
    public static void main(String[] args) {
        ScheduledExecutorService s = Executors.newScheduledThreadPool(1);
        //延时2秒后开始执行任务，每3秒再执行任务
        s.scheduleAtFixedRate(() -> {
            System.out.println(new Date().getSeconds());
        }, 2, 3, TimeUnit.SECONDS);
    }

    public static void simpleTest() {
        ScheduledExecutorService s = Executors.newScheduledThreadPool(1);
        //延时2秒后执行任务
        s.schedule(() -> System.out.println(Thread.currentThread().getName()), 2, TimeUnit.SECONDS);
        //需要记得关闭
        s.shutdown();
    }


}

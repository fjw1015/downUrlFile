package com.fjw.learn;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * jdk提供创建线程池 不建议使用这些方式，建议使用原生的
 */
public class PoolTestJdk {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(5);//自定义核心线程数量 无界队列
        ExecutorService executorService1 = Executors.newCachedThreadPool();//全为非核心线程 直接提交队列
        ExecutorService executorService2 = Executors.newSingleThreadExecutor();
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(5);

    }
}

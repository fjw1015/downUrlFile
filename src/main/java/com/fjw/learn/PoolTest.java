package com.fjw.learn;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池的创建
 */
public class PoolTest {
    public static void main(String[] args) {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
                2,3,1,TimeUnit.MINUTES,new ArrayBlockingQueue<>(2));
        //创建任务
        Runnable r = () -> System.out.println(Thread.currentThread().getName());
        //将任务提交给线程池
        for (int i = 0; i < 5; i++) {
            threadPoolExecutor.execute(r);
        }
        System.out.println(threadPoolExecutor.toString());
        threadPoolExecutor.shutdown();
    }
}

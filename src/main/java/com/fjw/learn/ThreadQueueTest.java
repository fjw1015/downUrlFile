package com.fjw.learn;

import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 演示直接提交队列
 */
public class ThreadQueueTest {
    public static void main(String[] args) throws Exception{
        ThreadPoolExecutor threadPoolExecutor = null;
        try {
            threadPoolExecutor = new ThreadPoolExecutor(
                    2, 3, 1, TimeUnit.MINUTES, new SynchronousQueue<>());
            //创建任务
            Runnable r = () -> System.out.println(Thread.currentThread().getName());
            //将任务提交给线程池
            for (int i = 0; i < 4; i++) {
                threadPoolExecutor.execute(r);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (threadPoolExecutor != null) {
                threadPoolExecutor.shutdown();  //温和
                //等待1分钟，如果线程池没有关闭，就暴力关闭线程池
                if (!threadPoolExecutor.awaitTermination(1, TimeUnit.MINUTES)) {
                    threadPoolExecutor.shutdownNow();   //暴力，抛弃阻塞队列的任务
                }
            }
        }


    }
}

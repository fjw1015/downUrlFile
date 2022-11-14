package com.fjw.core;

import com.fjw.constant.Constant;
import com.fjw.utils.FileUtils;
import com.fjw.utils.HttpUtils;
import com.fjw.utils.LogUtils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.concurrent.*;

public class Downloader {
    public ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
    public ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(
            Constant.THREAD_NUM,
            Constant.THREAD_NUM,
            0,
            TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(Constant.THREAD_NUM));

    private CountDownLatch countDownLatch = new CountDownLatch(Constant.THREAD_NUM);

    public void download(String url) throws Exception {
        //获取文件名
        String httpFileName = HttpUtils.getHttpFileName(url);
        //文件下载路径
        httpFileName = Constant.PATH + httpFileName;
        //获取本地文件大小
        long localFileLength = FileUtils.getFileContentLength(httpFileName);
        //获取连接对象
        HttpURLConnection httpUrlConnection = null;
        DownloadInfoThread downloadInfoThread = null;
        try {
            httpUrlConnection = HttpUtils.getHttpUrlConnection(url);
            //获取下载文件的总大小
            int contentLength = httpUrlConnection.getContentLength();
            //判断文件是否已下载过
            if (localFileLength >= contentLength) {
                LogUtils.info("{}已下载完毕，无需重新下载", httpFileName);
                return;
            }
            //创建获取下载信息的任务对象
            downloadInfoThread = new DownloadInfoThread(contentLength);
            //将任务交给线程执行，每隔1秒执行一次
            scheduledExecutorService.scheduleAtFixedRate(downloadInfoThread, 100, 100, TimeUnit.MILLISECONDS);
            //切分任务
            ArrayList<Future> list = new ArrayList<>();
            split(url, list);
            countDownLatch.await();
            //合并文件
            if (merge(httpFileName)) {
                //清除临时文件
                clearTemp(httpFileName);
            }
        } catch (Exception e) {
            LogUtils.error("连接异常");
        } finally {
            System.out.println("\r下载结束");
            //关闭连接对象
            if (null != httpUrlConnection) {
                httpUrlConnection.disconnect();
            }
            //关闭
            scheduledExecutorService.shutdownNow();
            poolExecutor.shutdown();
            //如果1分钟未关闭，强制关闭
            if (!poolExecutor.awaitTermination(1, TimeUnit.MINUTES)) {
                poolExecutor.shutdownNow();
            }
        }
    }

    public void split(String url, ArrayList<Future> futureList) {
        try {
            //获取下载文件大小
            long contentLength = HttpUtils.getHttpFileContentLength(url);
            //计算切分后的文件大小
            long size = contentLength / Constant.THREAD_NUM;
            //计算分块个数
            for (int i = 0; i < Constant.THREAD_NUM; i++) {
                //计算下载起始位置
                long startPos = i * size;
                //计算下载结束位置
                long endPos;
                if (i == Constant.THREAD_NUM - 1) {
                    //下载的最后一块，将剩余的全部下载
                    endPos = 0;
                } else {
                    endPos = startPos + size;
                }
                //如果不是第一块，起始位置+1
                if (startPos != 0) {
                    startPos++;
                }
                //创建任务
                DownloaderTask downloaderTask = new DownloaderTask(url, startPos, endPos, i, countDownLatch);
                //将任务创建到线程池中
                Future<Boolean> future = poolExecutor.submit(downloaderTask);
                futureList.add(future);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 文件合并
     *
     * @param filename 文件名
     */
    public boolean merge(String filename) {
        LogUtils.info("开始合并文件{}", filename);
        byte[] buffer = new byte[Constant.BYTE_SIZE];
        int len = -1;
        try (RandomAccessFile accessFile = new RandomAccessFile(filename, "rw")) {
            for (int i = 0; i < Constant.THREAD_NUM; i++) {
                try (BufferedInputStream bis = new BufferedInputStream(Files.newInputStream(Paths.get(filename + ".temp" + i)))) {
                    while ((len = bis.read(buffer)) != -1) {
                        accessFile.write(buffer, 0, len);
                    }
                }
            }
            LogUtils.info("文件{}合并完毕", filename);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 删除临时文件
     */
    public boolean clearTemp(String filename) {
        for (int i = 0; i < Constant.THREAD_NUM; i++) {
            File file = new File(filename + ".temp" + i);
            file.delete();
        }
        return true;
    }
}

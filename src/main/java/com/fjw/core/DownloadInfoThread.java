package com.fjw.core;

import com.fjw.constant.Constant;

import java.util.concurrent.atomic.LongAdder;

/**
 * 展示下载信息
 */
public class DownloadInfoThread implements Runnable {
    //下载文件总大小
    private final long httpFileContentLength;
    //本地已下载文件的大小
    public static LongAdder finishedSize = new LongAdder();
    //本次累计下载的大小
    public static volatile LongAdder downSize = new LongAdder();
    //前一次下载的大小
    public double prevSize;

    public DownloadInfoThread(long httpFileContentLength) {
        this.httpFileContentLength = httpFileContentLength;
    }

    @Override
    public void run() {
        //计算文件总大小
        String httpFileSize = String.format("%.2f", httpFileContentLength / Constant.MB);
        //计算每秒下载速度 kb
        double speed = (downSize.doubleValue() - prevSize) * 10 / Constant.MB;
        prevSize = downSize.doubleValue();
        //剩余文件的大小
        double remainSize = httpFileContentLength - finishedSize.doubleValue() - downSize.doubleValue();
        //计算剩余时间
        String remainTime = String.format("%.2f", remainSize / Constant.MB / speed);
        if ("Infinity".equalsIgnoreCase(remainTime)) {
            remainTime = "-";
        }
        //已下载大小
        String currentDownSize = String.format("%.2f", (downSize.doubleValue() - finishedSize.doubleValue()) / Constant.MB);
        String downInfo = String.format("已下载%s mb/%smb , 速度 %.2fmb/s , 剩余时间 %ss ",
                currentDownSize, httpFileSize, speed, remainTime);
        System.out.print("\r");
        System.out.print(downInfo);
    }
}

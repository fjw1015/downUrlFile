package com.fjw.utils;

import java.net.HttpURLConnection;
import java.net.URL;

/**
 * http 相关工具类
 */
public class HttpUtils {
    /**
     * 获取下载文件大小
     *
     * @param url 链接
     */
    public static long getHttpFileContentLength(String url) throws Exception {
        HttpURLConnection httpUrlConnection = null;
        int contentLength;
        try {
            httpUrlConnection = getHttpUrlConnection(url);
            contentLength = httpUrlConnection.getContentLength();
        } finally {
            if (httpUrlConnection != null) {
                httpUrlConnection.disconnect();
            }
        }
        return contentLength;
    }

    /**
     * 获取http连接对象
     */
    public static HttpURLConnection getHttpUrlConnection(String url) throws Exception {
        URL httpUrl = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) httpUrl.openConnection();
        //向文件所在服务器发送消息 agent消息从浏览器请求中可以复制
        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/107.0.0.0 Safari/537.36");
        return connection;
    }

    /**
     * 获取下载文件名
     *
     * @param url
     * @return
     */
    public static String getHttpFileName(String url) {
        int index = url.lastIndexOf("/");
        return url.substring(index + 1);
    }

    /**
     * 分块下载
     *
     * @param url      链接
     * @param startPos 起始位置
     * @param endPos   结束位置
     */
    public static HttpURLConnection getHttpUrlConnection(String url, long startPos, long endPos) throws Exception {
        HttpURLConnection httpUrlConnection = getHttpUrlConnection(url);
        LogUtils.info("下载的区间是{} - {}", startPos, endPos);
        if (endPos != 0) {
            httpUrlConnection.setRequestProperty("RANGE", "bytes=" + startPos + "-" + endPos);
        } else {
            httpUrlConnection.setRequestProperty("RANGE", "bytes=" + startPos + "-");
        }
        return httpUrlConnection;
    }
}

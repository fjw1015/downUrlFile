package com.fjw;

import com.fjw.core.Downloader;

import java.util.Scanner;

/**
 * 编写实现多线程下载功能
 */
public class App {
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        String url = null;
        if (args == null || args.length == 0) {
            do {
                System.out.println("请输入下载地址");
                url = scanner.next();
            } while (url == null);
        } else {
            url = args[0];
        }
        Downloader downloader = new Downloader();
        downloader.download(url);

    }
}

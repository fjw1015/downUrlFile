package com.fjw.utils;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * 日志工具类
 */
public class LogUtils {
    private static void print(String msg, String level, Object... args) {
        if (args != null && args.length > 0) {
            msg = String.format(msg.replace("{}", "%s"), args);
        }
        String name = Thread.currentThread().getName();
        System.out.println(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")) + " " + name  + level +msg );
    }

    public static void info(String msg, Object... args) {
        print(msg,"-info-",args);
    }

    public static void error(String msg, Object... args) {
        print(msg,"-error-",args);
    }
}

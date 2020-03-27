package com.xiaochen.emvp.base.utils;

import timber.log.Timber;

/**
 * Log统一管理类
 *
 * @author zlc
 */
public class LogUtil {

    private LogUtil() {
    }

    //开发完毕将isDebug置为false
    private static boolean isDebug = true;

    public static void init() {
        if (isDebug) {
            Timber.plant(new Timber.DebugTree());
        }
    }

    /**
     * 打印i级别的log
     *
     * @param tag
     * @param msg
     */
    public static void i(String tag, String msg) {
        if (isDebug) {
            Timber.tag(tag).i(msg);
        }
    }

    /**
     * 打印e级别的log
     *
     * @param tag
     * @param msg
     */
    public static void e(String tag, String msg) {
        if (isDebug) {
            Timber.tag(tag).e(msg);
        }
    }

    /**
     * 方便打log
     *
     * @param object
     * @param msg
     */
    public static void i(Object object, String msg) {
        if (isDebug) {
            Timber.tag(object.getClass().getSimpleName()).i(msg);
        }
    }

    /**
     * 方便打log
     *
     * @param object
     * @param msg
     */
    public static void e(Object object, String msg) {
        if (isDebug) {
            Timber.tag(object.getClass().getSimpleName()).e(msg);
        }
    }

    /***
     * 错误日志
     * @param msg
     */
    public static void e(String msg) {
        Timber.tag("AndroidRuntime").e(msg);
    }
}

package com.xuanyuan.library.utils;

import android.util.Log;

/**
 * 作者：罗发新
 * 时间：2019/8/9 0009    星期五
 * 邮件：424533553@qq.com
 * 说明：
 */
public class LogPrint {
    private final static String TAG_I = "log";

    public static void e(String tag, String message) {
        Log.e(tag, message);
    }

    public static void i(String tag, String message) {
        Log.i(tag, message);
    }

    public static void i(String message) {
        Log.i(TAG_I, message);
    }

    public static void e(String message) {
        Log.e(TAG_I, message);
    }
}

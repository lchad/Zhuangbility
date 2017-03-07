package com.liuchad.zhuangbility.util;

import com.liuchad.zhuangbility.BuildConfig;
import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;

/**
 * Created by lchad on 2016/11/25.
 * Github: https://github.com/lchad
 */

public class LogUtils {

    private static final String TAG = "Palmap";

    public static void init() {
        if (BuildConfig.DEBUG) {
            Logger.init(TAG).methodCount(2).methodOffset(1);
        } else {
            Logger.init(TAG).logLevel(LogLevel.NONE);
        }
    }

    public static void d(String msg) {
        Logger.t(TAG).d(msg);
    }

    public static void e(String msg) {
        Logger.t(TAG).e(msg);
    }

    public static void e(String msg, Throwable tr) {
        Logger.t(TAG).e(tr, msg);
    }

    public static void i(String msg) {
        Logger.t(TAG).i(msg);
    }

    public static void v(String msg) {
        Logger.t(TAG).v(msg);
    }

    public static void w(String msg) {
        Logger.t(TAG).w(msg);
    }

    public static void v(String msg, Object... args) {
        Logger.t(TAG).v(msg, args);
    }


    public static void d(String msg, Object... args) {
        Logger.t(TAG).d(msg, args);
    }


    public static void i(String msg, Object... args) {
        Logger.t(TAG).i(msg, args);
    }


    public static void w(String msg, Object... args) {
        Logger.t(TAG).w(msg, args);
    }


    public static void w(Throwable tr) {
        Logger.w(TAG, tr);
    }

    public static void e(String msg, Object... args) {
        Logger.t(TAG).e(msg, args);
    }

    public static void e(Throwable tr, String msg, Object... args) {
        Logger.t(TAG).e(tr, msg, args);
    }

}
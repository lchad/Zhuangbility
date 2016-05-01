package com.liuchad.zhuangbility;

import android.app.Application;

/**
 * Created by liuchad on 16/4/3.
 */
public class ZbApp extends Application {

    private static ZbApp sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
    }

    public static ZbApp getInstance(){
        return sInstance;
    }

    @Override
    public String getPackageName() {
        return super.getPackageName();
    }
}

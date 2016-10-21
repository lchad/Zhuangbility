package com.liuchad.zhuangbility.base;

import android.app.Application;

/**
 * Created by liuchad on 16/4/3.
 */
public class App extends Application {

    private static App sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
    }

    public static App getInstance() {
        return sInstance;
    }

    @Override
    public String getPackageName() {
        return super.getPackageName();
    }
}

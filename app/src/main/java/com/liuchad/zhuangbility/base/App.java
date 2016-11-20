package com.liuchad.zhuangbility.base;

import android.app.Application;
//import com.squareup.leakcanary.LeakCanary;

/**
 * Created by liuchad on 16/4/3.
 */
public class App extends Application {

    private static App sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        //if (LeakCanary.isInAnalyzerProcess(this)) {
        //    // This process is dedicated to LeakCanary for heap analysis.
        //    // You should not init your app in this process.
        //    return;
        //}
        //LeakCanary.install(this);
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

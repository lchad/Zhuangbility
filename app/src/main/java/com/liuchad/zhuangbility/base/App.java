package com.liuchad.zhuangbility.base;

import android.app.Application;
import com.facebook.react.BuildConfig;
import com.facebook.react.ReactApplication;
import com.facebook.react.ReactNativeHost;
import com.facebook.react.ReactPackage;
import java.util.Arrays;
import java.util.List;
//import com.squareup.leakcanary.LeakCanary;

/**
 * Created by lchad on 16/4/3.
 */
public class App extends Application
    //implements ReactApplication
{

    private static App sInstance;

    public static final Thread.UncaughtExceptionHandler sUncaughtExceptionHandler = Thread
        .getDefaultUncaughtExceptionHandler();

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

    //private final ReactNativeHost mReactNativeHost = new ReactNativeHost(this) {
    //    @Override public boolean getUseDeveloperSupport() {
    //        return BuildConfig.DEBUG;
    //    }
    //
    //    @Override protected List<ReactPackage> getPackages() {
    //        return Arrays.asList();
    //    }
    //};
    //
    //@Override public ReactNativeHost getReactNativeHost() {
    //    return null;
    //}
}

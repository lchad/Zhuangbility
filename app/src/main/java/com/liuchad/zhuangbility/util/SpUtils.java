package com.liuchad.zhuangbility.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.liuchad.zhuangbility.base.App;

/**
 * Created by lchad on 2016/11/18.
 * Github: https://github.com/lchad
 */
public class SpUtils {

    private static SpUtils sInstance;
    private SharedPreferences mManager;
    private static final String SP_NAME = "palmap";

    private SpUtils() {
    }

    public boolean checkNull() {
        if(mManager == null){
            mManager = App.getInstance().getSharedPreferences(
                    SP_NAME, Context.MODE_PRIVATE);
        }
        return false;
    }

    public static SpUtils getInstance() {
        if (sInstance == null) {
            sInstance = new SpUtils();
        }
        return sInstance;
    }

    public void putBoolean(String key, boolean value) {
        if(checkNull()){
            return;
        }
        mManager.edit().putBoolean(key, value).apply();
    }

    public void putString(String key, String value) {
        if(checkNull()){
            return;
        }
        mManager.edit().putString(key, value).commit();
    }

    public void putInt(String key, int value) {
        if(checkNull()){
            return;
        }
        mManager.edit().putInt(key, value).apply();
    }

    public void putLong(String key, long value) {
        if(checkNull()){
            return;
        }
        mManager.edit().putLong(key, value).apply();
    }

    public long getLong(String key) {
        if(checkNull()){
            return 0L;
        }
        return mManager.getLong(key, 0);
    }

    public long getLong(String key, long value) {
        if(checkNull()){
            return value;
        }
        return mManager.getLong(key, value);
    }

    public int getInt(String key) {
        if(checkNull()){
            return 0;
        }
        return mManager.getInt(key, 0);
    }

    public int getInt(String key, int value) {
        if(checkNull()){
            return value;
        }
        return mManager.getInt(key, value);
    }

    public Editor editor() {
        return mManager.edit();
    }

    public String getString(String key) {
        if(checkNull()){
            return "";
        }
        return mManager.getString(key, "");
    }

    public boolean getBoolean(String key) {
        if(checkNull()){
            return false;
        }
        if (!mManager.contains(key)) {
            return false;
        }
        return mManager.getBoolean(key, false);
    }

    public boolean getBoolean(String key, boolean a) {
        if(checkNull()){
            return a;
        }
        if (!mManager.contains(key)) {
            return a;
        }
        return mManager.getBoolean(key, a);
    }

    public boolean contain(String key) {
        return mManager.contains(key);
    }

    public void recycle() {
        sInstance = null;
        mManager = null;
    }
}

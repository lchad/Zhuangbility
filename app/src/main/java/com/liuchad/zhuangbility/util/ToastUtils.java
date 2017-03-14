package com.liuchad.zhuangbility.util;

import android.os.Handler;
import android.widget.Toast;

import com.liuchad.zhuangbility.base.App;

/**
 * Created by lchad on 2016/11/10.
 * Github: https://github.com/lchad
 */

public class ToastUtils {

    public static void showToast(String msg) {
        Toast.makeText(App.getInstance(), msg, Toast.LENGTH_SHORT).show();
    }

    public static void showToast(int msgId) {
        Toast.makeText(App.getInstance(), msgId, Toast.LENGTH_SHORT).show();
    }

    public static void showToast(Handler handler, final String msg) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(App.getInstance(), msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void showToast(Handler handler, final int msgId) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(App.getInstance(), msgId, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void showLongToast(String msg) {
        Toast.makeText(App.getInstance(), msg, Toast.LENGTH_LONG).show();
    }


    public static void showLongToast(int msgId) {
        Toast.makeText(App.getInstance(), App.getInstance().getResources().getString(msgId), Toast.LENGTH_LONG).show();
    }

}

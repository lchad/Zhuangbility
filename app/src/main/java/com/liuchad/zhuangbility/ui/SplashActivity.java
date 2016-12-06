package com.liuchad.zhuangbility.ui;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import com.liuchad.zhuangbility.R;
import com.liuchad.zhuangbility.base.BaseActivity;
import com.liuchad.zhuangbility.util.CommonUtils;
import in.workarounds.bundler.Bundler;

public class SplashActivity extends BaseActivity {

    /** 申请权限返回标志字段 */
    private static final int REQUEST_WRITE_STORAGE = 112;

    private Handler mHandler = new Handler();

    @Override protected int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override protected void initView() {

    }

    @Override protected void initData() {
        boolean hasPermission = (ContextCompat.checkSelfPermission(SplashActivity.this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
        if (!hasPermission) {
            ActivityCompat.requestPermissions(SplashActivity.this,
                new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE },
                REQUEST_WRITE_STORAGE);
            return;
        }
        enterMain();
    }

    @Override protected void initInjector() {

    }

    private void enterMain() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Bundler.mainActivity().start(SplashActivity.this);
                finish();
            }
        }, 1000);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
        @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_WRITE_STORAGE: {
                if (grantResults.length <= 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    CommonUtils.showToast(getString(R.string.rejected_hint));
                } else {
                    Bundler.mainActivity().start(SplashActivity.this);
                    finish();
                }
            }
        }
    }

    @Override protected boolean setFullScreen() {
        return true;
    }

}

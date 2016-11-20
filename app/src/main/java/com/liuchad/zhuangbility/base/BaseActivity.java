package com.liuchad.zhuangbility.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/10/15.
 */

public abstract class BaseActivity extends AppCompatActivity {
    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(getLayoutId());
        initInjector();
        ButterKnife.bind(this);
        initView();
        initData();
    }

    @Override protected void onDestroy() {
        super.onDestroy();
    }

    protected abstract int getLayoutId();

    protected abstract void initView();

    protected abstract void initData();

    protected abstract void initInjector();
}

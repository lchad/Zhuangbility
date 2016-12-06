package com.liuchad.zhuangbility.ui;

import com.liuchad.zhuangbility.R;
import com.liuchad.zhuangbility.base.BaseActivity;
import in.workarounds.bundler.Bundler;
import in.workarounds.bundler.annotations.RequireBundler;

@RequireBundler
public class AboutActivity extends BaseActivity {

    @Override protected int getLayoutId() {
        return R.layout.activity_about;
    }

    @Override protected void initInjector() {
        Bundler.inject(this);
    }

    @Override protected void initView() {

    }

    @Override protected void initData() {

    }
}

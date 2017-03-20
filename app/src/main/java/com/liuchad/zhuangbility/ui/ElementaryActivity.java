package com.liuchad.zhuangbility.ui;

import com.liuchad.zhuangbility.R;
import com.liuchad.zhuangbility.base.BaseActivity;

import in.workarounds.bundler.Bundler;
import in.workarounds.bundler.annotations.RequireBundler;

@RequireBundler
public class ElementaryActivity extends BaseActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_elementary;
    }

    @Override
    protected void initInjector() {
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, Bundler.elementaryFragment().create())
                .commit();
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }
}

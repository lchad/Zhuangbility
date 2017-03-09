package com.liuchad.zhuangbility.ui;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.liuchad.zhuangbility.R;
import com.liuchad.zhuangbility.base.BaseActivity;

import butterknife.BindView;
import in.workarounds.bundler.Bundler;

public class SearchZhuangbiActivity extends BaseActivity {

    @BindView(R.id.container) LinearLayout mContainer;
    @BindView(R.id.activity_theme) RelativeLayout mActivityTheme;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_search_zhuangbi;
    }

    @Override
    protected void initInjector() {

    }

    @Override
    protected void initView() {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.container, Bundler.elementaryFragment().create());
        transaction.commit();
    }

    @Override
    protected void initData() {

    }
}

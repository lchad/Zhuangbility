package com.liuchad.zhuangbility.ui;

import android.support.v7.widget.Toolbar;
import android.view.View;
import butterknife.Bind;
import com.liuchad.zhuangbility.R;
import com.liuchad.zhuangbility.base.BaseActivity;
import in.workarounds.bundler.Bundler;
import in.workarounds.bundler.annotations.RequireBundler;

@RequireBundler
public class NativeAboutActivity extends BaseActivity {

    @Bind(R.id.toolbar) Toolbar toolbar;

    @Override protected int getLayoutId() {
        return R.layout.activity_native_about;
    }

    @Override protected void initInjector() {
        Bundler.inject(this);
    }

    @Override protected void initView() {
        toolbar.setBackgroundColor(getResources().getColor(R.color.theme_light));
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_action_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        toolbar.setTitle(R.string.about);
    }

    @Override protected void initData() {

    }
}

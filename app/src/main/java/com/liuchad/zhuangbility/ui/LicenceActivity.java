package com.liuchad.zhuangbility.ui;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.liuchad.zhuangbility.R;
import com.liuchad.zhuangbility.adapter.LicenceAdapter;
import com.liuchad.zhuangbility.base.BaseActivity;

import butterknife.Bind;
import in.workarounds.bundler.Bundler;
import in.workarounds.bundler.annotations.RequireBundler;

@RequireBundler
public class LicenceActivity extends BaseActivity {

    @Bind(R.id.recyclerview)
    RecyclerView mRecyclerView;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    private LicenceAdapter mLicenceAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_licence;
    }

    @Override
    protected void initInjector() {
        Bundler.inject(this);
    }

    @Override
    protected void initView() {
        mToolbar.setBackgroundColor(getResources().getColor(R.color.theme_light));
        mToolbar.setTitle(R.string.software_licence);
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        RecyclerView.LayoutManager manager = new LinearLayoutManager(LicenceActivity.this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(manager);
        mLicenceAdapter = new LicenceAdapter(LicenceActivity.this);
        mRecyclerView.setAdapter(mLicenceAdapter);
    }

    @Override
    protected void initData() {

    }
}

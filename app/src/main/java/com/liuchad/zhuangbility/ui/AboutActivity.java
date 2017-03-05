package com.liuchad.zhuangbility.ui;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.liuchad.zhuangbility.R;
import com.liuchad.zhuangbility.base.BaseActivity;

import butterknife.Bind;
import in.workarounds.bundler.annotations.RequireBundler;

@RequireBundler
public class AboutActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.toolbar_layout)
    CollapsingToolbarLayout mToolbarLayout;
    @Bind(R.id.app_bar)
    AppBarLayout mAppBar;
    @Bind(R.id.about_app_title)
    TextView mAboutAppTitle;
    @Bind(R.id.about_app_light_description)
    TextView mAboutAppLightDescription;
    @Bind(R.id.about_version)
    TextView mAboutVersion;
    @Bind(R.id.about_app_card)
    CardView mAboutAppCard;
    @Bind(R.id.donald_header_img)
    ImageView mDonaldHeaderImg;
    @Bind(R.id.profile_img)
    ImageView mProfileImg;
    @Bind(R.id.lchad)
    TextView mLchad;
    @Bind(R.id.lchad_role)
    TextView mLchadRole;
    @Bind(R.id.lchad_description)
    TextView mLchadDescription;
    @Bind(R.id.about_mail)
    TextView mAboutMail;
    @Bind(R.id.about_github)
    TextView mAboutGithub;
    @Bind(R.id.about_sina_weibo)
    TextView mAboutSinaWeibo;
    @Bind(R.id.about_zhihu)
    TextView mAboutZhihu;
    @Bind(R.id.about_donald_card)
    CardView mAboutDonaldCard;
    @Bind(R.id.about_license_title)
    TextView mAboutLicenseTitle;
    @Bind(R.id.about_license_icon)
    ImageView mAboutLicenseIcon;
    @Bind(R.id.about_license_item)
    TextView mAboutLicenseItem;
    @Bind(R.id.about_license_item_sub)
    TextView mAboutLicenseItemSub;
    @Bind(R.id.ll_about_license)
    LinearLayout mLlAboutLicense;
    @Bind(R.id.about_libs_icon)
    ImageView mAboutLibsIcon;
    @Bind(R.id.about_libs_item)
    TextView mAboutLibsItem;
    @Bind(R.id.about_libs_item_sub)
    TextView mAboutLibsItemSub;
    @Bind(R.id.ll_about_libs)
    LinearLayout mLlAboutLibs;
    @Bind(R.id.about_license_card)
    CardView mAboutLicenseCard;
    @Bind(R.id.fab)
    FloatingActionButton mFab;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_scrolling;
    }

    @Override
    protected void initInjector() {

    }

    @Override
    protected void initView() {
        mToolbar.setBackgroundColor(getResources().getColor(R.color.theme_light));
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mToolbar.setTitle(R.string.about);

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        mAboutSinaWeibo.setOnClickListener(this);
        mAboutGithub.setOnClickListener(this);
        mAboutZhihu.setOnClickListener(this);
        mAboutMail.setOnClickListener(this);
        mLlAboutLibs.setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.about_sina_weibo:
                gotoUrl(WEIBO_URL);
                break;
            case R.id.about_zhihu:
                gotoUrl(ZHIHU_URL);
                break;
            case R.id.about_github:
                gotoUrl(GITHUB_URL);
                break;
            case R.id.about_mail:
                sendMail();
                break;
            case R.id.ll_about_libs:
                showLicenceDialog();
                break;
        }
    }

    private void showLicenceDialog() {

    }

    private void gotoUrl(String url) {
        Uri uri = Uri.parse(url);
        startActivity(new Intent(Intent.ACTION_VIEW, uri));
    }

    private void sendMail() {
        Intent data = new Intent(Intent.ACTION_SENDTO);
        data.setData(Uri.parse("mailto:liuchad@outlook.com"));
        startActivity(data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_about, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_share:

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public static final String WEIBO_URL = "https://github.com/lchad";
    public static final String ZHIHU_URL = "https://www.zhihu.com/people/lchad";
    public static final String GITHUB_URL = "http://weibo.com/lchad";
}

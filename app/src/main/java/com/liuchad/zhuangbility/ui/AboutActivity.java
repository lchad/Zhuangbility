package com.liuchad.zhuangbility.ui;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.liuchad.zhuangbility.Constants;
import com.liuchad.zhuangbility.R;
import com.liuchad.zhuangbility.base.BaseActivity;

import butterknife.BindView;
import cn.sharesdk.onekeyshare.OnekeyShare;
import in.workarounds.bundler.Bundler;
import in.workarounds.bundler.annotations.RequireBundler;

@RequireBundler
public class AboutActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.toolbar_layout) CollapsingToolbarLayout mToolbarLayout;
    @BindView(R.id.app_bar) AppBarLayout mAppBar;
    @BindView(R.id.about_app_title) TextView mAboutAppTitle;
    @BindView(R.id.about_app_light_description) TextView mAboutAppLightDescription;
    @BindView(R.id.about_version) TextView mAboutVersion;
    @BindView(R.id.about_app_card) CardView mAboutAppCard;
    @BindView(R.id.donald_header_img) ImageView mDonaldHeaderImg;
    @BindView(R.id.profile_img) ImageView mProfileImg;
    @BindView(R.id.lchad) TextView mLchad;
    @BindView(R.id.lchad_role) TextView mLchadRole;
    @BindView(R.id.lchad_description) TextView mLchadDescription;
    @BindView(R.id.about_mail) TextView mAboutMail;
    @BindView(R.id.about_github) TextView mAboutGithub;
    @BindView(R.id.about_sina_weibo) TextView mAboutSinaWeibo;
    @BindView(R.id.about_zhihu) TextView mAboutZhihu;
    @BindView(R.id.about_donald_card) CardView mAboutDonaldCard;
    @BindView(R.id.about_license_title) TextView mAboutLicenseTitle;
    @BindView(R.id.about_license_icon) ImageView mAboutLicenseIcon;
    @BindView(R.id.about_license_item) TextView mAboutLicenseItem;
    @BindView(R.id.about_license_item_sub) TextView mAboutLicenseItemSub;
    @BindView(R.id.ll_about_license) LinearLayout mLlAboutLicense;
    @BindView(R.id.about_libs_icon) ImageView mAboutLibsIcon;
    @BindView(R.id.about_libs_item) TextView mAboutLibsItem;
    @BindView(R.id.about_libs_item_sub) TextView mAboutLibsItemSub;
    @BindView(R.id.ll_about_libs) LinearLayout mLlAboutLibs;
    @BindView(R.id.about_license_card) CardView mAboutLicenseCard;
    @BindView(R.id.fab) FloatingActionButton mFab;

    private OnekeyShare mOnekeyShare;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_about;
    }

    @Override
    protected void initInjector() {
        mOnekeyShare = new OnekeyShare();
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

            }
        });
        mAboutSinaWeibo.setOnClickListener(this);
        mAboutGithub.setOnClickListener(this);
        mAboutZhihu.setOnClickListener(this);
        mAboutMail.setOnClickListener(this);
        mLlAboutLibs.setOnClickListener(this);
        mLlAboutLicense.setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.about_sina_weibo:
                gotoUrl(Constants.WEIBO_URL);
                break;
            case R.id.about_zhihu:
                gotoUrl(Constants.ZHIHU_URL);
                break;
            case R.id.about_github:
                gotoUrl(Constants.GITHUB_URL);
                break;
            case R.id.about_mail:
                sendMail();
                break;
            case R.id.ll_about_libs:
                Bundler.licenceActivity().start(AboutActivity.this);
                break;
            case R.id.ll_about_license:
                gotoUrl(Constants.REPOSITORY_URL);
                break;
        }
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
                mOnekeyShare.setTitle("Zhuangbility");
                mOnekeyShare.show(AboutActivity.this);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}

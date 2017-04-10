package com.liuchad.zhuangbility.ui;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import com.liuchad.zhuangbility.Mode;
import com.liuchad.zhuangbility.R;
import com.liuchad.zhuangbility.base.BaseActivity;
import com.liuchad.zhuangbility.event.MultiPicSelectedEvent;
import com.liuchad.zhuangbility.event.SinglePicSelectedEvent;
import com.liuchad.zhuangbility.util.CommonUtils;

import butterknife.BindView;
import in.workarounds.bundler.Bundler;
import in.workarounds.bundler.annotations.Arg;
import in.workarounds.bundler.annotations.RequireBundler;
import in.workarounds.bundler.annotations.Required;
import java.io.File;
import java.util.ArrayList;
import org.greenrobot.eventbus.EventBus;

@RequireBundler
public class MultiImageSelectorActivity extends BaseActivity
    implements MultiImageSelectorFragment.Callback {

    private ArrayList<String> resultList = new ArrayList<>();

    public @Arg @Required() int selectMode;
    public @Arg @Required(false) int maxSelectCount = 10;
    public @Arg @Required() boolean showCamera;

    @BindView(R.id.toolbar) Toolbar mToolbar;

    @SuppressWarnings("FieldCanBeLocal") private MenuItem mFinishMenu;

    @Override protected int getLayoutId() {
        return R.layout.activity_image_selector;
    }

    @Override protected void initInjector() {
        Bundler.inject(this);
    }

    @Override protected void initView() {
        mToolbar.setBackgroundColor(getResources().getColor(R.color.theme_light));
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.select_album);
        }
        getSupportFragmentManager().beginTransaction()
            .add(R.id.image_grid, Bundler.multiImageSelectorFragment(Mode.MODE_SINGLE, true).create())
            .commit();
    }

    @Override protected void initData() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_image_select, menu);
        mFinishMenu = menu.findItem(R.id.action_finish);
        mFinishMenu.setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_finish:
                MultiPicSelectedEvent event = new MultiPicSelectedEvent();
                event.selectedPaths = resultList;
                EventBus.getDefault().post(event);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSingleImageSelected(String path) {
//        SinglePicSelectedEvent event = new SinglePicSelectedEvent();
//        event.path = path;
//        EventBus.getDefault().post(event);
//        finish();
        if (TextUtils.isEmpty(path)) {
            return;
        }

        Uri uri = Uri.fromFile(new File(path));
        CommonUtils.startPhotoZoom(MultiImageSelectorActivity.this, uri, REQUEST_CODE_CAMERA_CROP);
    }

    private static final int REQUEST_CODE_CAMERA_CROP = 103;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_CODE_CAMERA_CROP: //裁剪
                if (resultCode != RESULT_OK) {
                    return;
                }
                String cameraPicPath = CommonUtils.SDCARD + "/" + "temp.jpg";
//                cropImage(cameraPicPath);
                break;
        }
    }

    @Override
    public void onImageSelected(String path) {
        if (!resultList.contains(path)) {
            resultList.add(path);
        }
    }

    @Override
    public void onImageUnselected(String path) {
        if (resultList.contains(path)) {
            resultList.remove(path);
        }
    }

    @Override
    public void onCameraShot(File imageFile) {
        if (imageFile != null) {
            SinglePicSelectedEvent event = new SinglePicSelectedEvent();
            event.path = imageFile.getAbsolutePath();
            EventBus.getDefault().post(event);
        }
    }
}

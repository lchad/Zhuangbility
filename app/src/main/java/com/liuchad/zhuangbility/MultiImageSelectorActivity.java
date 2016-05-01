package com.liuchad.zhuangbility;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import com.example.liuchad.zhuangbidemo.R;
import java.io.File;
import java.util.ArrayList;

public class MultiImageSelectorActivity extends AppCompatActivity implements MultiImageSelectorFragment.Callback {
    public static final int REQUEST_CODE = 3027;
    /**
     * 最大图片选择次数，int类型，默认9
     */
    public static final String EXTRA_SELECT_COUNT = "max_select_count";
    /**
     * 图片选择模式，默认多选
     */
    public static final String EXTRA_SELECT_MODE = "select_count_mode";
    /**
     * 是否显示相机，默认显示
     */
    public static final String EXTRA_SHOW_CAMERA = "show_camera";
    /**
     * 选择结果，返回为 ArrayList&lt;String&gt; 图片路径集合
     */
    public static final String EXTRA_RESULT = "select_result";
    /**
     * 默认选择集
     */
    public static final String EXTRA_DEFAULT_SELECTED_LIST = "default_list";

    /**
     * 单选
     */
    public static final int MODE_SINGLE = 0;
    /**
     * 多选
     */
    public static final int MODE_MULTI = 1;

    private ArrayList<String> resultList = new ArrayList<>();
    private int mMaxSelectCount;

    /**
     * 图片多选界面
     *
     * @param context        上下文对象
     * @param showCamera     是否显示拍照按钮
     * @param maxSelectCount 图片最多选择数
     * @param requestCode    Activity请求码
     */
    public static void actionStart(Activity context, boolean showCamera, int maxSelectCount, int requestCode) {
        Intent intent = new Intent(context, MultiImageSelectorActivity.class);
        // whether show camera
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, showCamera);
        // max select image amount
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, maxSelectCount);
        // select mode (MultiImageSelectorActivity.MODE_SINGLE OR MultiImageSelectorActivity.MODE_MULTI)
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MultiImageSelectorActivity.MODE_MULTI);
        context.startActivityForResult(intent, requestCode);
    }

    /**
     * 图片多选界面
     *
     * @param fragment       请求界面
     * @param showCamera     是否显示拍照按钮
     * @param maxSelectCount 图片最多选择数
     * @param requestCode    Activity请求码
     */
    public static void actionStart(Fragment fragment, boolean showCamera, int maxSelectCount, int requestCode) {
        Intent intent = new Intent(fragment.getActivity(), MultiImageSelectorActivity.class);
        // whether show camera
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, showCamera);
        // max select image amount
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, maxSelectCount);
        // select mode (MultiImageSelectorActivity.MODE_SINGLE OR MultiImageSelectorActivity.MODE_MULTI)
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MultiImageSelectorActivity.MODE_MULTI);
        fragment.startActivityForResult(intent, requestCode);
    }

    /**
     * 图片单选
     *
     * @param context     上下文对象
     * @param showCamera  是否显示拍照按钮
     * @param requestCode Activity请求码
     */
    public static void actionStartSingleChoice(Activity context, boolean showCamera, int requestCode) {
        Intent intent = new Intent(context, MultiImageSelectorActivity.class);
        // whether show camera
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, showCamera);
        // select mode (MultiImageSelectorActivity.MODE_SINGLE OR MultiImageSelectorActivity.MODE_MULTI)
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MultiImageSelectorActivity.MODE_SINGLE);
        context.startActivityForResult(intent, requestCode);
    }

    /**
     * 图片单选
     *
     * @param fragment    请求界面
     * @param showCamera  是否显示拍照按钮
     * @param requestCode Activity请求码
     */
    public static void actionStartSingleChoice(Fragment fragment, boolean showCamera, int requestCode) {
        Intent intent = new Intent(fragment.getActivity(), MultiImageSelectorActivity.class);
        // whether show camera
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, showCamera);
        // select mode (MultiImageSelectorActivity.MODE_SINGLE OR MultiImageSelectorActivity.MODE_MULTI)
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MultiImageSelectorActivity.MODE_SINGLE);
        fragment.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_selector);
        initView();

        Intent intent = getIntent();
        mMaxSelectCount = intent.getIntExtra(EXTRA_SELECT_COUNT, 10);
        int mode = intent.getIntExtra(EXTRA_SELECT_MODE, MODE_MULTI);
        boolean isShow = intent.getBooleanExtra(EXTRA_SHOW_CAMERA, true);
        if (mode == MODE_MULTI && intent.hasExtra(EXTRA_DEFAULT_SELECTED_LIST)) {
            resultList = intent.getStringArrayListExtra(EXTRA_DEFAULT_SELECTED_LIST);
        }

        Bundle bundle = new Bundle();
        bundle.putInt(MultiImageSelectorFragment.EXTRA_SELECT_COUNT, mMaxSelectCount);
        bundle.putInt(MultiImageSelectorFragment.EXTRA_SELECT_MODE, mode);
        bundle.putBoolean(MultiImageSelectorFragment.EXTRA_SHOW_CAMERA, isShow);
        bundle.putStringArrayList(MultiImageSelectorFragment.EXTRA_DEFAULT_SELECTED_LIST, resultList);

        getSupportFragmentManager().beginTransaction()
            .add(R.id.image_grid, Fragment.instantiate(this, MultiImageSelectorFragment.class.getName(), bundle))
            .commit();
    }

    private MenuItem mFinishMenu;

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
                Intent data = new Intent();
                data.putStringArrayListExtra(EXTRA_RESULT, resultList);
                setResult(RESULT_OK, data);
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void initView() {
    }

    @Override
    public void onSingleImageSelected(String path) {
        Intent data = new Intent();
        data.putExtra(EXTRA_RESULT, path);
        setResult(RESULT_OK, data);
        finish();
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
            Intent data = new Intent();
            data.putExtra(EXTRA_RESULT, imageFile.getAbsolutePath());
            setResult(RESULT_OK, data);
            finish();
        }
    }
}

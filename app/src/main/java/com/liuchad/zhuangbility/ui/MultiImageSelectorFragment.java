package com.liuchad.zhuangbility.ui;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.ListPopupWindow;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.TextView;
import butterknife.Bind;
import com.liuchad.zhuangbility.R;
import com.liuchad.zhuangbility.Constants;
import com.liuchad.zhuangbility.Mode;
import com.liuchad.zhuangbility.adapter.ImageFolderAdapter;
import com.liuchad.zhuangbility.adapter.ImageGridAdapter;
import com.liuchad.zhuangbility.base.BaseFragment;
import com.liuchad.zhuangbility.util.CommonUtils;
import com.liuchad.zhuangbility.vo.ImageFile;
import com.liuchad.zhuangbility.vo.ImageFolder;
import in.workarounds.bundler.Bundler;
import in.workarounds.bundler.annotations.Arg;
import in.workarounds.bundler.annotations.RequireBundler;
import in.workarounds.bundler.annotations.Required;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

@RequireBundler
public class MultiImageSelectorFragment extends BaseFragment {

    /** 默认选择的数据集 */
    public static final String EXTRA_DEFAULT_SELECTED_LIST = "default_result";
    private final static float IMAGE_FOLDER_ITEM_HEIGHT = 92f;
    // 不同loader定义
    private static final int LOADER_ALL = 0;
    private static final int LOADER_CATEGORY = 1;
    // 请求加载系统照相机
    private static final int REQUEST_CAMERA = 100;

    public @Arg @Required() int selectMode;
    public @Arg @Required(false) int maxSelectCount = 10;
    public @Arg @Required() boolean showCamera;

    // 图片Grid
    public @Bind(R.id.grid) GridView mGridView;
    public @Bind(R.id.view_shadow_bg) View mViewShadowBg;
    // 类别
    public @Bind(R.id.category_btn) TextView mCategoryText;
    // 预览按钮
    public @Bind(R.id.preview) Button mPreviewBtn;
    // 底部View
    public @Bind(R.id.footer) View mPopupAnchorView;

    // 结果数据
    private ArrayList<String> resultList = new ArrayList<>();
    // 文件夹数据
    private ArrayList<ImageFolder> mResultImageFolder = new ArrayList<>();
    private Callback mCallback;
    private ImageGridAdapter mImageAdapter;
    private ImageFolderAdapter mFolderAdapter;
    private ListPopupWindow mFolderPopupWindow;
    private boolean hasFolderGened = false;
    private boolean mIsShowCamera = false;
    private int mGridViewWidth;
    private int mGridViewHeight;
    private File mTmpFile;

    /**
     * Loader 读取系统图片的回调
     */
    private LoaderManager.LoaderCallbacks<Cursor> mLoaderCallback = new LoaderManager.LoaderCallbacks<Cursor>() {

        private final String[] IMAGE_PROJECTION = {
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.DATE_ADDED,
            MediaStore.Images.Media._ID
        };

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            if (id == LOADER_ALL) {
                return new CursorLoader(getActivity(), MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION,
                    null,
                    null,
                    IMAGE_PROJECTION[2] + " DESC");
            } else if (id == LOADER_CATEGORY) {
                return new CursorLoader(getActivity(), MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION,
                    IMAGE_PROJECTION[0] + " like '%" + args.getString("path") + "%'", null,
                    IMAGE_PROJECTION[2] + " DESC");
            }

            return null;
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            if (data == null || data.getCount() <= 0) {
                return;
            }
            // 读取图片数据
            List<ImageFile> images = new ArrayList<>();
            data.moveToFirst();
            do {
                String path = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[0]));
                String name = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[1]));
                long dateTime = data.getLong(data.getColumnIndexOrThrow(IMAGE_PROJECTION[2]));
                File file = new File(path);
                if (file.exists()) {
                    ImageFile image = new ImageFile(path, name, dateTime);
                    images.add(image);
                    if (!hasFolderGened) {
                        // 获取文件夹名称
                        File imageFile = new File(path);
                        File folderFile = imageFile.getParentFile();
                        ImageFolder imageFolder = new ImageFolder();
                        imageFolder.name = folderFile.getName();
                        imageFolder.path = folderFile.getAbsolutePath();
                        imageFolder.cover = image;
                        if (!mResultImageFolder.contains(imageFolder)) {
                            List<ImageFile> imageList = new ArrayList<>();
                            imageList.add(image);
                            imageFolder.images = imageList;
                            mResultImageFolder.add(imageFolder);
                        } else {
                            // 更新
                            ImageFolder folder = mResultImageFolder.get(mResultImageFolder.indexOf(imageFolder));
                            folder.images.add(image);
                        }
                    }
                }
            } while (data.moveToNext());

            mImageAdapter.setData(images);
            // 设定默认选择
            if (resultList != null && resultList.size() > 0) {
                mImageAdapter.setDefaultSelected(resultList);
            }

            mFolderAdapter.setData(mResultImageFolder);
            hasFolderGened = true;
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }
    };

    @SuppressWarnings("deprecation") @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallback = (Callback) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(
                "The Activity must implement MultiImageSelectorFragment.Callback interface...");
        }
    }

    @Override protected int getLayoutId() {
        return R.layout.fragment_multi_image;
    }

    @Override public void initView() {
        mGridView.setNumColumns(3);
        // 是否显示照相机
        mImageAdapter = new ImageGridAdapter(getActivity(), mIsShowCamera);
        // 是否显示选择指示器
        mImageAdapter.showSelectIndicator(selectMode == Mode.MODE_MULTI);
        mGridView.setAdapter(mImageAdapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (mImageAdapter.isShowCamera() && i == 0) {
                    showCameraAction();
                } else {
                    ImageFile image = (ImageFile) adapterView.getAdapter().getItem(i);
                    selectImageFromGrid(image, selectMode);
                }
            }
        });
        mFolderAdapter = new ImageFolderAdapter(getActivity());

        mCategoryText.setText(R.string.folder_all);
        mCategoryText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mFolderPopupWindow.isShowing()) {
                    mFolderPopupWindow.dismiss();
                } else {
                    setFolderPopWindowSize(mGridViewWidth, mGridViewHeight);
                    mFolderPopupWindow.show();
                    mViewShadowBg.setVisibility(View.VISIBLE);
                    int index = mFolderAdapter.getSelectIndex();
                    index = index == 0 ? index : index - 1;
                    //noinspection ConstantConditions
                    mFolderPopupWindow.getListView().setSelection(index);
                }
            }
        });
        mPreviewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO 预览
            }
        });
    }

    @Override protected void initData() {
        // 默认选择
        if (selectMode == Mode.MODE_MULTI) {
            ArrayList<String> tmp = getArguments().getStringArrayList(EXTRA_DEFAULT_SELECTED_LIST);
            if (tmp != null && tmp.size() > 0) {
                resultList = tmp;
            }
        }

        setPreViewStatus();

        monitorGridViewSizeChange();
    }

    @Override protected void initInjector() {
        Bundler.inject(this);
    }

    /**
     * 设置相册文件夹 PopupWindow 的尺寸
     *
     * @param width 宽度
     * @param contentViewHeight 高度
     */
    private void setFolderPopWindowSize(int width, int contentViewHeight) {
        int maxHeight = contentViewHeight * 5 / 7;
        int realHeight = mFolderAdapter.getCount() * CommonUtils.dip2px(getContext(), IMAGE_FOLDER_ITEM_HEIGHT);

        int height = realHeight < maxHeight ? realHeight : maxHeight;
        if (mFolderPopupWindow == null) {
            createPopupFolderList(width, height);
        } else {
            mFolderPopupWindow.setWidth(width);
            mFolderPopupWindow.setHeight(height);
        }
    }

    /**
     * 创建弹出的相册文件夹的 PopupWindow
     *
     * @param width PopupWindow 宽度
     * @param height PopupWindow 高度
     */
    private void createPopupFolderList(int width, int height) {
        mFolderPopupWindow = new ListPopupWindow(getActivity());
        mFolderPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mFolderPopupWindow.setAdapter(mFolderAdapter);
        mFolderPopupWindow.setContentWidth(width);
        mFolderPopupWindow.setWidth(width);
        mFolderPopupWindow.setHeight(height);
        mFolderPopupWindow.setAnchorView(mPopupAnchorView);
        mFolderPopupWindow.setModal(true);
        mFolderPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                mViewShadowBg.setVisibility(View.GONE);
            }
        });
        mFolderPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                mFolderAdapter.setSelectIndex(i);

                final int index = i;
                final AdapterView v = adapterView;

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mFolderPopupWindow.dismiss();
                        if (index == 0) {
                            getActivity().getSupportLoaderManager().restartLoader(LOADER_ALL, null, mLoaderCallback);
                            mCategoryText.setText(R.string.folder_all);
                            mImageAdapter.setShowCamera(mIsShowCamera);
                        } else {
                            ImageFolder imageFolder = (ImageFolder) v.getAdapter().getItem(index);
                            if (null != imageFolder) {
                                mImageAdapter.setData(imageFolder.images);
                                mCategoryText.setText(imageFolder.name);
                                // 设定默认选择
                                if (resultList != null && resultList.size() > 0) {
                                    mImageAdapter.setDefaultSelected(resultList);
                                }
                            }
                            mImageAdapter.setShowCamera(false);
                        }
                        // 滑动到最初始位置
                        mGridView.smoothScrollToPosition(0);
                    }
                }, 100);
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // 首次加载所有图片
        int permissionCheck =
            ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                getActivity(), new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE },
                Constants.WRITE_EXTERNAL_STORAGE);
        } else {
            getActivity().getSupportLoaderManager().initLoader(LOADER_ALL, null, mLoaderCallback);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 相机拍照完成后，返回图片路径
        if (requestCode == REQUEST_CAMERA) {
            if (resultCode == Activity.RESULT_OK) {
                if (mTmpFile != null) {
                    if (mCallback != null) {
                        mCallback.onCameraShot(mTmpFile);
                    }
                }
            } else {
                if (mTmpFile != null && mTmpFile.exists()) {
                    //noinspection ResultOfMethodCallIgnored
                    mTmpFile.delete();
                }
            }
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        // 处理屏幕旋转问题
        if (mFolderPopupWindow != null && mFolderPopupWindow.isShowing()) {
            mFolderPopupWindow.dismiss();
        }
        monitorGridViewSizeChange();
        super.onConfigurationChanged(newConfig);
    }

    /**
     * 监控 GridView 的变化改变相册文件夹的尺寸
     */
    private void monitorGridViewSizeChange() {
        mGridView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int width = mGridView.getWidth();
                int height = mGridView.getHeight();
                mGridViewWidth = width;
                mGridViewHeight = height;
                setFolderPopWindowSize(width, height);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    mGridView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    mGridView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
            }
        });
    }

    /**
     * 选择相机
     */
    private void showCameraAction() {
        // 跳转到系统照相机
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // 设置系统相机拍照后的输出路径
            // 创建临时文件
            mTmpFile = CommonUtils.createTmpFile(getActivity());
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mTmpFile));
            startActivityForResult(cameraIntent, REQUEST_CAMERA);
        } else {
            CommonUtils.showToast(R.string.msg_no_camera);
        }
    }

    /**
     * 选择图片操作
     */
    private void selectImageFromGrid(ImageFile image, int mode) {
        if (image == null) {
            return;
        }
        switch (mode) {
            case Mode.MODE_MULTI:
                if (resultList.contains(image.path)) {
                    resultList.remove(image.path);
                    if (mCallback != null) {
                        mCallback.onImageUnselected(image.path);
                    }
                } else {
                    // 判断选择数量问题
                    if (maxSelectCount == resultList.size()) {
                        CommonUtils.showToast(R.string.msg_amount_limit);
                        return;
                    }
                    resultList.add(image.path);
                    if (mCallback != null) {
                        mCallback.onImageSelected(image.path);
                    }
                }
                setPreViewStatus();
                mImageAdapter.select(image);
                break;
            case Mode.MODE_SINGLE:
                if (mCallback != null) {
                    mCallback.onSingleImageSelected(image.path);
                }
                break;
            default:
                break;
        }
    }

    /**
     * 设置预览按钮的状态
     *
     * 注:暂时没这个功能 (16.04.07)
     */
    private void setPreViewStatus() {
        if (resultList != null && resultList.size() != 0) {
            mPreviewBtn.setEnabled(true);
            mPreviewBtn.setText(
                String.format(getResources().getString(R.string.image_preview_format), resultList.size()));
        } else {
            mPreviewBtn.setEnabled(false);
            mPreviewBtn.setText(R.string.preview);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
        @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case Constants.WRITE_EXTERNAL_STORAGE: {
                if (grantResults.length <= 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    CommonUtils.showToast(getString(R.string.rejected_hint));
                } else {
                    getActivity().getSupportLoaderManager().initLoader(LOADER_ALL, null, mLoaderCallback);
                }
            }
        }
    }

    /**
     * 回调接口
     */
    public interface Callback {
        void onSingleImageSelected(String path);

        void onImageSelected(String path);

        void onImageUnselected(String path);

        void onCameraShot(File imageFile);
    }
}

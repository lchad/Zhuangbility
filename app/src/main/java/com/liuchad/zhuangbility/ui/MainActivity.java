package com.liuchad.zhuangbility.ui;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.ColorInt;
import android.support.annotation.IntDef;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.SeekBar;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.liuchad.zhuangbility.Constants;
import com.liuchad.zhuangbility.Mode;
import com.liuchad.zhuangbility.R;
import com.liuchad.zhuangbility.base.BaseActivity;
import com.liuchad.zhuangbility.event.MultiPicSelectedEvent;
import com.liuchad.zhuangbility.event.SelectPicEvent;
import com.liuchad.zhuangbility.event.SelectRemotePicEvent;
import com.liuchad.zhuangbility.event.SinglePicSelectedEvent;
import com.liuchad.zhuangbility.util.CommonUtils;
import com.liuchad.zhuangbility.util.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import butterknife.BindView;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import in.workarounds.bundler.Bundler;
import in.workarounds.bundler.annotations.RequireBundler;
import me.priyesh.chroma.ChromaDialog;
import me.priyesh.chroma.ColorMode;
import me.priyesh.chroma.ColorSelectListener;

@RequireBundler
public class MainActivity extends BaseActivity
        implements SeekBar.OnSeekBarChangeListener, CompoundButton.OnCheckedChangeListener, View.OnClickListener {
    // TODO: 2017/3/21 研究花熊的字体
    // TODO: 2017/3/21 恢复从素材选图 (走网络)
    // TODO: 2017/3/21 研究如何实时改变Bitmap的失真度
    // TODO: 2017/3/21 设置颜色主题,优先度低
    // TODO: 2017/3/21 将MainActivity改造成MVP模式
    // TODO: 2017/3/21 引入Dagger2
    // TODO: 2017/3/21 如何实现gif编辑效果

    public final int REQUEST_SELECT_PIC = 101;
    private static final int REQUEST_CODE_CAMERA_CROP = 103;

    private static final int[] defaultFontColors = new int[]{
            Color.parseColor("#333333"),    /*默认字体颜色*/
            Color.BLACK, Color.WHITE, Color.GRAY,
    };

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.zhuangbi) ImageView mEmoji;
    @BindView(R.id.emoji_slogan) EditText mEmojiInputContent;
    @BindView(R.id.text_size_progress) SeekBar mTextSizeProgress;
    @BindView(R.id.text_mask_progress) SeekBar mTextMaskProgress;
    @BindView(R.id.text_direction_bottom) RadioButton mTextDirectionBottom;
    @BindView(R.id.text_direction_up) RadioButton mTextDirectionUp;
    @BindView(R.id.text_inside_pic) RadioButton mTextInsidePic;
    @BindView(R.id.text_beyond_pic) RadioButton mTextBeyondPic;
    @BindView(R.id.higher_quality) RadioButton mHigherQuality;
    @BindView(R.id.lower_quality) RadioButton mLowerQuality;
    @BindView(R.id.pure_text) RadioButton mPureText;
    @BindView(R.id.bold) CheckBox mBold;
    @BindView(R.id.italic) CheckBox mItalic;
    @BindView(R.id.black) RadioButton mBlack;
    @BindView(R.id.white) RadioButton mWhite;
    @BindView(R.id.gray) RadioButton mGray;
    @BindView(R.id.default_color) RadioButton mDefaultColor;
    @BindView(R.id.color_picker) ImageView mColorPicker;
    @BindView(R.id.text_color_rg) RadioGroup mTextColorRg;
    @BindView(R.id.tips_quality) Button mTipsQuality;
    @BindView(R.id.scroll_view) ScrollView mScrollView;
    @BindView(R.id.multiple_actions) FloatingActionsMenu mFloatingActionsMenu;
    @BindView(R.id.from_gallery) FloatingActionButton mFromGallery;
    @BindView(R.id.from_internet) FloatingActionButton mFromInternet;
    @BindView(R.id.from_sample) FloatingActionButton mFromSample;

    /**
     * 要增加的文字
     */
    String mEmojiText = "";

    private Bitmap mBitmapFromFile;

    /**
     * 原图的Bitmap
     */
    @SuppressWarnings("FieldCanBeLocal") private Bitmap mOriginalEmoji;

    /**
     * 修改之后的图片的Bitmap
     */
    private Bitmap mComposedEmoji;

    private boolean mIsTextInside = false;
    private boolean mIsTextBottom = true;

    /**
     * 默认的文字晕影值
     */
    private int mMaskValue = 3;

    private int mTextSize = 40;

    /**
     * 选择颜色值的下标
     */
    private int mTextPaintColorIndex = 0;

    /**
     * 颜色选择器设置的颜色值
     */
    private int mColorPicked = -1;

    private int mFontStyle = 0;
    private TextPaint mTextPaint;
    private Paint mRectPaint;
    private Canvas mCanvas;

    /**
     * 默认的素材图片资源id
     */
    private int mDefaultEmojiId = R.drawable.kt2;

    /**
     * (高清/祖传)模式标志位
     */
    private int mPicMode = QuantityMode.HIGH;

    private int screenWidth;

    private OnekeyShare mOnekeyShare;

    private String[] modeStringArray = {Constants.GAOQING, Constants.ZUCHUAN, Constants.PURE_TEXT};

    private TextWatcher mContentTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            mEmojiText = s.toString();
            doInvalidateCanvas();
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initInjector() {
        Bundler.inject(this);
        ShareSDK.initSDK(MainActivity.this, Constants.MOB_KEY);
    }

    private void oneKeyShare(String imagePath) {
        if (mOnekeyShare == null) {
            mOnekeyShare = new OnekeyShare();
        }
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        mOnekeyShare.setImagePath(imagePath);//确保SDcard下面存在此张图片

        // 启动分享GUI
        mOnekeyShare.show(this);
    }

    @Override
    protected void initView() {
        toolbar.setBackgroundColor(getResources().getColor(R.color.theme_light));
        setSupportActionBar(toolbar);

        mTextPaint = new TextPaint();
        mTextPaint.setAntiAlias(true);
        mRectPaint = new Paint();
        mCanvas = new Canvas();
        mTextSizeProgress.setMax((int) (getResources().getDisplayMetrics().density * 50));
        mTextSizeProgress.setOnSeekBarChangeListener(this);
        mTextMaskProgress.setOnSeekBarChangeListener(this);
        mEmojiInputContent.addTextChangedListener(mContentTextWatcher);
        mDefaultColor.setOnCheckedChangeListener(this);
        mBlack.setOnCheckedChangeListener(this);
        mWhite.setOnCheckedChangeListener(this);
        mGray.setOnCheckedChangeListener(this);
        mBold.setOnCheckedChangeListener(this);
        mItalic.setOnCheckedChangeListener(this);
        mTextInsidePic.setOnCheckedChangeListener(this);
        mTextBeyondPic.setOnCheckedChangeListener(this);
        mTextDirectionBottom.setOnCheckedChangeListener(this);
        mTextDirectionUp.setOnCheckedChangeListener(this);
        mColorPicker.setOnClickListener(this);
        mTipsQuality.setOnClickListener(this);
        mHigherQuality.setOnCheckedChangeListener(this);
        mLowerQuality.setOnCheckedChangeListener(this);
        mPureText.setOnCheckedChangeListener(this);
        mFromGallery.setOnClickListener(this);
        mFromInternet.setOnClickListener(this);
        mEmoji.setOnClickListener(this);
        mFromSample.setOnClickListener(this);
//        mFromInternet.performClick();
    }

    @Override
    protected void initData() {
        EventBus.getDefault().register(this);
        screenWidth = CommonUtils.getScreenWidth(MainActivity.this);
    }

    @Override
    public void reportFullyDrawn() {
        super.reportFullyDrawn();
    }

    private void doInvalidateCanvas() { /*输入文字的总宽度*/
        float textTotalWidth = mTextPaint.measureText(mEmojiText);
        if (mBitmapFromFile == null) {
            mOriginalEmoji = BitmapFactory.decodeResource(getResources(), mDefaultEmojiId);
        } else {
            mOriginalEmoji = mBitmapFromFile;
        }
        //输入文字的总高度(包括换行)
        int extraTextAreaHeight = ((int) Math.ceil(textTotalWidth / mOriginalEmoji.getWidth())) * (int) ((mTextSize) * 1.2);

        Typeface font = Typeface.create(Typeface.SANS_SERIF, mFontStyle);

        mTextPaint.setTypeface(font);
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setColor(mColorPicked == -1 ? defaultFontColors[mTextPaintColorIndex] : mColorPicked);
        mTextPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mTextPaint.setMaskFilter(new BlurMaskFilter(mMaskValue, BlurMaskFilter.Blur.SOLID));

        mRectPaint.setColor(Color.WHITE);
        mRectPaint.setAntiAlias(true);
        mRectPaint.setStyle(Paint.Style.FILL);

        mComposedEmoji = Bitmap.createBitmap(
                mOriginalEmoji.getWidth(),
                mIsTextInside ? mOriginalEmoji.getHeight() : mOriginalEmoji.getHeight() + extraTextAreaHeight,
                Bitmap.Config.ARGB_8888);

        resizeImageView(mComposedEmoji.getWidth(), mComposedEmoji.getHeight());

        mCanvas.setBitmap(mComposedEmoji);

        //通过StaticLayout来达到换行的效果.
        StaticLayout staticLayout = new StaticLayout(mEmojiText, mTextPaint, mOriginalEmoji.getWidth(),
                Layout.Alignment.ALIGN_CENTER, 1, 0, false);
        mCanvas.save();
        mTextPaint.setTextAlign(Paint.Align.LEFT);

        if (!mIsTextInside) {
            if (mIsTextBottom) {
                mCanvas.drawBitmap(mOriginalEmoji, 0f, 0f, null);
                mCanvas.drawRect(0, mOriginalEmoji.getHeight(), mOriginalEmoji.getWidth(),
                        mOriginalEmoji.getHeight() + extraTextAreaHeight,
                        mRectPaint);
                mCanvas.translate(0, mOriginalEmoji.getHeight());
            } else {
                mCanvas.drawBitmap(mOriginalEmoji, 0f, extraTextAreaHeight, null);
                mCanvas.drawRect(0, 0, mOriginalEmoji.getWidth(), extraTextAreaHeight, mRectPaint);
            }
        } else {
            mCanvas.drawBitmap(mOriginalEmoji, 0f, 0f, null);
            if (mIsTextBottom) {
                mCanvas.translate(0, mOriginalEmoji.getHeight() - extraTextAreaHeight);
            }
        }

        staticLayout.draw(mCanvas);
        mCanvas.restore();
        mEmoji.setImageBitmap(mComposedEmoji);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_SELECT_PIC:

                break;
            case REQUEST_CODE_CAMERA_CROP: //裁剪
                if (resultCode != RESULT_OK) {
                    return;
                }
                String cameraPicPath = CommonUtils.SDCARD + "/" + "temp.jpg";
                cropImage(cameraPicPath);
                break;
        }
    }

    @Subscribe
    public void onSinglePicSelectedEvent(SinglePicSelectedEvent event) {
        if (TextUtils.isEmpty(event.path)) {
            return;
        }

        Uri uri = Uri.fromFile(new File(event.path));
        CommonUtils.startPhotoZoom(MainActivity.this, uri, REQUEST_CODE_CAMERA_CROP);
    }

    @Subscribe
    public void onMultiPicSelectedEvent(MultiPicSelectedEvent event) {

    }

    private void cropImage(String cameraPicPath) {
        mBitmapFromFile = BitmapFactory.decodeFile(cameraPicPath);
        if (mBitmapFromFile.getWidth() > screenWidth) {
            double rate = (double) screenWidth / (double) mBitmapFromFile.getWidth();
            int newWidth = (int) (rate * mBitmapFromFile.getWidth());
            int newHeight = (int) (rate * mBitmapFromFile.getHeight());
            mBitmapFromFile.recycle();
            mBitmapFromFile = CommonUtils.decodeSampledBitmapFromFile(cameraPicPath, newWidth, newHeight);
        }

        resizeImageView(mBitmapFromFile.getWidth(), mBitmapFromFile.getHeight());
        doInvalidateCanvas();
        boolean deleted = new File(cameraPicPath).delete();//删除拍照的图片
        if (!deleted) {
            Log.e("文件删除失败: ", "cameraPicPath " + cameraPicPath);
        }
    }

    public void resizeImageView(int width, int height) {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mEmoji.getLayoutParams();
        params.width = width;
        params.height = height;
        mEmoji.setLayoutParams(params);
    }

    public void refreshColorRadioButtonsCheck(int index) {
        mTextPaintColorIndex = index;
        doInvalidateCanvas();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (seekBar.getId()) {
            case R.id.text_size_progress:
                mTextSize = progress;
                if (!TextUtils.isEmpty(mEmojiText)) {
                    doInvalidateCanvas();
                }
                break;
            case R.id.text_mask_progress:
                mMaskValue = progress;
                if (mMaskValue <= 0) {
                    mMaskValue = 1;
                }
                if (!TextUtils.isEmpty(mEmojiText)) {
                    doInvalidateCanvas();
                }
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.default_color:
                if (isChecked) {
                    refreshColorRadioButtonsCheck(0);
                }
                mColorPicked = -1;
                break;

            case R.id.black:
                if (isChecked) {
                    refreshColorRadioButtonsCheck(1);
                }
                mColorPicked = -1;
                break;

            case R.id.white:
                if (isChecked) {
                    refreshColorRadioButtonsCheck(2);
                }
                mColorPicked = -1;
                break;

            case R.id.gray:
                if (isChecked) {
                    refreshColorRadioButtonsCheck(3);
                }
                mColorPicked = -1;
                break;

            case R.id.text_direction_bottom:
                if (isChecked) {
                    mIsTextBottom = true;
                    doInvalidateCanvas();
                }
                break;

            case R.id.text_direction_up:
                if (isChecked) {
                    mIsTextBottom = false;
                    doInvalidateCanvas();
                }
                break;

            case R.id.text_inside_pic:
                if (isChecked) {
                    mIsTextInside = true;
                    doInvalidateCanvas();
                }
                break;

            case R.id.text_beyond_pic:
                if (isChecked) {
                    mIsTextInside = false;
                    doInvalidateCanvas();
                }
                break;
            case R.id.higher_quality:
                if (isChecked) {
                    mPicMode = QuantityMode.HIGH;
                }
                break;

            case R.id.lower_quality:
                if (isChecked) {
                    mPicMode = QuantityMode.LOW;
                }
                break;
            case R.id.pure_text:
                if (isChecked) {
                    mPicMode = QuantityMode.PURE_TEXT;
                }
                break;

            case R.id.bold:
                if (isChecked) {
                    if (mItalic.isChecked()) {
                        mFontStyle = Typeface.BOLD_ITALIC;
                        doInvalidateCanvas();
                    } else {
                        mFontStyle = Typeface.BOLD;
                        doInvalidateCanvas();
                    }
                } else {
                    if (mItalic.isChecked()) {
                        mFontStyle = Typeface.ITALIC;
                        doInvalidateCanvas();
                    } else {
                        mFontStyle = Typeface.NORMAL;
                        doInvalidateCanvas();
                    }
                }
                break;

            case R.id.italic:
                mFontStyle = Typeface.ITALIC;
                if (isChecked) {
                    if (mBold.isChecked()) {
                        mFontStyle = Typeface.BOLD_ITALIC;
                        doInvalidateCanvas();
                    } else {
                        mFontStyle = Typeface.ITALIC;
                        doInvalidateCanvas();
                    }
                } else {
                    if (mBold.isChecked()) {
                        mFontStyle = Typeface.BOLD;
                        doInvalidateCanvas();
                    } else {
                        mFontStyle = Typeface.NORMAL;
                        doInvalidateCanvas();
                    }
                }
                break;
            default:
                break;
        }
    }

    /**
     * 选择内置图片之后的回调
     *
     * @param event EventBus事件
     */
    @Subscribe
    public void onEvent(SelectPicEvent event) {
        mDefaultEmojiId = event.id;
        mBitmapFromFile = null;
        doInvalidateCanvas();
    }

    /**
     * 从服务端选择的图片,会回调图片的url.
     * @param event 事件.
     */
    @Subscribe
    public void onEvent(SelectRemotePicEvent event) {
        ToastUtils.showToast(event.url);
    }

    @SuppressLint("InflateParams")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.color_picker:
                //noinspection deprecation
                new ChromaDialog.Builder()
                        .initialColor(getResources().getColor(R.color.theme_light))
                        .colorMode(ColorMode.ARGB)
                        .onColorSelected(new ColorSelectListener() {
                            @Override
                            public void onColorSelected(@ColorInt int i) {
                                mColorPicked = i;
                                doInvalidateCanvas();
                                mTextColorRg.clearCheck();
                            }
                        })
                        .create()
                        .show(getSupportFragmentManager(), Constants.ZHUANGBILITY);
                break;
            case R.id.tips_quality:
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle(getString(R.string.quality_title))
                        .setView(getLayoutInflater().inflate(R.layout.dialog_quality, null))
                        .show();
                break;
            case R.id.from_gallery:
                Bundler.multiImageSelectorActivity(Mode.MODE_SINGLE, true).start(MainActivity.this);
                mFloatingActionsMenu.collapse();
                break;
            case R.id.from_internet:
                Bundler.elementaryActivity().start(MainActivity.this);
                mFloatingActionsMenu.collapse();
                break;
            case R.id.from_sample:
                Bundler.selectPicActivity().start(MainActivity.this);
                mFloatingActionsMenu.collapse();
                break;

            case R.id.zhuangbi:

                break;
        }
    }

    private String getFormatFileName(int mode) {
        return getString(R.string.app_name_english)
                + "-"
                + modeStringArray[mode]
                + "-"
                + System.currentTimeMillis()
                + ".jpeg";
    }

    public void saveNewEmojiToSdCard(String filename, Bitmap bitmap) {
        if (bitmap == null) {
            return;
        }

        String snackText = getString(R.string.filename) + filename;
        FileOutputStream out = null;
        File dest = null;
        File zhuangbiDir = new File(
                Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + Constants.ZHUANGBILITY);
        boolean success = true;
        if (!zhuangbiDir.exists()) {
            success = zhuangbiDir.mkdir();
        }

        if (success) {
            dest = new File(zhuangbiDir, filename);
        }
        if (dest == null) {
            CommonUtils.showToast(getString(R.string.make_fail));
            return;
        }
        try {
            out = new FileOutputStream(dest.getAbsolutePath());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            CommonUtils.showToast(getString(R.string.file_not_found));
        }
        bitmap.compress(Bitmap.CompressFormat.JPEG, mPicMode != QuantityMode.LOW ? 100 : 0, out);
        Snackbar.make(mScrollView, snackText, Snackbar.LENGTH_LONG)
                .setAction("Action", null)
                .show();
        CommonUtils.refreshLocalDb(MainActivity.this, dest);

        if (!dest.exists()) {
            return;
        }
        Bitmap reusedBitmap = BitmapFactory.decodeFile(dest.getAbsolutePath());
        if (reusedBitmap != null) {
            mEmoji.setImageBitmap(null);
            mEmoji.setImageBitmap(reusedBitmap);
        }
        try {
            if (out != null) {
                out.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @IntDef({QuantityMode.HIGH, QuantityMode.LOW, QuantityMode.PURE_TEXT})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface QuantityMode {
        int HIGH = 0;
        int LOW = 1;
        int PURE_TEXT = 2;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_theme:
                Bundler.themeActivity().start(MainActivity.this);
                break;
            case R.id.action_save:
                if (mComposedEmoji == null) {
                    break;
                }
                String filename = getFormatFileName(mPicMode);
                saveNewEmojiToSdCard(filename, mComposedEmoji);
                break;
            case R.id.action_share:
                if (mComposedEmoji == null) {
                    break;
                }
                String sharedFilename = getFormatFileName(mPicMode);
                saveNewEmojiToSdCard(sharedFilename, mComposedEmoji);
                oneKeyShare(Environment.getExternalStorageDirectory().getAbsolutePath()
                        + File.separator + Constants.ZHUANGBILITY + File.separator + sharedFilename);
                break;
            case R.id.action_about:
                Bundler.aboutActivity().start(MainActivity.this);
                break;
            case R.id.action_donate:
                Bundler.donateActivity().start(MainActivity.this);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(false);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}

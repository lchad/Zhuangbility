package com.liuchad.zhuangbility.base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import butterknife.ButterKnife;
import com.liuchad.zhuangbility.R;

/**
 * Created by lchad on 2016/10/15.
 */

public abstract class BaseActivity extends AppCompatActivity {

    private ProgressDialog mProgressDialog;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (setFullScreen()) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(getLayoutId());
        Thread.setDefaultUncaughtExceptionHandler(App.sUncaughtExceptionHandler);
        initInjector();
        ButterKnife.bind(this);
        initView();
        initData();
    }

    /**
     * Get the layout Id.
     */
    protected abstract int getLayoutId();

    /**
     * Init the inject.
     */
    protected abstract void initInjector();

    /**
     * Init the listener.
     */
    protected abstract void initView();

    /**
     * Init the data.
     */
    protected abstract void initData();

    /**
     * config the status bar or not.
     */
    protected boolean setStatus() {
        return true;
    }

    /**
     * make activity full screen or not.
     */
    protected boolean setFullScreen() {
        return false;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (setStatus()) {
            setStatusBar();
        }
    }

    /**
     * 设置状态栏颜色
     */
    protected void setStatusBar() {
        //StatusBarCompat.setStatusBarColor(this, getResources().getColor(R.color.primary));
    }

    public void showLoadingDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setCancelable(false);
            mProgressDialog.setCanceledOnTouchOutside(false);
        }
        mProgressDialog.show();
    }

    public void hideLoadingDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    protected void showProgressDialog() {
        showProgressDialog(getString(R.string.loading));
    }

    protected void showProgressDialog(String info) {
        showProgressDialog(info, false);
    }

    protected void showProgressDialog(int id) {
        showProgressDialog(getResources().getString(id), true);
    }

    protected void showProgressDialog(String info, boolean cancelable) {
        showProgressDialog("", info, cancelable);
    }

    protected void showProgressDialog(String title, String info,
        boolean cancelable) {
        if (!isFinishing()) {
            if (mProgressDialog == null) {
                mProgressDialog = ProgressDialog.show(this, title, info, true, cancelable, new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        mProgressDialog.dismiss();
                    }
                });
            } else {
                if (!mProgressDialog.isShowing()) {
                    mProgressDialog.show();
                }
            }
        }
    }

    protected void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }

    protected void hideKeyBoard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    protected void showSoftKeyBoard(Activity activity, View view) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.RESULT_SHOWN);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }
}

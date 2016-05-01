package com.liuchad.zhuangbility.widget;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.liuchad.zhuangbidemo.R;

/**
 * Created by liuchad on 2016/4/26.
 */
public class IconView extends LinearLayout {

    private Context mContext;
    private ImageView ivIcon;
    private TextView tvTitle;
    private int iconResId;
    private int iconPressResId;

    private static final int STATE_NORMAL = 0;
    private static final int STATE_SCALING_TO_BIG = 1;
    private static final int STATE_SCALING_TO_NORMAL = 2;
    private static final int STATE_SCALED = 3;

    private int mState = STATE_NORMAL;
    private boolean isClick;

    public static final int MODE_ANIMA = 0x01;
    public static final int MODE_ALPHA = 0x02;
    private int mMode = MODE_ALPHA;

    private IconClickListener mListener;

    public IconView(Context context) {
        super(context);
        mContext = context;
        initView();
    }

    public IconView(Context context, int resId, String text) {
        this(context);
        ivIcon.setImageResource(resId);
        tvTitle.setText(text);
    }

    public IconView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView();
        TypedArray ta = mContext.obtainStyledAttributes(attrs, R.styleable.IconView);
        iconResId = ta.getResourceId(R.styleable.IconView_img, R.drawable.sz_ic_pyq);
        iconPressResId = ta.getResourceId(R.styleable.IconView_img_press, R.drawable.sz_ic_pyq_press);
        tvTitle.setText(ta.getString(R.styleable.IconView_text));
        tvTitle.setTextSize(ta.getInt(R.styleable.IconView_titleTextSize, 12));
        tvTitle.setTextColor(Color.parseColor("#8b868d"));
        ivIcon.setImageResource(iconResId);
        ta.recycle();
    }

    private void initView() {
        LayoutInflater.from(mContext).inflate(R.layout.icon_layout, this);
        ivIcon = (ImageView) findViewById(R.id.ivIcon);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        this.setAnimationCacheEnabled(false);
    }

    public void setIconClickListener(IconClickListener listener) {
        this.mListener = listener;
    }

    /**
     * 设置图标
     *
     * @param resId
     */
    public void setIconRes(int resId) {
        ivIcon.setImageResource(resId);
    }

    /**
     * 设置图标点击背景
     *
     * @param resId
     */
    public void setIconPressResId(int resId) {
        iconPressResId = resId;
    }

    /**
     * 设置标题
     *
     * @param text
     */
    public void setTvTitle(String text) {
        tvTitle.setText(text);
    }

    /**
     * 设置点击响应模式 : 动画/ 背景alpha变换
     *
     * @param mode
     */
    public void setActionMode(int mode) {
        if (mode != MODE_ANIMA && mode != MODE_ALPHA) return;
        this.mMode = mode;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        boolean isFocus;
        isFocus = contains(x, y);
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (isFocus) {
                isClick = true;
                isFocus = false;
            }
        }
        if(mMode == MODE_ANIMA){
            if (isFocus && mState == STATE_NORMAL) {
                scaleToBig(ivIcon);
                scaleToBig(tvTitle);
            } else if (!isFocus && mState == STATE_SCALED) {
                scaleToNormal(ivIcon);
                scaleToNormal(tvTitle);
            }
        }else {
            if (isFocus) {
                ivIcon.setImageResource(iconPressResId);
            } else {
                ivIcon.setImageResource(iconResId);
            }
        }
        if (isClick && mListener != null) {
            isClick = false;
            mListener.onIconClick(this);
        }
        return true;
    }

    private void scaleToBig(View view) {
        mState = STATE_SCALING_TO_BIG;
        ObjectAnimator animX = ObjectAnimator.ofFloat(view, "scaleX", 1.0f, 1.1f);
        ObjectAnimator animY = ObjectAnimator.ofFloat(view, "scaleY", 1.0f, 1.1f);

        animX.addListener(mAnimatorListener);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(200);
        animatorSet.setInterpolator(new LinearInterpolator());
        animatorSet.playTogether(animX, animY);
        animatorSet.start();
    }

    private void scaleToNormal(View view) {
        mState = STATE_SCALING_TO_NORMAL;

        ObjectAnimator animX = ObjectAnimator.ofFloat(view, "scaleX", 1.0f, 1 / 1.1f);
        ObjectAnimator animY = ObjectAnimator.ofFloat(view, "scaleY", 1.0f, 1 / 1.1f);
        animX.addListener(mAnimatorListener);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(200);
        animatorSet.setInterpolator(new LinearInterpolator());
        animatorSet.playTogether(animX, animY);
        animatorSet.start();
    }

    private Animator.AnimatorListener mAnimatorListener = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animation) {
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            if (mState == STATE_SCALING_TO_BIG) {
                mState = STATE_SCALED;
            } else if (mState == STATE_SCALING_TO_NORMAL) {
                mState = STATE_NORMAL;
            }
        }

        @Override
        public void onAnimationCancel(Animator animation) {
        }

        @Override
        public void onAnimationRepeat(Animator animation) {
        }
    };

    private boolean contains(float x, float y) {
        return x >= ivIcon.getLeft() && x <= ivIcon.getRight() && y >= ivIcon.getTop() && y <= ivIcon.getBottom();
    }

    public interface IconClickListener {

        void onIconClick(IconView view);
    }

}

package com.vicedev.vloadingviewslib.views;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import androidx.annotation.Nullable;

import com.vicedev.vloadingviewslib.R;
import com.vicedev.vloadingviewslib.utils.DensityUtil;
import com.vicedev.vloadingviewslib.utils.ViewUtils;

/**
 * @author vicedev
 * @date 2020/2/3 18:50
 */
public class VLoadingView0 extends View {
    private ObjectAnimator mObjectAnimatorX = new ObjectAnimator();
    private ObjectAnimator mObjectAnimatorY = new ObjectAnimator();
    private AnimatorSet mAnimatorSet;
    /**
     * the color of rectangle
     */
    private int mRectColor = Color.WHITE;

    /**
     * the default size of rectangle
     */
    private int mDefaultSize;

    /**
     * total time(millisecond)
     */
    private int mTotalDuration = 1200;


    public VLoadingView0(Context context) {
        this(context, null);
    }

    public VLoadingView0(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VLoadingView0(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mDefaultSize = DensityUtil.dp2px(50);

        if (context != null && attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.VLoadingView0);
            mRectColor = typedArray.getColor(R.styleable.VLoadingView0_rect_color, Color.WHITE);
            mTotalDuration = typedArray.getInt(R.styleable.VLoadingView0_total_duration, mTotalDuration);
            typedArray.recycle();
        }

        setBackgroundColor(mRectColor);

        mObjectAnimatorX.setInterpolator(new LinearInterpolator());
        mObjectAnimatorX.setDuration((long) (mTotalDuration / 2.0f));
        mObjectAnimatorX.setTarget(this);
        mObjectAnimatorX.setFloatValues(0.0f, -180.0f);
        mObjectAnimatorX.setProperty(View.ROTATION_X);

        mObjectAnimatorY.setInterpolator(new LinearInterpolator());
        mObjectAnimatorY.setDuration((long) (mTotalDuration / 2.0f));
        mObjectAnimatorY.setTarget(this);
        mObjectAnimatorY.setFloatValues(0.0f, -180.0f);
        mObjectAnimatorY.setProperty(View.ROTATION_Y);

        mAnimatorSet = new AnimatorSet();
        mAnimatorSet.playSequentially(mObjectAnimatorX, mObjectAnimatorY);
        mAnimatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mAnimatorSet.start();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int value = ViewUtils.measureMinSize(this, widthMeasureSpec, heightMeasureSpec, mDefaultSize);
        setMeasuredDimension(value, value);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mAnimatorSet.start();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mAnimatorSet.cancel();
    }
}

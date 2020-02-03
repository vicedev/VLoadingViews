package com.vicedev.vloadingviewslib.views;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import androidx.annotation.Nullable;

import com.vicedev.vloadingviewslib.R;
import com.vicedev.vloadingviewslib.utils.DensityUtil;

/**
 * @author vicedev
 * @date 2020/2/3 18:50
 */
public class VLoadingView1 extends View {
    private Rect mRect = new Rect();
    private ObjectAnimator mObjectAnimatorX = new ObjectAnimator();
    private ObjectAnimator mObjectAnimatorY = new ObjectAnimator();
    private AnimatorSet mAnimatorSet;
    /**
     * 矩形的颜色
     */
    private int mRectangleColor = Color.WHITE;

    /**
     * 矩形的宽度
     */
    private int mRectangleWidth;

    /**
     * 一次动画的总时间（毫秒）
     */
    private int mTotalDuration = 1200;


    public VLoadingView1(Context context) {
        this(context, null);
    }

    public VLoadingView1(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VLoadingView1(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mRectangleWidth = DensityUtil.dp2px(50);

        if (context != null && attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.VLoadingView1);
            mRectangleColor = typedArray.getColor(R.styleable.VLoadingView1_rectangle_color, Color.WHITE);
            mRectangleWidth = (int) typedArray.getDimension(R.styleable.VLoadingView1_rectangle_width, mRectangleWidth);
            mTotalDuration = typedArray.getInt(R.styleable.VLoadingView1_total_duration, mTotalDuration);
            typedArray.recycle();
        }

        setBackgroundColor(mRectangleColor);

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
        int wMode = MeasureSpec.getMode(widthMeasureSpec);
        int hMode = MeasureSpec.getMode(heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        if (wMode == MeasureSpec.AT_MOST || wMode == MeasureSpec.UNSPECIFIED) {
            width = mRectangleWidth;
        }
        if (hMode == MeasureSpec.AT_MOST || hMode == MeasureSpec.UNSPECIFIED) {
            height = mRectangleWidth;
        }
        int value = Math.min(width, height);
        setMeasuredDimension(value, value);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        int rectangleWidth = Math.min(w, h);
        mRect.left = (int) ((w - rectangleWidth) / 2.0);
        mRect.top = (int) ((h - rectangleWidth) / 2.0);
        mRect.right = w - mRect.left;
        mRect.bottom = h - mRect.top;
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

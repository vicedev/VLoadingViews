package com.vicedev.vloadingviewslib.views;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import androidx.annotation.Nullable;

import com.vicedev.vloadingviewslib.R;
import com.vicedev.vloadingviewslib.utils.DensityUtil;
import com.vicedev.vloadingviewslib.utils.ViewUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author vicedev
 * @date 2020/2/4 9:16
 */
public class VLoadingView1 extends View {

    private List<Dot> mDotList = new ArrayList<>();
    private long mDuration = 2000;
    private float mMaximumCircleRadius;
    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private int mDefaultSize;
    private int mCircleColor = Color.WHITE;

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
        mMaximumCircleRadius = DensityUtil.dp2px(5);
        mDefaultSize = DensityUtil.dp2px(50);

        if (context != null && attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.VLoadingView1);
            mMaximumCircleRadius = typedArray.getDimension(R.styleable.VLoadingView1_maximum_circle_radius, mMaximumCircleRadius);
            mCircleColor = typedArray.getColor(R.styleable.VLoadingView1_circle_color, mCircleColor);
            typedArray.recycle();
        }

        mPaint.setColor(mCircleColor);
        mPaint.setStyle(Paint.Style.FILL);

        mDotList.clear();
        for (int i = 0; i < 6; i++) {
            Dot dot;
            int time = 500 - i * 100;
            if (i == 0) {
                dot = new Dot(mMaximumCircleRadius, time);
            } else {
                dot = new Dot((float) (mDotList.get(i - 1).mRadius * Math.sqrt(0.8f)), time);
            }
            mDotList.add(dot);
        }


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int value = ViewUtils.measureMinSize(this, widthMeasureSpec, heightMeasureSpec, mDefaultSize);
        setMeasuredDimension(value, value);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < mDotList.size(); i++) {
            Dot dot = mDotList.get(i);
            canvas.drawCircle(dot.mCenterX, dot.mCenterY, dot.mRadius, mPaint);
        }
        postInvalidateDelayed(10);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        for (int i = 0; i < mDotList.size(); i++) {
            ValueAnimator valueAnimator = mDotList.get(i).mValueAnimator;
            valueAnimator.cancel();
            valueAnimator.start();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        for (int i = 0; i < mDotList.size(); i++) {
            ValueAnimator valueAnimator = mDotList.get(i).mValueAnimator;
            valueAnimator.cancel();
        }
    }

    class Dot {
        float mRadius;
        float mCenterX = -1000;
        float mCenterY = -1000;
        float mStartProgress = 0.0f;
        private float endValue = 360.0f + 360.0f - (360.0f / 5.0f);
        ValueAnimator mValueAnimator = ValueAnimator.ofFloat(0.0f, endValue);
        private float mCurrentProgress;

        Dot(float radius, long delay) {
            mRadius = radius;

            mValueAnimator.setDuration(mDuration - delay);
            mValueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
            mValueAnimator.setStartDelay(delay);
            mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    mCurrentProgress = mStartProgress + (float) animation.getAnimatedValue();
                    mCenterX = (float) (getWidth() / 2.0f + (getWidth() / 2.0f - mMaximumCircleRadius) * Math.cos(mCurrentProgress * Math.PI / 180));
                    mCenterY = (float) (getHeight() / 2.0f + (getHeight() / 2.0f - mMaximumCircleRadius) * Math.sin(mCurrentProgress * Math.PI / 180));
                }
            });

            mValueAnimator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    mStartProgress = mCurrentProgress % 360.0f;
                    animation.start();
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });

        }
    }


}

package com.vicedev.vloadingviewslib.views;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import androidx.annotation.Nullable;

import com.vicedev.vloadingviewslib.R;
import com.vicedev.vloadingviewslib.utils.DensityUtil;
import com.vicedev.vloadingviewslib.utils.ViewUtils;

/**
 * @author vicedev
 * @date 2020/2/5 11:25
 */
public class VLoadingView6 extends View {

    private int mDefaultSize;
    private int mCircleColor = Color.WHITE;
    private Paint mPaint;
    private ObjectAnimator mObjectAnimator;
    private float mMaxCircleRadius;
    private PointF mCircleCenterPoint;
    private float mCurrentCircleRadius;

    public VLoadingView6(Context context) {
        this(context, null);
    }

    public VLoadingView6(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VLoadingView6(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {

        mDefaultSize = DensityUtil.dp2px(50);

        if (context != null && attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.VLoadingView6);
            mCircleColor = typedArray.getColor(R.styleable.VLoadingView6_circle_color, mCircleColor);
            typedArray.recycle();
        }


        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mCircleColor);

        mObjectAnimator = ObjectAnimator.ofFloat(this, View.ROTATION, 0.0f, 360.0f);
        mObjectAnimator.setDuration(2000);
        mObjectAnimator.setInterpolator(new LinearInterpolator());
        mObjectAnimator.setRepeatMode(ValueAnimator.RESTART);
        mObjectAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mObjectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float percent = animation.getAnimatedFraction();
                if (percent >= 0.0f && percent <= 0.5f) {
                    mCurrentCircleRadius = mMaxCircleRadius * percent * 2.0f;
                } else {
                    mCurrentCircleRadius = mMaxCircleRadius - mMaxCircleRadius * ((percent - 0.5f) * 2.0f);
                }
                invalidate();
            }
        });
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mMaxCircleRadius = Math.min(w, h) / 2.0f / 2.0f;
        mCircleCenterPoint = new PointF(mMaxCircleRadius, h / 2.0f - mMaxCircleRadius);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int viewSize = ViewUtils.measureMinSize(this, widthMeasureSpec, heightMeasureSpec, mDefaultSize);
        setMeasuredDimension(viewSize, viewSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(mCircleCenterPoint.x, mCircleCenterPoint.y, mCurrentCircleRadius, mPaint);

        canvas.drawCircle(mCircleCenterPoint.x, mCircleCenterPoint.y + mMaxCircleRadius * 2.0f, mMaxCircleRadius - mCurrentCircleRadius, mPaint);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mObjectAnimator.cancel();
        mObjectAnimator.start();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mObjectAnimator.cancel();
    }
}

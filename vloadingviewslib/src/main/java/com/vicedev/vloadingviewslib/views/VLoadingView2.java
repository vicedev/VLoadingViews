package com.vicedev.vloadingviewslib.views;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.vicedev.vloadingviewslib.R;
import com.vicedev.vloadingviewslib.utils.DensityUtil;
import com.vicedev.vloadingviewslib.utils.ViewUtils;

/**
 * @author vicedev
 * @date 2020/2/4 19:37
 */
public class VLoadingView2 extends View {

    private int mDefaultSize;
    private int mMaxCircleRadius;
    private int mCircleRadius1;
    private int mCircleRadius2;
    private int mCircleColor = Color.parseColor("#99ffffff");
    private Paint mPaint;

    private ObjectAnimator mObjectAnimator;

    public VLoadingView2(Context context) {
        this(context, null);
    }

    public VLoadingView2(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VLoadingView2(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mDefaultSize = DensityUtil.dp2px(50);

        if (context != null && attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.VLoadingView2);
            mCircleColor = typedArray.getColor(R.styleable.VLoadingView2_circle_color, mCircleColor);
            typedArray.recycle();
        }

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mCircleColor);

        mObjectAnimator = ObjectAnimator.ofInt(this, "radius", 0, (int) (mDefaultSize / 2.0f));
        mObjectAnimator.setDuration(1000);
        mObjectAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mObjectAnimator.setRepeatMode(ValueAnimator.REVERSE);
    }

    private void setRadius(int radius) {
        mCircleRadius1 = radius;
        mCircleRadius2 = mMaxCircleRadius - radius;
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int value = ViewUtils.measureMinSize(this, widthMeasureSpec, heightMeasureSpec, mDefaultSize);
        mMaxCircleRadius = (int) (value / 2.0f);
        setMeasuredDimension(value, value);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //draw out circle1
        canvas.drawCircle(getWidth() / 2.0f, getHeight() / 2.0f, mCircleRadius1, mPaint);

        //draw in circle2
        canvas.drawCircle(getWidth() / 2.0f, getHeight() / 2.0f, mCircleRadius2, mPaint);
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

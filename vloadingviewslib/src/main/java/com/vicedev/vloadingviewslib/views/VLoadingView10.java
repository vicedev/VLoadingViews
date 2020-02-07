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
import android.view.animation.LinearInterpolator;

import androidx.annotation.Nullable;

import com.vicedev.vloadingviewslib.R;
import com.vicedev.vloadingviewslib.utils.DensityUtil;
import com.vicedev.vloadingviewslib.utils.ViewUtils;

/**
 * @author vicedev
 * @date 2020/2/7 14:50
 */
public class VLoadingView10 extends View {

    private int mDefaultSize;
    private int mCircleColor = Color.WHITE;
    private Paint mPaint;
    private float mCircleRadius;
    private long mAnimDuration = 1200;
    private int mCircleCount = 12;
    private ObjectAnimator mObjectAnimator;
    private float mCurrentAngle;


    public VLoadingView10(Context context) {
        this(context, null);
    }

    public VLoadingView10(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VLoadingView10(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {

        mDefaultSize = DensityUtil.dp2px(50);
        mCircleRadius = DensityUtil.dp2px(4);

        if (context != null && attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.VLoadingView10);
            mCircleColor = typedArray.getColor(R.styleable.VLoadingView10_circle_color, mCircleColor);
            mCircleRadius = typedArray.getDimension(R.styleable.VLoadingView10_circle_radius, mCircleRadius);
            typedArray.recycle();
        }


        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mCircleColor);

        mObjectAnimator = ObjectAnimator.ofFloat(this, "angle", 0.0f, 360.0f);
        mObjectAnimator.setDuration(mAnimDuration);
        mObjectAnimator.setInterpolator(new LinearInterpolator());
        mObjectAnimator.setRepeatMode(ValueAnimator.RESTART);
        mObjectAnimator.setRepeatCount(ValueAnimator.INFINITE);

    }

    private void setAngle(float angle) {
        mCurrentAngle = (float) (Math.floor(angle / 30.0f) * 30.0f);
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int viewSize = ViewUtils.measureMinSize(this, widthMeasureSpec, heightMeasureSpec, mDefaultSize);
        setMeasuredDimension(viewSize, viewSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.save();
        canvas.rotate(mCurrentAngle, getWidth() / 2.0f, getHeight() / 2.0f);

        for (int i = 0; i < mCircleCount; i++) {
            canvas.save();
            int alpha = (int) (255 * (1.0f - (i * 2.0f) / mCircleCount));
            mPaint.setAlpha(alpha < 0 ? 0 : alpha);
            canvas.rotate(-30 * i, getWidth() / 2.0f, getHeight() / 2.0f);
            canvas.drawCircle(getWidth() / 2.0f, mCircleRadius, mCircleRadius, mPaint);
            canvas.restore();
        }

        canvas.restore();

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

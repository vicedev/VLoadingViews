package com.vicedev.vloadingviewslib.views;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateInterpolator;

import androidx.annotation.Nullable;

import com.vicedev.vloadingviewslib.R;
import com.vicedev.vloadingviewslib.utils.DensityUtil;
import com.vicedev.vloadingviewslib.utils.ViewUtils;

/**
 * @author vicedev
 * @date 2020/2/5 9:28
 */
public class VLoadingView5 extends View {

    private int mDefaultSize;
    private int mCircleColor = Color.WHITE;
    private Paint mPaint;
    private ValueAnimator mValueAnimator;
    private float mMaxRadius;
    private float mCurrentRadius;

    public VLoadingView5(Context context) {
        this(context, null);
    }

    public VLoadingView5(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VLoadingView5(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mDefaultSize = DensityUtil.dp2px(50);

        if (context != null && attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.VLoadingView5);
            mCircleColor = typedArray.getColor(R.styleable.VLoadingView5_circle_color, mCircleColor);
            typedArray.recycle();
        }


        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mCircleColor);

        mValueAnimator = ValueAnimator.ofFloat(0.0f, 1.0f);
        mValueAnimator.setDuration(1000);
        mValueAnimator.setInterpolator(new AccelerateInterpolator());
        mValueAnimator.setStartDelay(200);
        mValueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mValueAnimator.start();
            }
        });
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float percent = animation.getAnimatedFraction();
                mCurrentRadius = mMaxRadius * percent;
                mPaint.setAlpha((int) (255.0f * (1.0f - percent)));
                invalidate();
            }
        });
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mMaxRadius = Math.min(w, h) / 2.0f;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int viewSize = ViewUtils.measureMinSize(this, widthMeasureSpec, heightMeasureSpec, mDefaultSize);
        setMeasuredDimension(viewSize, viewSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(getWidth() / 2.0f, getHeight() / 2.0f, mCurrentRadius, mPaint);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mValueAnimator.cancel();
        mValueAnimator.start();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mValueAnimator.cancel();
    }
}

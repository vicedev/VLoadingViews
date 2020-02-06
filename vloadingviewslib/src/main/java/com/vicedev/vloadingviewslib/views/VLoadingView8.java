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

import java.util.ArrayList;
import java.util.List;

/**
 * @author vicedev
 * @date 2020/2/6 11:25
 */
public class VLoadingView8 extends View {

    private int mDefaultSize;
    private int mCircleColor = Color.WHITE;
    private Paint mPaint;
    private float mMaxCircleRadius;
    private long mAnimDuration = 800;
    private int mCircleCount = 12;
    private List<DataBean> mDataBeanList = new ArrayList<>();


    public VLoadingView8(Context context) {
        this(context, null);
    }

    public VLoadingView8(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VLoadingView8(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {

        mDefaultSize = DensityUtil.dp2px(50);
        mMaxCircleRadius = DensityUtil.dp2px(4);

        if (context != null && attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.VLoadingView8);
            mCircleColor = typedArray.getColor(R.styleable.VLoadingView8_circle_color, mCircleColor);
            mMaxCircleRadius = typedArray.getDimension(R.styleable.VLoadingView8_max_circle_radius, mMaxCircleRadius);
            mAnimDuration = typedArray.getInt(R.styleable.VLoadingView8_anim_duration, (int) mAnimDuration);
            typedArray.recycle();
        }

        for (int i = 0; i < mCircleCount; i++) {
            mDataBeanList.add(new DataBean(mMaxCircleRadius, 100 * i));
        }

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mCircleColor);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int viewSize = ViewUtils.measureMinSize(this, widthMeasureSpec, heightMeasureSpec, mDefaultSize);
        setMeasuredDimension(viewSize, viewSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < mDataBeanList.size(); i++) {
            canvas.save();

            canvas.rotate(i * 30.0f, getWidth() / 2.0f, getHeight() / 2.0f);
            canvas.drawCircle(getWidth() / 2.0f, mMaxCircleRadius, mDataBeanList.get(i).radius, mPaint);

            canvas.restore();
        }
        postInvalidateDelayed(10);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        for (int i = 0; i < mDataBeanList.size(); i++) {
            mDataBeanList.get(i).mObjectAnimator.cancel();
            mDataBeanList.get(i).mObjectAnimator.start();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        for (int i = 0; i < mDataBeanList.size(); i++) {
            mDataBeanList.get(i).mObjectAnimator.cancel();
        }
    }

    class DataBean {
        float radius;
        ObjectAnimator mObjectAnimator;

        private void setRadius(float radius) {
            this.radius = radius;
        }

        DataBean(float maxRadius, long delay) {
            mObjectAnimator = ObjectAnimator.ofFloat(this, "radius", 0.0f, maxRadius);
            mObjectAnimator.setDuration(mAnimDuration);
            mObjectAnimator.setRepeatCount(ValueAnimator.INFINITE);
            mObjectAnimator.setRepeatMode(ValueAnimator.REVERSE);
            mObjectAnimator.setStartDelay(delay);
        }
    }
}

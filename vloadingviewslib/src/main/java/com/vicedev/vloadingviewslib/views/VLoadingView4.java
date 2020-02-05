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
 * @date 2020/2/5 21:38
 */
public class VLoadingView4 extends View {

    private int mDefaultSize;
    private int mRectColor = Color.WHITE;
    private Paint mPaint;
    private float mMaxRectSideSize;
    private float mMinRectSideSize;
    private ObjectAnimator mObjectAnimator;
    private DataBean mDataBean;

    public VLoadingView4(Context context) {
        this(context, null);
    }

    public VLoadingView4(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VLoadingView4(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mDefaultSize = DensityUtil.dp2px(50);

        mMaxRectSideSize = DensityUtil.dp2px(14);
        mMinRectSideSize = DensityUtil.dp2px(7);

        if (context != null && attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.VLoadingView4);
            mRectColor = typedArray.getColor(R.styleable.VLoadingView4_rect_color, mRectColor);
            mMaxRectSideSize = typedArray.getDimension(R.styleable.VLoadingView4_max_rect_side_size, mMaxRectSideSize);
            mMinRectSideSize = typedArray.getDimension(R.styleable.VLoadingView4_min_rect_side_size, mMinRectSideSize);
            if (mMinRectSideSize > mMaxRectSideSize) {
                mMinRectSideSize = mMaxRectSideSize;
            }
            typedArray.recycle();
        }

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mRectColor);

        mDataBean = new DataBean(new PointF(mMaxRectSideSize / 2.0f, mMaxRectSideSize / 2.0f), mMaxRectSideSize);

        mObjectAnimator = ObjectAnimator.ofFloat(this, "value", 0.0f, 4.0f);
        mObjectAnimator.setDuration(1800);
        mObjectAnimator.setInterpolator(new LinearInterpolator());
        mObjectAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mObjectAnimator.setRepeatMode(ValueAnimator.RESTART);
    }

    private void setValue(float value) {
        PointF centerPoint = mDataBean.centerPoint;
        if (value >= 0 && value < 1) {
            centerPoint.x = value * (getWidth() - mMaxRectSideSize) + mMaxRectSideSize / 2.0f;
            mDataBean.sideSize = mMaxRectSideSize - (mMaxRectSideSize - mMinRectSideSize) * value;
            mDataBean.rotateAngle = -90 * value;
        } else if (value >= 1 && value < 2) {
            centerPoint.y = (value - 1.0f) * (getHeight() - mMaxRectSideSize) + mMaxRectSideSize / 2.0f;
            mDataBean.sideSize = mMinRectSideSize + (mMaxRectSideSize - mMinRectSideSize) * (value - 1.0f);
            mDataBean.rotateAngle = -90 * (value - 1.0f);
        } else if (value >= 2 && value < 3) {
            centerPoint.x = getWidth() - mMaxRectSideSize / 2.0f - (value - 2.0f) * (getWidth() - mMaxRectSideSize);
            mDataBean.sideSize = mMaxRectSideSize - (mMaxRectSideSize - mMinRectSideSize) * (value - 2.0f);
            mDataBean.rotateAngle = -90 * (value - 2.0f);
        } else {
            centerPoint.y = getHeight() - mMaxRectSideSize / 2.0f - (value - 3.0f) * (getHeight() - mMaxRectSideSize);
            mDataBean.sideSize = mMinRectSideSize + (mMaxRectSideSize - mMinRectSideSize) * (value - 3.0f);
            mDataBean.rotateAngle = -90 * (value - 3.0f);
        }
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
        canvas.rotate(mDataBean.rotateAngle, mDataBean.centerPoint.x, mDataBean.centerPoint.y);
        float v = mDataBean.sideSize / 2.0f;
        canvas.drawRect(mDataBean.centerPoint.x - v, mDataBean.centerPoint.y - v, mDataBean.centerPoint.x + v, mDataBean.centerPoint.y + v, mPaint);
        canvas.restore();

        canvas.save();
        canvas.rotate(180, getWidth() / 2.0f, getHeight() / 2.0f);
        canvas.rotate(mDataBean.rotateAngle, mDataBean.centerPoint.x, mDataBean.centerPoint.y);
        canvas.drawRect(mDataBean.centerPoint.x - v, mDataBean.centerPoint.y - v, mDataBean.centerPoint.x + v, mDataBean.centerPoint.y + v, mPaint);

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

    class DataBean {
        PointF centerPoint;
        float sideSize;
        float rotateAngle;

        DataBean(PointF centerPoint, float sideSize) {
            this.centerPoint = centerPoint;
            this.sideSize = sideSize;
        }
    }

}

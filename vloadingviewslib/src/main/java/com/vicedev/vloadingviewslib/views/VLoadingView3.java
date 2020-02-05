package com.vicedev.vloadingviewslib.views;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateInterpolator;

import androidx.annotation.Nullable;

import com.vicedev.vloadingviewslib.R;
import com.vicedev.vloadingviewslib.utils.DensityUtil;
import com.vicedev.vloadingviewslib.utils.ViewUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author vicedev
 * @date 2020/2/5 9:31
 */
public class VLoadingView3 extends View {

    private int mDefaultSize;
    private int mBarColor = Color.WHITE;
    private Paint mPaint;
    private List<DataBean> mDataBeanList = new ArrayList<>();
    private int mBarCount = 5;
    private float mBarDivideSize;
    private long mPerBarTime = 300;

    public VLoadingView3(Context context) {
        this(context, null);
    }

    public VLoadingView3(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VLoadingView3(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mDefaultSize = DensityUtil.dp2px(50);
        mBarDivideSize = DensityUtil.dp2px(3);

        if (context != null && attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.VLoadingView3);
            mBarColor = typedArray.getColor(R.styleable.VLoadingView3_bar_color, mBarColor);
            mBarCount = typedArray.getInt(R.styleable.VLoadingView3_bar_count, mBarCount);
            mBarDivideSize = typedArray.getDimension(R.styleable.VLoadingView3_bar_divide_size, mBarDivideSize);
            typedArray.recycle();
        }

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mBarColor);
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
            DataBean dataBean = mDataBeanList.get(i);
            if (dataBean.mRectF != null) {
                canvas.drawRect(dataBean.mRectF, mPaint);
            }
        }
        postInvalidateDelayed(10);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);


        for (int i = 0; i < mDataBeanList.size(); i++) {
            mDataBeanList.get(i).mValueAnimator.cancel();
        }

        mDataBeanList.clear();
        for (int i = 0; i < mBarCount; i++) {

            float totalDivideSize = (mBarCount - 1) * mBarDivideSize;
            if (totalDivideSize >= w) {
                return;
            }
            float barWidth = (w - totalDivideSize) / mBarCount;

            RectF rectF = new RectF();
            rectF.left = i * (barWidth + mBarDivideSize);
            rectF.top = h / 3.0f;
            rectF.right = rectF.left + barWidth;
            rectF.bottom = h - rectF.top;

            mDataBeanList.add(new DataBean(rectF, mBarCount <= 1 ? 0 : mPerBarTime / (mBarCount - 1) * i));
        }

        for (int i = 0; i < mDataBeanList.size(); i++) {
            mDataBeanList.get(i).mValueAnimator.start();
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        for (int i = 0; i < mDataBeanList.size(); i++) {
            ValueAnimator valueAnimator = mDataBeanList.get(i).mValueAnimator;
            valueAnimator.cancel();
            valueAnimator.start();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        for (int i = 0; i < mDataBeanList.size(); i++) {
            ValueAnimator valueAnimator = mDataBeanList.get(i).mValueAnimator;
            valueAnimator.cancel();
        }
    }

    class DataBean {
        RectF mRectF;
        ValueAnimator mValueAnimator = ValueAnimator.ofFloat(0.0f, 100.0f);

        DataBean(RectF rectF, long delay) {
            mRectF = rectF;
            mValueAnimator.setRepeatCount(1);
            mValueAnimator.setRepeatMode(ValueAnimator.REVERSE);
            mValueAnimator.setDuration(mPerBarTime);
            mValueAnimator.setInterpolator(new AccelerateInterpolator());
            mValueAnimator.setStartDelay(delay);
            mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {

                    float v = getHeight() / 3.0f;
                    float min = getHeight() / 3.0f / 2.0f;

                    mRectF.top = getHeight() / 2.0f - (min + v * animation.getAnimatedFraction());
                    mRectF.bottom = getHeight() - mRectF.top;
                }
            });

            mValueAnimator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    mValueAnimator.setStartDelay(mBarCount <= 1 ? 0 : (mPerBarTime / (mBarCount - 1)) + 500);
                    mValueAnimator.start();
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

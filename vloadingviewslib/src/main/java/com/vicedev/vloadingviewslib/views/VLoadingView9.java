package com.vicedev.vloadingviewslib.views;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import androidx.annotation.Nullable;

import com.vicedev.vloadingviewslib.R;
import com.vicedev.vloadingviewslib.utils.DensityUtil;
import com.vicedev.vloadingviewslib.utils.ViewUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author vicedev
 * @date 2020/2/7 10:39
 */
public class VLoadingView9 extends View {

    private int mDefaultSize;
    private int mCircleColor = Color.WHITE;
    private Paint mPaint;
    private ValueAnimator mValueAnimator;
    private long mAnimTime = 900;
    private long mDelayTime = 400;

    private List<DataBean> mDataBeanList = new ArrayList<>();

    public VLoadingView9(Context context) {
        this(context, null);
    }

    public VLoadingView9(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VLoadingView9(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {

        mDefaultSize = DensityUtil.dp2px(50);

        if (context != null && attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.VLoadingView9);
            mCircleColor = typedArray.getColor(R.styleable.VLoadingView9_circle_color, mCircleColor);
            typedArray.recycle();
        }


        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mCircleColor);

        long totalTime = mAnimTime + mDelayTime;
        mValueAnimator = ValueAnimator.ofFloat(0.0f, totalTime);
        mValueAnimator.setDuration(totalTime);
        mValueAnimator.setInterpolator(new LinearInterpolator());
        mValueAnimator.setRepeatMode(ValueAnimator.RESTART);
        mValueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float time = (float) animation.getAnimatedValue();
                for (int i = 0; i < mDataBeanList.size(); i++) {
                    DataBean dataBean = mDataBeanList.get(i);
                    if (time < dataBean.startTime || time >= dataBean.endTime) {
                        dataBean.scale = 1.0f;
                    } else {
                        long interval = (long) ((dataBean.endTime - dataBean.startTime) / 2.0f);
                        if (time >= dataBean.startTime && time < dataBean.startTime + interval) {
                            //zoom out
                            dataBean.scale = 1.0f * (interval - (time - dataBean.startTime)) / interval;
                        } else {
                            //zoom in
                            dataBean.scale = 1.0f * (interval - (dataBean.endTime - time)) / interval;
                        }
                    }
                }
                invalidate();
            }
        });
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mValueAnimator.cancel();
        mDataBeanList.clear();
        float perRectWidth = w / 3.0f;
        float perRectHeight = h / 3.0f;
        for (int i = 0; i < 9; i++) {
            float left;
            float top;
            long startTime;
            if (i <= 2) {
                top = 0;
                left = i * perRectWidth;
            } else if (i <= 5) {
                top = perRectHeight;
                left = (i - 3) * perRectWidth;
            } else {
                top = perRectHeight * 2;
                left = (i - 6) * perRectWidth;
            }
            long perDelay = (long) (mDelayTime / 4.0f);
            if (i == 6) {
                startTime = 0;
            } else if (i == 3 || i == 7) {
                startTime = perDelay;
            } else if (i == 0 || i == 4 || i == 8) {
                startTime = perDelay * 2;
            } else if (i == 1 || i == 5) {
                startTime = perDelay * 3;
            } else {
                startTime = mDelayTime;
            }
            mDataBeanList.add(new DataBean(new RectF(left, top, left + perRectWidth, top + perRectHeight), startTime, startTime + mAnimTime));
        }
        mValueAnimator.start();
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
            canvas.save();
            float centerX = (dataBean.rectF.left + dataBean.rectF.right) / 2.0f;
            float centerY = (dataBean.rectF.top + dataBean.rectF.bottom) / 2.0f;
            canvas.scale(dataBean.scale, dataBean.scale, centerX, centerY);
            canvas.drawRect(dataBean.rectF, mPaint);
            canvas.restore();
        }
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

    class DataBean {
        long startTime;
        long endTime;
        RectF rectF;
        float scale = 1.0f;

        DataBean(RectF rectF, long startTime, long endTime) {
            this.startTime = startTime;
            this.endTime = endTime;
            this.rectF = rectF;
        }
    }
}

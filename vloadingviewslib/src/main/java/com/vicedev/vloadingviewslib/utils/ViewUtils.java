package com.vicedev.vloadingviewslib.utils;

import android.view.View;

/**
 * @author vicedev
 * @date 2020/2/4 19:25
 */
public class ViewUtils {
    /**
     * used in onMeasure,return suitable size (same width and height)
     */
    public static int measureMinSize(View view, int widthMeasureSpec, int heightMeasureSpec, int defaultSize) {
        if (view == null) {
            return defaultSize;
        }
        int wMode = View.MeasureSpec.getMode(widthMeasureSpec);
        int hMode = View.MeasureSpec.getMode(heightMeasureSpec);
        int width = View.MeasureSpec.getSize(widthMeasureSpec);
        int height = View.MeasureSpec.getSize(heightMeasureSpec);
        if (wMode == View.MeasureSpec.AT_MOST || wMode == View.MeasureSpec.UNSPECIFIED) {
            width = defaultSize;
        }
        if (hMode == View.MeasureSpec.AT_MOST || hMode == View.MeasureSpec.UNSPECIFIED) {
            height = defaultSize;
        }
        return Math.min(width, height);
    }
}

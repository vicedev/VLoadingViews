package com.vicedev.vloadingviewslib.utils;

import android.content.res.Resources;

/**
 * @author vicedev
 * @date 2020/2/3 19:24
 */
public class DensityUtil {
    /**
     * dp->px
     */
    public static int dp2px(float dpValue) {
        return (int) (0.5f + dpValue * Resources.getSystem().getDisplayMetrics().density);
    }

    /**
     * px->dp
     */
    public static float px2dp(float pxValue) {
        return (pxValue / Resources.getSystem().getDisplayMetrics().density);
    }

    /**
     * screen width(px)
     */
    public static float getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    /**
     * screen height(px)
     */
    public static float getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

}

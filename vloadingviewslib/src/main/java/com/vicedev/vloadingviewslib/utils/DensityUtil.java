package com.vicedev.vloadingviewslib.utils;

import android.content.res.Resources;

/**
 * @author vicedev
 * @date 2020/2/3 19:24
 */
public class DensityUtil {
    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dp2px(float dpValue) {
        return (int) (0.5f + dpValue * Resources.getSystem().getDisplayMetrics().density);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static float px2dp(float pxValue) {
        return (pxValue / Resources.getSystem().getDisplayMetrics().density);
    }

    /**
     * 根据手机的分辨率获取宽度像素
     */
    public static float getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    /**
     * 根据手机的分辨率获取高度像素
     */
    public static float getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

}

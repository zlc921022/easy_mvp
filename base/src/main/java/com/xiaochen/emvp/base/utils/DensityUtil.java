package com.xiaochen.emvp.base.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;

/**
 * @author zlc
 * email : zlc921022@163.com
 * desc : 屏幕适配类
 */
public class DensityUtil {

    private DensityUtil(){}
    /**
     * dp 转 px
     * @param context
     * @return
     */
    public static int dp2px(Context context,float dp){
        if(context == null)
            throw new RuntimeException("context not is null");
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    /**
     * px 转 dp
     * @param context
     * @return
     */
    public static int px2dp(Context context,float px){
        if(context == null)
            throw new RuntimeException("context not is null");
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }

    /**
     * sp 转 px
     * @param context
     * @return
     */
    public static int sp2px(Context context,float sp){
        if(context == null)
            throw new RuntimeException("context not is null");
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,sp,dm);
    }

    /**
     * px 转 sp
     * @param context
     * @return
     */
    public static int px2sp(Context context,float px){
        if(context == null)
            throw new RuntimeException("context not is null");
        final float scale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (px / scale);
    }
}

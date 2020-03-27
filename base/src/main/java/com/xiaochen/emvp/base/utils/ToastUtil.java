package com.xiaochen.emvp.base.utils;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.widget.Toast;

/**
 * Toast提示框
 * @author zlc
 */
public class ToastUtil {

    private ToastUtil(){}

    private static Toast toast;
    /**
     * 显示长时间Toast
     * @param context
     * @param text
     */
    public static void showLongToast(Context context, String text) {
        if(TextUtils.isEmpty(text)) return;
        if (toast == null) {
            toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
        }
        toast.setText(text);
        toast.show();
    }

    /***
     * 显示短时间Toast
     * @param context
     * @param text
     */
    public static void showShortToast(Context context, String text) {

        if(TextUtils.isEmpty(text)) return;
        if (toast == null) {
            toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        }
        toast.setText(text);
        toast.show();
    }

    /**
     * 吐司显示到屏幕中心
     * @param context
     * @param text
     */
    public static void showCenterToast(Context context, String text) {

        if(TextUtils.isEmpty(text)) return;
        if (toast == null) {
            toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        }
        toast.setGravity(Gravity.CENTER,0,0);
        toast.setText(text);
        toast.show();
    }

    /**
     * 取消吐司显示
     */
    public static void cancel(){
        if (toast != null) {
            LogUtil.e("msg","退出了toast");
            toast.cancel();
        }
    }

}

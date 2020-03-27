package com.xiaochen.emvp.base.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.xiaochen.emvp.base.GlideApp;
import com.xiaochen.emvp.base.R;

/**
 * @author zlc
 * email : zlc921022@163.com
 * desc : 图片加载类
 */
public class ImageUtil {

    private ImageUtil() {
    }

    /**
     * 正常加载图片
     *
     * @param context
     * @param img
     * @param url
     */
    public static void showImage(Context context, ImageView img, String url) {
        GlideApp.with(context)
                .load(url)
                .into(img);
    }

    /**
     * 加载正方形图片
     *
     * @param context
     * @param img
     * @param url
     */
    public static void showSquare(Context context, ImageView img, String url) {
        GlideApp.with(context)
                .load(url)
                .centerCrop()
                .into(img);
    }

    /**
     * 加载头像类圆形图片
     *
     * @param context
     * @param img
     * @param url
     */
    public static void showCircle(Context context, ImageView img, String url) {
        GlideApp.with(context)
                .load(url)
                .transform(new CircleCrop())
                .into(img);
    }


    /**
     * 加载gif图片
     *
     * @param context
     * @param img
     * @param resId
     */
    public static void showGif(Context context, ImageView img, int resId) {
        GlideApp.with(context)
                .asGif()
                .load(resId)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(img);
    }

    /**
     * 加载gif图片
     *
     * @param context
     * @param img
     * @param resId
     */
    public static void showGif2(Context context, ImageView img, int resId) {
        final RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.icon_pull)
                .error(R.drawable.icon_pull);
        Glide.with(context)
                .asGif()
                .load(resId)
                .apply(options)
                .into(img);
    }

    /**
     * 加载本地图片
     *
     * @param context
     * @param img
     * @param url
     */
    public static void showLocalImage(Context context, ImageView img, String url) {
        GlideApp.with(context)
                .load(url)
                .thumbnail(0.1f)
                .into(img);
    }

}

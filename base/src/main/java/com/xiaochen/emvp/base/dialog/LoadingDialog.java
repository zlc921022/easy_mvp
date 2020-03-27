package com.xiaochen.emvp.base.dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.xiaochen.emvp.base.R;
import com.xiaochen.emvp.base.utils.ImageUtil;

/**
 * Created by zlc on 2019/7/9
 * Email: zlc921022@163.com
 * Desc: loading对话框
 *
 * @author zlc
 */
public class LoadingDialog extends MaterialDialog {

    private ImageView mLoadingIcon;
    private Context mContext;
    private MaterialDialog mDialog;

    private LoadingDialog(Builder builder) {
        super(builder);
        this.mContext = builder.getContext();
        initDialog();
    }

    public static LoadingDialog getLoading(Context context) {
        return new LoadingDialog(new MaterialDialog.Builder(context));
    }

    private void initDialog() {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(mContext);
        View layout = LayoutInflater.from(mContext).inflate(R.layout.layout_loading_dialog, null);
        mLoadingIcon = layout.findViewById(R.id.loading_icon);
        builder.customView(layout, false);
        mDialog = builder.build();
        mDialog.setCanceledOnTouchOutside(false);
    }

    /**
     * 显示loading
     */
    @Override
    public void show() {
        try {
            if (!isShowing()) {
                mDialog.show();
                setLoadingWindow();
                showGif();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isShowing() {
        return mDialog != null && mDialog.isShowing();
    }

    /**
     * 显示gif图
     */
    private void showGif() {
        if (mContext == null || mLoadingIcon == null) {
            return;
        }
        ImageUtil.showGif(mContext, mLoadingIcon, R.drawable.loading);
    }

    /**
     * 关闭弹框
     */
    @Override
    public void dismiss() {
        if (isShowing()) {
            mDialog.dismiss();
        }
    }

    /**
     * 设置弹框点击外部能否关闭
     */
    @Override
    public void setCanceledOnTouchOutside(boolean cancel) {
        super.setCanceledOnTouchOutside(cancel);
    }

    /**
     * 获取dialog对象
     */
    @SuppressWarnings("all")
    public <T extends MaterialDialog> T getDialog() {
        return (T) this;
    }

    /**
     * 设置loading 弹框的宽度和背景
     */
    private void setLoadingWindow() {
        if (mContext == null || mDialog == null) {
            return;
        }
        Window window = mDialog.getWindow();
        if (window != null) {
            window.setDimAmount(0.20f);
            WindowManager.LayoutParams lp = window.getAttributes();
            int size = (int) mContext.getResources().getDimension(R.dimen.dp_80);
            lp.width = size;
            lp.height = size;
            window.setAttributes(lp);
        }
    }

}

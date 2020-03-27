package com.xiaochen.emvp.base.ui;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;

import com.xiaochen.emvp.base.dialog.LoadingDialog;
import com.xiaochen.emvp.base.presenter.AbsBasePresenter;
import com.jaeger.library.StatusBarUtil;

import butterknife.ButterKnife;

/**
 * 抽象的父类activity 所有activity继承当前类
 *
 * @param <T>
 * @author admin
 */
public abstract class AbsBaseActivity<T extends AbsBasePresenter> extends BaseActivity {

    protected T mPresenter;
    protected LoadingDialog mLoading;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        ButterKnife.bind(this);
        setStatusBar();
        initView();
        initData();
        initListener();
    }

    @Override
    protected void initData() {
        super.initData();
        if (isShowLoading()) {
            mLoading = LoadingDialog.getLoading(this);
        }
        mPresenter = getPresenter();
        if (mPresenter != null) {
            mPresenter.bindLifeCycle(getLifecycle());
            loadingDispose();
        }
    }

    /**
     * 获取布局id
     *
     * @return id
     */
    protected abstract int getLayoutId();

    /**
     * 获取Presenter对象
     *
     * @return presenter
     */
    protected T getPresenter() {
        return null;
    }

    /**
     * 设置沉浸式 状态栏
     */
    protected void setStatusBar() {
        StatusBarUtil.setLightMode(this);
        StatusBarUtil.setTransparentForImageView(this, null);
    }

    /**
     * loading统一处理
     */
    @SuppressWarnings("all")
    private void loadingDispose() {
        mPresenter.mLoadingLiveData.observe(this, (Observer<Boolean>) isShow -> {
            if (mLoading == null) {
                return;
            }
            if (isShow) {
                showLoading();
            } else {
                dismissLoading();
            }
        });
    }

    /**
     * 显示loading 统一处理
     */
    public void showLoading() {
        if (mLoading != null) {
            mLoading.show();
        }
    }

    /**
     * 关闭loading 统一处理
     */
    public void dismissLoading() {
        if (mLoading != null) {
            mLoading.dismiss();
        }
    }

    /**
     * 是否需要显示loading
     *
     * @return true 显示 默认false
     */
    public boolean isShowLoading() {
        return false;
    }
}

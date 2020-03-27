package com.xiaochen.emvp.base.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;

import com.xiaochen.emvp.base.dialog.LoadingDialog;
import com.xiaochen.emvp.base.presenter.AbsBasePresenter;

import butterknife.ButterKnife;

/**
 * 所有fragment的父类，子类必须实现它
 *
 * @param <T>
 * @author zlc
 */
public abstract class AbsBaseFragment<T extends AbsBasePresenter> extends BaseFragment {

    protected View mView;
    protected T mPresenter;
    /**
     * 是否初始化了View，即是否调用过onCreateView方法
     */
    private boolean isInitView = false;

    protected LoadingDialog mLoading;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(getLayoutId(), container, false);
        ButterKnife.bind(this, mView);
        initView();
        isInitView = true;
        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
        initListener();
    }

    @Override
    protected void initData() {
        super.initData();
        if (isShowLoading()) {
            mLoading = LoadingDialog.getLoading(mContext);
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


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!isInitView || !isVisibleToUser) {
            return;
        }
        lazyLoadData();
    }

    /**
     * 懒加载数据
     */
    protected void lazyLoadData() {

    }

    /**
     * 是否需要显示loading
     *
     * @return true 显示 默认false
     */
    public boolean isShowLoading() {
        return false;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isInitView = false;
    }

}

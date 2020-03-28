package com.xiaochen.emvp.base.presenter;

import com.xiaochen.emvp.base.utils.LogUtil;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.OnLifecycleEvent;

/**
 * <p>presenter基类</p >
 *
 * @author zhenglecheng
 * @date 2019/11/20
 */
public class BasePresenter implements LifecycleObserver {

    private final String TAG = this.getClass().getSimpleName();

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    public void onCreateView(LifecycleOwner lifecycleOwner) {
        LogUtil.e(TAG, "onResume");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void onDetachView(LifecycleOwner lifecycleOwner) {
        LogUtil.e(TAG, "onDestroy");
    }

}

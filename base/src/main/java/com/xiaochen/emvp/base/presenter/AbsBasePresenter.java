package com.xiaochen.emvp.base.presenter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.MutableLiveData;

import com.xiaochen.emvp.base.view.IBaseView;
import com.xiaochen.emvp.data.ApiManager;
import com.xiaochen.emvp.data.BaseResponseObserver;
import com.xiaochen.emvp.data.response.BaseResponseVO;
import com.uber.autodispose.AutoDispose;
import com.uber.autodispose.AutoDisposeConverter;
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider;

import java.lang.ref.SoftReference;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * presenter的父类,所有子类都必须实现它
 *
 * @param <T>
 */
@SuppressWarnings("all")
public abstract class AbsBasePresenter<T extends IBaseView> extends BasePresenter {

    protected Context mContext;
    protected ApiManager mApiManager;
    private SoftReference<T> mView;
    private Lifecycle mLifecycle;
    /**
     * 通过liveData处理loading显示和关闭
     */
    public MutableLiveData<Boolean> mLoadingLiveData = new MutableLiveData<Boolean>();

    public AbsBasePresenter(@NonNull Context context, @NonNull final T view) {
        this.mContext = context;
        this.mView = new SoftReference<T>(view);
        mApiManager = ApiManager.getManager();
    }

    /**
     * 传入lifecycle，用于管理页面生命周期
     */
    public void bindLifeCycle(@NonNull final Lifecycle lifecycle) {
        this.mLifecycle = lifecycle;
        this.mLifecycle.addObserver(this);
    }

    @Override
    public void onCreateView() {
       super.onCreateView();
    }

    @Override
    public void onDetachView() {
        super.onDetachView();
        if (mLifecycle != null) {
            mLifecycle.removeObserver(this);
            mLifecycle = null;
        }
        if (mView != null) {
            mView.clear();
            mView = null;
        }
    }

    /**
     * 获取view接口对象
     * 如果为null，要么就是没实现view接口，要么就是activity被销毁了
     *
     * @return
     */
    protected T getView() {
        if (mView == null) {
            return null;
        }
        return mView.get();
    }

    /**
     * 请求数据
     *
     * @param observable
     * @param observer
     */
    protected <V, P extends BaseResponseVO<V>, Q extends BaseResponseObserver<V, P>> void requestData(
            final Observable<P> observable, final Q observer) {
        if (getView() == null) {
            return;
        }
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(bindAutoDispose())
                .subscribe(observer);
    }

    /**
     * 绑定生命周期 防止内存泄漏
     */
    protected <P> AutoDisposeConverter<P> bindAutoDispose() {
        return AutoDispose.autoDisposable(AndroidLifecycleScopeProvider
                .from(mLifecycle, Lifecycle.Event.ON_DESTROY));
    }

    /**
     * 统一处理loading显示和关闭
     *
     * @param <V> 返回的实体data
     * @param <P> 返回的包含响应码，错误信息的的对象
     */
    protected abstract class ResponseObserverCallBack<V, P extends BaseResponseVO<V>> extends BaseResponseObserver<V, P> {
        @Override
        public void onStart() {
            mLoadingLiveData.setValue(true);
        }

        @Override
        public abstract void onSuccess(V data);

        @Override
        public abstract void onFailure(String code, String errMessage);

        @Override
        public void onEnd() {
            mLoadingLiveData.setValue(false);
        }
    }

}

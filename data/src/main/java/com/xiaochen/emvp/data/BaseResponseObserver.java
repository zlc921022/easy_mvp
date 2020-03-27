package com.xiaochen.emvp.data;

import com.xiaochen.emvp.data.response.BaseResponseVO;

import java.net.ConnectException;
import java.net.UnknownHostException;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;


/**
 * @author zhenglecheng
 * @date 2017/10/19
 * 基类Observer 用于网络请求T
 */
public abstract class BaseResponseObserver<T, P extends BaseResponseVO<T>> implements Observer<P>,
        BaseResponseCallBack<T> {

    protected BaseResponseObserver() {
        onStart();
    }

    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(P result) {
        if (result == null) {
            return;
        }
        if (result.errorCode == -1) {
            onFailure(result.errorCode + "", result.errorMsg);
        } else {
            onSuccess(result.data);
        }
    }

    @Override
    public void onError(Throwable e) {
        String errorMessage;
        if (e instanceof UnknownHostException || e instanceof ConnectException) {
            errorMessage = "网络不可用，请检查网络";
        } else {
            errorMessage = e.getMessage();
        }
        onFailure("-1", errorMessage);
        onEnd();
    }

    @Override
    public void onComplete() {
        onEnd();
    }
}

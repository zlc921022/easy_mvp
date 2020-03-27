package com.xiaochen.emvp.data;

/**
 * 服务数据接口回调
 */
public interface BaseResponseCallBack<T> {

    /**
     * 开始请求
     */
    void onStart();

    /**
     * 请求成功
     *
     * @param data 对象
     */
    void onSuccess(T data);

    /**
     * 请求失败
     *
     * @param code       错误码
     * @param errMessage 错误信息
     */
    void onFailure(String code, String errMessage);

    /**
     * 请求结束
     */
    void onEnd();
}

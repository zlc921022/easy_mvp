package com.xiaochen.emvp;

import com.xiaochen.emvp.base.BaseApplication;
import com.xiaochen.emvp.base.utils.LogUtil;
import com.xiaochen.emvp.data.HttpManager;

/**
 * <p></p >
 *
 * @author zhenglecheng
 * @date 2019/12/26
 */
public class MyApplication extends BaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtil.init();
        HttpManager.createProvider(getBaseUrl(), true, this);
    }

    private String getBaseUrl() {
        return ApiConstants.BASE_URL;
    }
}

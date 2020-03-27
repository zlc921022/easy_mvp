package com.xiaochen.emvp.base;

import android.app.Application;
import android.content.Context;

import androidx.multidex.MultiDex;

/**
 * 父类application
 * 负责context和apiManager对象的创建和获取
 *
 * @author admin
 */
public class BaseApplication extends Application {

    private static BaseApplication mApplication;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this;
    }

    public static BaseApplication getApplication() {
        return mApplication;
    }
}

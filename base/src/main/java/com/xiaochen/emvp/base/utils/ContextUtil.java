package com.xiaochen.emvp.base.utils;

import android.app.Application;
import android.content.Context;

import com.xiaochen.emvp.base.BaseApplication;

/**
 * @author zlc
 * email : zlc921022@163.com
 * desc : 获取上下文
 */
public class ContextUtil {

    private ContextUtil(){}
    /**
     * 获取全局唯一Context对象
     * @return
     */
    public static Context getContext(){
        return BaseApplication.getApplication();
    }

    /**
     * 获取Application对象
     * @return
     */
    public static Application getApplication(){
        return BaseApplication.getApplication();
    }
}

package com.xiaochen.emvp.base.ui;

import android.app.ActivityManager;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.xiaochen.emvp.base.utils.LogUtil;


/**
 * 父类activity 子类应该直接继承抽象父类activity
 *
 * @author admin
 */
public class BaseActivity extends AppCompatActivity implements View.OnClickListener {

    protected final String TAG = getClass().getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * 初始化控件
     */
    protected void initView() {
        // todo
    }

    /**
     * 加载数据
     */
    protected void initData() {
        // todo
    }

    /**
     * 点击事件处理
     */
    protected void initListener() {
        // todo
    }

    @Override
    public void onClick(View v) {
        // todo
    }

    @Override
    protected void onResume() {
        super.onResume();
        LogUtil.e(TAG, "onResume");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}

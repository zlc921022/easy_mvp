package com.xiaochen.emvp.base.ui;

import android.content.Context;
import android.view.View;

import androidx.fragment.app.Fragment;

import com.xiaochen.emvp.base.utils.LogUtil;

//子类应该直接继承抽象父类fragment
public class BaseFragment extends Fragment implements View.OnClickListener {

    protected Context mContext;
    protected final String TAG = this.getClass().getSimpleName();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    /**
     * 初始化控件
     */
    protected void initView() {
    }

    /**
     * 加载数据
     */
    protected void initData() {
    }

    /**
     * 点击事件处理
     */
    protected void initListener() {
    }

    @Override
    public void onClick(View v) {
    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtil.e(TAG, "onResume");
    }

}

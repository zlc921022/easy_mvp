package com.xiaochen.emvp.widget.ui.fragment;

import com.xiaochen.emvp.widget.R;
import com.xiaochen.emvp.widget.view.SmileFaceView;

/**
 * Created by zlc on 2018/7/16.
 */

public class SmileFaceFragment extends BaseFragment {

    private SmileFaceView mSmileView;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_smile_face;
    }

    @Override
    protected void initView() {
        super.initView();
        mSmileView = findView(R.id.smile_view);
    }

    @Override
    protected void initData() {
    }

    public void smileAnimation(){
        mSmileView.playAnimation();
    }
}

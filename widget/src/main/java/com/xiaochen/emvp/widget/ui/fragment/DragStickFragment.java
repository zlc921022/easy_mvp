package com.xiaochen.emvp.widget.ui.fragment;

import android.widget.ListView;

import com.xiaochen.emvp.widget.R;
import com.xiaochen.emvp.widget.ui.adapter.PointsAdapter;

/**
 * Created by zlc on 2018/7/16.
 */

public class DragStickFragment extends BaseFragment{
    private ListView mLvPoints;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_drag_stick;
    }

    @Override
    protected void initView() {
        super.initView();
        mLvPoints = findView(R.id.lv_points);
    }

    @Override
    protected void initData() {
        mLvPoints.setAdapter(new PointsAdapter(mActivity));
    }
}

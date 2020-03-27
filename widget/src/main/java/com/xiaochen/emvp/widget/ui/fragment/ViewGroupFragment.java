package com.xiaochen.emvp.widget.ui.fragment;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.View;

import com.google.android.material.tabs.TabLayout;
import com.xiaochen.emvp.widget.R;
import com.xiaochen.emvp.widget.ui.adapter.MyPagerAdapter;

import java.util.ArrayList;

/**
 * Created by zlc on 2018/7/14.
 */

public class ViewGroupFragment extends BaseFragment{

    private TabLayout mTab;
    private ViewPager mViewpager;
    private ArrayList<Fragment> mFragments;
    private String[] mTitles = {
        "自定义布局","流式布局","奥运五环","游标卡尺"
    };

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_viewgroup;
    }

    @Override
    protected void initView() {
        super.initView();
        mTab = findView(R.id.tab1);
        mViewpager = findView(R.id.viewpager1);
    }

    @Override
    protected void initData() {
        mFragments = new ArrayList<>();
        addFragments();
        MyPagerAdapter adapter = new MyPagerAdapter(getChildFragmentManager());
        adapter.setFragments(mFragments);
        adapter.setTitles(mTitles);
        mViewpager.setAdapter(adapter);
        mTab.setupWithViewPager(mViewpager);
    }

    private void addFragments() {
        mFragments.add(new MyLayoutFragment());
        mFragments.add(new FlowLayoutFragment());
        mFragments.add(new OlymicRingsFragment());
        mFragments.add(new RangeSeekBarFragment());
    }

    @Override
    public void initListener() {
        super.initListener();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
    }
}

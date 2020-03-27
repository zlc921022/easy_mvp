package com.xiaochen.emvp.widget.ui.fragment;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.View;

import com.google.android.material.tabs.TabLayout;
import com.xiaochen.emvp.widget.R;
import com.xiaochen.emvp.widget.ui.adapter.MyPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zlc on 2018/7/14.
 */

public class ViewFragment extends BaseFragment{

    private TabLayout mTab;
    private ViewPager mViewpager;
    private List<Fragment> mFragments;
    private String[] mTitles = {
            "开关","loading","圆形进度条",
            "饼状图","QQ小红点","雷达蜘蛛网","钟表盘"
    };
    private SmileFaceFragment mSmileFaceFragment;
    private CircleProgressFragment mProgressFragment;
    private PieChartFragment mPieChartFrament;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_view;
    }

    @Override
    protected void initView() {
        super.initView();
        mTab = findView(R.id.tab);
        mViewpager = findView(R.id.viewpager);
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
        mFragments.add(new ToggleFragment());
        mFragments.add(mSmileFaceFragment = new SmileFaceFragment());
        mFragments.add(mProgressFragment = new CircleProgressFragment());
        mFragments.add(mPieChartFrament = new PieChartFragment());
        mFragments.add(new DragStickFragment());
        mFragments.add(new CobwebFragment());
        mFragments.add(new ClockFragment());
    }

    @Override
    public void initListener() {
        super.initListener();
        mViewpager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position){
                    //loading动画处理
                    case 1:
                        mSmileFaceFragment.smileAnimation();
                        break;
                    //圆形进度条动画处理
                    case 2:
                        mProgressFragment.progressAnimation();
                        break;
                    //饼状图动画处理
                    case 3:
                        mPieChartFrament.chartAnimation();
                        break;
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
    }
}

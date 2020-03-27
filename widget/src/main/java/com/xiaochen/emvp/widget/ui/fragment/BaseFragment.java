package com.xiaochen.emvp.widget.ui.fragment;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by zlc on 2018/7/14.
 */

public abstract class BaseFragment extends Fragment implements View.OnClickListener{

    protected FragmentActivity mActivity;
    protected View mView;
    protected LayoutInflater mLayoutInflater;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mActivity = getActivity();
        mLayoutInflater = LayoutInflater.from(mActivity);
        mView = mLayoutInflater.inflate(getLayoutId(), container, false);
        initView();
        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
        initListener();
    }

    protected abstract int getLayoutId();
    protected abstract void initData();
    protected void initView(){}
    public void initListener(){}

    protected <T extends View> T findView(int id){
        return mView != null ? (T) mView.findViewById(id) : null;
    }

    @Override
    public void onClick(View v){
    }

}

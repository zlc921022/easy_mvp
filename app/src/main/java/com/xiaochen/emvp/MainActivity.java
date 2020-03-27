package com.xiaochen.emvp;

import android.content.Intent;

import com.xiaochen.emvp.base.ui.AbsBaseActivity;
import com.xiaochen.emvp.base.utils.LogUtil;
import com.xiaochen.emvp.base.utils.ToastUtil;
import com.xiaochen.emvp.data.response.HomeArticleRespVO;
import com.xiaochen.emvp.presenter.TestPresenter;
import com.xiaochen.emvp.widget.ui.WidgetActivity;
import com.xiaochen.emvp.view.ITestView;

import butterknife.OnClick;

/**
 * <p>主页</p >
 *
 * @author zhenglecheng
 * @date 2019/12/26
 */
public class MainActivity extends AbsBaseActivity<TestPresenter> implements ITestView {


    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected TestPresenter getPresenter() {
        return new TestPresenter(this, this);
    }

    @OnClick(R.id.button_test)
    void onClickButtonTest() {
        mPresenter.getHomeArticles(1);
    }

    @OnClick(R.id.button_view)
    void onClickButtonView() {
        Intent intent = new Intent(this, WidgetActivity.class);
        startActivity(intent);
    }

    @Override
    public void setData(HomeArticleRespVO.Data data) {
        LogUtil.e(TAG, data != null ? data.toString() : null);
    }

    @Override
    public void onError(String msg, String code) {
        ToastUtil.showShortToast(this, msg);
    }
}

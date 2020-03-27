﻿﻿﻿# easy-mvp项目

====
## 1. 项目框架介绍

项目框架采用RxJava + Retrofit + AndroidX + JetPack + MVP + 模块化方式搭建，屏幕适配方面采用的是今日头条的适配方案，注释详尽，是一个适合新手入门，老手快速开发的框架。

## 2. 模块介绍
     a. base模块：主要是针对activity，fragment，presenter，view接口的封装，同时针对rv适配器和下拉刷新框架做了封装，并且提供了大量工具类。
     b. data模块：主要是对接口请求request，接口响应response，api提供类，okhttp，retrofit，拦截器，接口回调等配合搭建的网络框架。
     c. fastble模块：主要是针对蓝牙操作封装的一个快速开发框架。
     d. widget模块：是我自定义View的模块。

## 3. 项目使用
     a. 定义一个Activity继承AbsBaseActivity,实现getPresenter和getLayoutId方法，前者是创建Presenter对象，后者是加载布局
```
public class TestActivity extends AbsBaseActivity<TestPresenter> implements ITestView {


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

    // 数据请求成功回调
    @Override
    public void setData(HomeArticleRespVO.Data data) {
        LogUtil.e(TAG, data != null ? data.toString() : null);
    }

   // 数据请求失败回调
    @Override
    public void onError(String msg, String code) {
        ToastUtil.showShortToast(this, msg);
    }
}


```
  b. 创建一个presenter，里面定义网络请求方法，定义为TestPresenter

```
public class TestPresenter extends AbsBasePresenter<ITestView> {

    private final IServiceApi mServiceApi;

    public TestPresenter(@NonNull Context context,ITestView view) {
        super(context,view);
        mServiceApi = mApiManager.createApi(IServiceApi.class);
    }

  // 网络请求方法
    public void getHomeArticles(final int page) {
        ResponseObserverCallBack<HomeArticleRespVO.Data, HomeArticleRespVO> observer =
                new ResponseObserverCallBack<HomeArticleRespVO.Data, HomeArticleRespVO>() {

                    @Override
                    public void onSuccess(HomeArticleRespVO.Data data) {
                        if (getView() != null) {
                            getView().setData(data);
                        }
                    }

                    @Override
                    public void onFailure(String code, String errMessage) {
                        if (getView() != null) {
                            getView().onError(errMessage, code);
                        }
                    }
                };
        Observable<HomeArticleRespVO> observable = mServiceApi.getHomeArticles(page);
        requestData(observable, observer);
    }

}

```

c.  一个网络请求就是这么简单，不需要考虑页面生命周期，因为已经通过JetPack组件处理了，不需要考虑rxjava内存泄漏问题，因为通过AutoDispose处理了，不需要每次都处理loading的显示和关闭，因为通过JetPack的liveData对象已经在AbsBaseActivity里面统一处理了loading的显示和关闭，使用起来非常简洁方便。


## 4. 项目设计技术点和开源库
```
- 模块化开发
- mvp
- rxjava
- rxandroid
- retrofit
- okhttp
- lifecycle
- livedata
- glide
- eventbus
- butterknife
- SmartRefreshLayout
- autodispose
- rxpermissions
- BaseRecyclerViewAdapterHelper   
- timber
- banner
- kotlin
- statusbarutil

```

## 5. 自定义View模块Widget介绍

  a. 自定义View

  1. [自定义View实战之开关按钮实现](https://blog.csdn.net/rjgcszlc/article/details/80977898)
  2. [自定义View实战之仿土豆Loading实现](https://blog.csdn.net/rjgcszlc/article/details/80978184)
  3. [自定义View实战之渐变,可拨动，带动画圆环控件实现](https://blog.csdn.net/rjgcszlc/article/details/80991937)
  4. [自定义View实战之饼状图效果实现](https://blog.csdn.net/rjgcszlc/article/details/80992243)
  5. [自定义View实战之仿QQ小红点实现](https://blog.csdn.net/rjgcszlc/article/details/80992634)
  6. [自定义View实战之仿雷达蜘蛛网实现](https://blog.csdn.net/rjgcszlc/article/details/80992909)
  7. [自定义View之钟表盘实现](https://blog.csdn.net/rjgcszlc/article/details/80993684)

b. 自定义ViewGroup系列

  1. [自定义ViewGroup之自定义布局的实现](https://blog.csdn.net/rjgcszlc/article/details/81007284)
  2. [自定义ViewGroup之流式布局的实现](https://blog.csdn.net/rjgcszlc/article/details/81007638)
  3. [自定义ViewGroup之仿奥运五环的实现](https://blog.csdn.net/rjgcszlc/article/details/81007940)
  4. [自定义ViewGroup之游标卡尺的实现](https://blog.csdn.net/rjgcszlc/article/details/81008461)

## 6. [点击进入我的博客](http://blog.csdn.net/rjgcszlc "尽人事看天意")

## 7. 联系方式

    QQ:1509815887

## 8. 感谢

    如果觉得好就给我右上角star点一下吧, 如果觉得不好 欢迎批评指点 ,感谢。









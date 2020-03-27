package com.xiaochen.emvp.data;

import android.content.Context;


import com.xiaochen.emvp.data.interceptor.CommonHeaderInterceptor;
import com.xiaochen.emvp.data.interceptor.CommonParamInterceptor;
import com.xiaochen.emvp.data.interceptor.HttpLogerInterceptor;
import com.xiaochen.emvp.data.utils.SSLUtil;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

/**
 * Http提供类，主要是负责创建okhttp，Retrofit对象
 * 统一添加头部信息，提供创建api接口的方法
 */
@SuppressWarnings("all")
public class HttpManager {

    private Context mContext;
    private String mBaseUrl;
    private boolean isDebug;
    private OkHttpClient mOkHttpClient;
    private Retrofit mRetrofit;
    private static HttpManager sHttpProvider = null;
    private String TAG = "HttpProvider";
    /**
     * 管理cookie
     */
    private final HashMap<String, List<Cookie>> cookieStore = new HashMap<>();

    private HttpManager(String baseUrl, boolean isDebug, Context context) {
        this.mContext = context;
        this.mBaseUrl = baseUrl;
        this.isDebug = isDebug;
        this.mOkHttpClient = createOkHttpClient();
        this.mRetrofit = createRetrofit();
    }

    public static HttpManager createProvider(String baseUrl, boolean isDebug, Context context) {
        if (sHttpProvider == null) {
            synchronized (HttpManager.class) {
                if (sHttpProvider == null) {
                    sHttpProvider = new HttpManager(baseUrl, isDebug, context);
                }
            }
        }
        return sHttpProvider;
    }

    //获取provider对象
    public static HttpManager getProvider() {
        return sHttpProvider;
    }


    //获取创建的服务对象
    public <T> T createApi(final Class<T> service) {
        return mRetrofit.create(service);
    }

    //创建Retrofit
    private Retrofit createRetrofit() {
        return new Retrofit.Builder()
                .client(mOkHttpClient)
                .baseUrl(mBaseUrl)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    //创建OkHttpClient
    private OkHttpClient createOkHttpClient() {
        OkHttpClient.Builder srcBuilder = new OkHttpClient.Builder()
                .cookieJar(mCookies)
                .addInterceptor(new CommonHeaderInterceptor())
                .addInterceptor(new CommonParamInterceptor())
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS);
        // 添加日志拦截器
        addLogInterceptor(srcBuilder);
        //处理https
        httpSSl(srcBuilder);
        return srcBuilder.build();
    }

    //处理https
    private void httpSSl(OkHttpClient.Builder srcBuilder) {
        if (mBaseUrl.startsWith("https:")) {
            try {
                final SSLUtil.SSLParams sslParams =
                        SSLUtil.getSslSocketFactory(null, null, null);
                srcBuilder.sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager);
                srcBuilder.hostnameVerifier(((hostname, session) -> true));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 添加日志拦截器
     */
    private void addLogInterceptor(OkHttpClient.Builder srcBuilder) {
        if (isDebug) {
            srcBuilder.addInterceptor(getLogInterceptor());
        }
    }

    /**
     * 获取日志拦截器
     */
    private Interceptor getLogInterceptor() {
        HttpLogerInterceptor interceptor = new HttpLogerInterceptor(new HttpLogerInterceptor.Logger() {
            @Override
            public void log(@NotNull String message) {
                try {
                    Timber.tag("HTTP").e(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        interceptor.setLevel(HttpLogerInterceptor.Level.BODY);
        return interceptor;
    }

    /**
     * cookie相关
     */
    final CookieJar mCookies = new CookieJar() {
        @Override
        public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
            cookieStore.put(url.host(), cookies);
        }

        @Override
        public List<Cookie> loadForRequest(HttpUrl url) {
            List<Cookie> cookies = cookieStore.get(url.host());
            return cookies != null ? cookies : new ArrayList<>();
        }
    };

}

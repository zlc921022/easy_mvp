package com.xiaochen.emvp.data.interceptor;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * <p>通用请求头拦截器</p >
 *
 * @author zhenglecheng
 * @date 2019/12/27
 */
public class CommonHeaderInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Request.Builder builder = request.newBuilder();
        builder.headers(getHeads());
        return chain.proceed(builder.build());
    }

    /**
     * 添加通用请求头并获取请求头
     */
    private Headers getHeads() {
        HashMap<String, String> heads = new HashMap<>();
        heads.put("token","1");
        heads.put("id","2");
        heads.put("cookie","3");
        return Headers.of(heads);
    }
}

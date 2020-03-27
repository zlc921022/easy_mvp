package com.xiaochen.emvp.data.interceptor;

import android.text.TextUtils;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;

/**
 * <p>添加通用参数拦截器</p >
 *
 * @author zhenglecheng
 * @date 2019/12/27
 */
public class CommonParamInterceptor implements Interceptor {

    private HashMap<String, Object> mCommonParams = new HashMap<>();

    public CommonParamInterceptor() {
        // 添加通用参数
        mCommonParams.put("id", "");
        mCommonParams.put("token", "");
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        if (TextUtils.equals(request.method(), "GET")) {
            Request newRequest = getAddParams(request);
            return chain.proceed(newRequest);
        } else if (request.body() instanceof FormBody) {
            Request newRequest = postAddParams(request);
            return chain.proceed(newRequest);
        } else {
            Request newRequest = gsonAddParams(request);
            return chain.proceed(newRequest == null ? request : newRequest);
        }
    }

    /**
     * get方式添加通用参数
     *
     * @param request 原始请求
     */
    private Request getAddParams(Request request) {
        HttpUrl.Builder builder = request.url().newBuilder();
        for (String key : mCommonParams.keySet()) {
            builder.addQueryParameter(key, mCommonParams.get(key) + "");
        }
        HttpUrl httpUrl = builder.build();
        return request.newBuilder()
                .url(httpUrl)
                .build();
    }


    /**
     * post方式 form表单添加通用参数
     *
     * @param request 原始请求
     */
    private Request postAddParams(Request request) {
        FormBody.Builder builder = new FormBody.Builder();
        FormBody body = (FormBody) request.body();
        // 添加通用参数
        for (String key : mCommonParams.keySet()) {
            builder.add(key, mCommonParams.get(key) + "");
        }
        // 以前的参数添加
        if (body != null) {
            for (int i = 0; i < body.size(); i++) {
                if (!mCommonParams.containsKey(body.name(i))) {
                    builder.add(body.encodedName(i), body.encodedValue(i));
                }
            }
        }
        return request.newBuilder()
                .post(builder.build())
                .build();
    }

    /**
     * gson 方式添加参数
     */
    private Request gsonAddParams(Request request) {
        Gson gson = new Gson();
        Buffer buffer = new Buffer();
        if (request.body() == null) {
            return null;
        }
        try {
            request.body().writeTo(buffer);
            String paramStr = buffer.readUtf8();
            HashMap map = gson.fromJson(paramStr, HashMap.class);
            if (!map.isEmpty() && !mCommonParams.isEmpty()) {
                for (String key : mCommonParams.keySet()) {
                    if (!map.containsKey(key)) {
                        map.put(key, mCommonParams.get(key) + "");
                    }
                }
            }
            String json = gson.toJson(map);
            MediaType type = MediaType.parse("application/json; charset=UTF-8");
            RequestBody requestBody = RequestBody.create(type, json);
            return new Request.Builder()
                    .url(request.url())
                    .post(requestBody)
                    .build();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

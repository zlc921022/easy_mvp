package com.xiaochen.emvp.data.interceptor;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.concurrent.TimeUnit;

import okhttp3.Connection;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.internal.http.HttpHeaders;
import okhttp3.internal.platform.Platform;
import okio.Buffer;
import okio.BufferedSource;

import static okhttp3.internal.platform.Platform.INFO;

/**
 * okhttp日志拦截器
 */
@SuppressWarnings("all")
public final class HttpLogerInterceptor implements Interceptor {
    private static final Charset UTF8 = Charset.forName("UTF-8");

    public enum Level {
        //不显示日志
        NONE,
        //显示Content-Type 和 Content-Length等信息
        HEADERS,
        //显示所有的日志信息
        BODY
    }

    public interface Logger {
        void log(String message);

        /**
         * A {@link HttpLogerInterceptor.Logger} defaults output appropriate for the current platform.
         */
        HttpLogerInterceptor.Logger DEFAULT = new HttpLogerInterceptor.Logger() {
            @Override
            public void log(String message) {
                Platform.get().log(INFO, message, null);
            }
        };
    }

    public HttpLogerInterceptor() {
        this(HttpLogerInterceptor.Logger.DEFAULT);
    }

    public HttpLogerInterceptor(HttpLogerInterceptor.Logger logger) {
        this.logger = logger;
    }

    private final HttpLogerInterceptor.Logger logger;

    //默认不打印日志
    private volatile HttpLogerInterceptor.Level level = HttpLogerInterceptor.Level.NONE;

    //设置日志打印级别
    public HttpLogerInterceptor setLevel(HttpLogerInterceptor.Level level) {
        if (level == null) {
            throw new NullPointerException("level == null. Use Level.NONE instead.");
        }
        this.level = level;
        return this;
    }

    public HttpLogerInterceptor.Level getLevel() {
        return level;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {

        HttpLogerInterceptor.Level level = this.level;
        Request request = chain.request();
        final String requestContentType = request.header("Content-Type");
        //NONE日志级别或者上传文件
        if (level == HttpLogerInterceptor.Level.NONE || (requestContentType != null
                && requestContentType.equals("multipart/form-data"))) {
            return chain.proceed(request);
        }

        //判断日志级别
        boolean logBody = level == HttpLogerInterceptor.Level.BODY;
        boolean logHeaders = logBody || level == HttpLogerInterceptor.Level.HEADERS;

        //获取请求主体对象
        RequestBody requestBody = request.body();
        boolean hasRequestBody = requestBody != null;

        //--------------------------------------请求信息---------------------------------------------
        //获取连接对象
        Connection connection = chain.connection();
        Protocol protocol = connection != null ? connection.protocol() : Protocol.HTTP_1_1;
        logger.log("--------------------------------请求开始--------------------------------------");
        String requestStartMessage = "--> " + request.method() + ' ' + request.url() + ' ' + protocol;
        //BODY级别日志才获取requestBody的长度
        if (!logHeaders && hasRequestBody) {
            requestStartMessage += " (" + requestBody.contentLength() + "-byte body)";
        }
        logger.log(requestStartMessage);

        if (logHeaders) {
            //判断requestBody是否为空
            if (hasRequestBody) {
                if (requestBody.contentType() != null) {
                    logger.log("Content-Type: " + requestBody.contentType());
                }
                if (requestBody.contentLength() != -1) {
                    logger.log("Content-Length: " + requestBody.contentLength());
                }
            }

            //获取请求头部信息
            Headers headers = request.headers();
            StringBuffer headSb = new StringBuffer();
            for (int i = 0, count = headers.size(); i < count; i++) {
                String name = headers.name(i);
                if (!"Content-Type".equalsIgnoreCase(name) && !"Content-Length".equalsIgnoreCase(name)) {
                    headSb.append("\"" + name + "\":").append("\"" + headers.value(i) + "\",");
                }
            }
            if (headSb.length() > 0) {
                logger.log("header: { " + headSb.delete(headSb.length() - 1, headSb.length()) + "}");
            }

            //判断requestBody不为空，并且请求参数为POST时
            if (!logBody || !hasRequestBody) {
                logger.log("--> END " + request.method());
            } else {
                final StringBuilder sb = new StringBuilder();
                if (requestBody instanceof FormBody) {
                    FormBody body = (FormBody) requestBody;
                    for (int i = 0; i < body.size(); i++) {
                        sb.append(body.encodedName(i)).append(":")
                                .append(body.encodedValue(i))
                                .append(i != body.size() - 1 ? "," : "");
                    }
                    logger.log("Request: {" + sb.toString() + "}");
                } else {
                    Buffer buffer = new Buffer();
                    requestBody.writeTo(buffer);
                    logger.log("Request: " + buffer.readString(Charset.forName("UTF-8")));
                }
                logger.log("--> END " + request.method()
                        + " (" + requestBody.contentLength() + "-byte body)");
            }
        }

        //--------------------------------------响应信息---------------------------------------------
        //获取开始时间
        long startNs = System.nanoTime();
        Response response;
        try {
            response = chain.proceed(request);
        } catch (Exception e) {
            logger.log("<-- HTTP FAILED: " + e);
            throw e;
        }

        //计算总时间
        long tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs);
        //获取响应内容
        ResponseBody responseBody = response.body();
        long contentLength = responseBody.contentLength();
        String bodySize = contentLength != -1 ? contentLength + "-byte" : "unknown-length";
        logger.log("<-- " + response.code() + ' ' + response.message() + ' '
                + response.request().url() + " (" + tookMs + "ms" + (!logHeaders ? ", "
                + bodySize + " body" : "") + ')');

        //打印响应日志信息
        if (logHeaders) {

            //获取响应头部信息
            Headers headers = response.headers();
            for (int i = 0, count = headers.size(); i < count; i++) {
                if (headers.name(i).contains("Content")) {
                    logger.log(headers.name(i) + ": " + headers.value(i));
                }
            }

            if (!logBody || !HttpHeaders.hasBody(response)) {
                logger.log("<-- END HTTP");
            } else {

                BufferedSource source = responseBody.source();
                source.request(Long.MAX_VALUE); // Buffer the entire body.
                Buffer buffer = source.buffer();

                Charset charset = UTF8;
                MediaType contentType = responseBody.contentType();
                if (contentType != null) {
                    try {
                        charset = contentType.charset(UTF8);
                    } catch (UnsupportedCharsetException e) {
                        logger.log("");
                        logger.log("Couldn't decode the response body; charset is likely malformed.");
                        logger.log("<-- END HTTP");
                        return response;
                    }
                }

                if (contentLength != 0) {
                    logger.log("");
                    logger.log("Response: " + buffer.clone().readString(charset));
                }

                logger.log("<-- END HTTP (" + buffer.size() + "-byte body)");
            }
        }
        logger.log("--------------------------------响应结束--------------------------------------");
        return response;
    }
}
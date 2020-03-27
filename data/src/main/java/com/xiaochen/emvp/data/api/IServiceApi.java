package com.xiaochen.emvp.data.api;

import com.xiaochen.emvp.data.response.HomeArticleRespVO;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * <p>服务器接口{d}</p>
 *
 * @author zhenglecheng
 * @date 2019-12-08
 */
public interface IServiceApi {

    /**
     * 首页文章列表
     */
    @GET("article/list/{page}/json")
    Observable<HomeArticleRespVO> getHomeArticles(@Path("page") int page);
}

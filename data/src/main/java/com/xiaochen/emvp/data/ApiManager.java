package com.xiaochen.emvp.data;

/**
 * Api的管理类，负责创建api接口对象
 */
public class ApiManager {

    private HttpManager mHttpProvider;

    private ApiManager() {
        mHttpProvider = HttpManager.getProvider();
    }

    public static ApiManager getManager() {
        return SingleHolder.INSTANCE;
    }

    private final static class SingleHolder {
        private final static ApiManager INSTANCE = new ApiManager();
    }

    /**
     * 获得api接口对象
     *
     * @param service
     * @param <T>
     * @return
     */
    public <T> T createApi(Class<T> service) {
        return mHttpProvider.createApi(service);
    }
}

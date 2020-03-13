package com.tourcoo.training.core.retrofit.repository;



import com.tourcoo.training.core.base.entity.BaseMovieEntity;
import com.tourcoo.training.core.base.entity.BaseResult;
import com.tourcoo.training.core.retrofit.BaseLoadingObserver;
import com.tourcoo.training.core.retrofit.CommonTransformer;
import com.tourcoo.training.core.retrofit.RetrofitHelper;
import com.tourcoo.training.core.retrofit.RetryWhen;
import com.tourcoo.training.core.retrofit.service.ApiService;
import com.tourcoo.training.core.util.ToastUtil;
import com.trello.rxlifecycle3.android.ActivityEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;

/**
 * @Author: JenkinsZhou on 2018/11/19 14:25
 * @E-Mail: 971613168@qq.com
 * @Function: Retrofit api调用示例
 * @Description:
 */
public class ApiRepository extends BaseRepository {

    private static volatile ApiRepository instance;
    private ApiService mApiService;

    private ApiRepository() {
        mApiService = getApiService();
    }

    public static ApiRepository getInstance() {
        if (instance == null) {
            synchronized (ApiRepository.class) {
                if (instance == null) {
                    instance = new ApiRepository();
                }
            }
        }
        return instance;
    }

    private ApiService getApiService() {
        mApiService = RetrofitHelper.getInstance().createService(ApiService.class);
        return mApiService;
    }




    /**
     * 获取电影列表
     *
     * @param start 起始 下标
     * @param count 请求总数量
     * @return
     */
    public Observable<BaseMovieEntity> getMovie(int start, int count) {
        Map<String, Object> params = new HashMap<>(3);
        params.put("apikey","0b2bdeda43b5688921839c8ecb20399b");
        params.put("start", start);
        params.put("count", count);
        return CommonTransformer.switchSchedulers(getApiService().getMovie("v2/movie/top250", params).retryWhen(new RetryWhen()));
    }


    public Observable<BaseResult<List<Object>>> requestTradeType() {
        return CommonTransformer.switchSchedulers(getApiService().requestTradeType().retryWhen(new RetryWhen()));
    }
}

package com.tourcoo.training.core.retrofit.service;


import com.tourcoo.training.core.base.entity.BaseMovieEntity;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

/**
 * @Author: JenkinsZhou on 2018/7/30 14:01
 * @E-Mail: 971613168@qq.com
 * Function: 接口定义
 * Description:
 */
public interface ApiService {

//      public static final String BASE_URL = "https://api.douban.com/";


    /**
     * 用户认证状态列表
     * @return
     */
    @GET("meituApi")
    Observable<String> requestAuthentication(@QueryMap Map<String, Object> map);


    /**
     * 获取电影数据
     *
     * @param url
     * @param map
     * @return
     */
    @GET("{url}")
    Observable<BaseMovieEntity> getMovie(@Path("url") String url, @QueryMap Map<String, Object> map);
}

package com.tourcoo.training.core.retrofit.service;


import com.tourcoo.training.core.base.entity.BaseMovieEntity;
import com.tourcoo.training.core.base.entity.BaseResult;
import com.tourcoo.training.entity.account.IdCardInfo;
import com.tourcoo.training.entity.account.TradeType;
import com.tourcoo.training.entity.account.UserInfo;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
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
     *
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


    /**
     * 获取行业类型
     *
     * @return
     */
    @GET("api/v1.0/open/config/get-trade-type")
    Observable<BaseResult<List<TradeType>>> requestTradeType();

    /**
     * 个体工商户注册
     *
     * @return
     */
    @POST("api/v1.0/open/user/register-individual-business")
    Observable<BaseResult<UserInfo>> requestIndustryRegister(@Body Map<String, Object> map);


    @GET("api/v1.0/open/common/smscode")
    Observable<BaseResult> requestVCode(@QueryMap Map<String, Object> map);


    @POST("api/v1.0/open/user/reset-password")
    Observable<BaseResult> requestResetPass(@Body Map<String, Object> map);




    /**
     * 多个文件上传
     *
     * @param files
     * @return
     */
    @POST("v1.0/open/utils/idcard-recognition")
    Call<BaseResult<IdCardInfo>> requestIdCardRecognition(@Body RequestBody files);


}

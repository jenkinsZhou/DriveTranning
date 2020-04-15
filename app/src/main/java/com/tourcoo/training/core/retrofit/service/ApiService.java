package com.tourcoo.training.core.retrofit.service;


import com.tourcoo.training.core.base.entity.BaseMovieEntity;
import com.tourcoo.training.core.base.entity.BasePageResult;
import com.tourcoo.training.core.base.entity.BaseResult;
import com.tourcoo.training.core.retrofit.TokenInterceptor;
import com.tourcoo.training.entity.account.IdCardInfo;
import com.tourcoo.training.entity.account.TradeType;
import com.tourcoo.training.entity.account.UserInfo;
import com.tourcoo.training.entity.account.register.BusinessLicenseInfo;
import com.tourcoo.training.entity.account.register.CompanyInfo;
import com.tourcoo.training.entity.account.register.IndustryCategory;
import com.tourcoo.training.entity.certificate.CertificateInfo;
import com.tourcoo.training.entity.course.CourseInfo;
import com.tourcoo.training.entity.exam.ExamEntity;
import com.tourcoo.training.entity.recognize.FaceRecognizeResult;

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

import static com.tourcoo.training.core.retrofit.TokenInterceptor.HEADER_NOT_SKIP_LOGIN;
import static com.tourcoo.training.core.retrofit.TokenInterceptor.HEADER_SKIP_LOGIN;

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
   /* @POST("api/v1.0/open/user/register-individual-business")
    Observable<BaseResult<UserInfoOld>> requestIndustryRegister(@Body Map<String, Object> map);*/
    @GET("api/v1.0/open/common/smscode")
    Observable<BaseResult> requestVCode(@QueryMap Map<String, Object> map);


    @POST("api/v1.0/open/user/reset-password")
    Observable<BaseResult> requestResetPass(@Body Map<String, Object> map);


    /**
     * 身份证识别
     *
     * @param files
     * @return
     */
    @POST("v1.0/open/utils/idcard-recognition")
    Call<BaseResult<IdCardInfo>> requestIdCardRecognition(@Body RequestBody files);

    /**
     * 身份证识别
     *
     * @param files
     * @return
     */
    @POST("v1.0/open/utils/business-license-recognition")
    Call<BaseResult<BusinessLicenseInfo>> requestBusinessRecognition(@Body RequestBody files);

    /**
     * 公司模糊匹配
     *
     * @param map
     * @return
     */
    @POST("v1.0/open/utils/list-company-by-needle")
    Observable<BaseResult<List<CompanyInfo>>> requestCompanyByKeyword(@Body Map<String, Object> map);

    /**
     * 驾驶员注册
     *
     * @param map
     * @return
     */
    @POST("v1.0/open/User/register-driver")
    Observable<BaseResult<UserInfo>> requestRegisterDriver(@Body Map<String, Object> map);

    @POST("v1.0/open/utils/get-industry-category")
    Observable<BaseResult<List<IndustryCategory>>> requestCategory();

    @POST("v1.0/open/user/register-individual-business")
    Observable<BaseResult<Object>> requestRegisterIndustry(@Body Map<String, Object> map);

    @POST("v1.0/open/user/id-card-login")
    Observable<BaseResult<UserInfo>> requestLoginByIdCard(@Body Map<String, Object> map);


    @Headers({TokenInterceptor.HEADER_NEED_TOKEN, HEADER_NOT_SKIP_LOGIN})
    @POST("v1.0/open/trainee/details")
    Observable<BaseResult<UserInfo>> requestUserInfo();


    @Headers({TokenInterceptor.HEADER_NEED_TOKEN, HEADER_NOT_SKIP_LOGIN})
    @POST("v1.0/training/list-online-training-present-month-plan")
    Observable<BaseResult<List<CourseInfo>>> requestOnLineTrainingList();


    @Headers({TokenInterceptor.HEADER_NEED_TOKEN, HEADER_NOT_SKIP_LOGIN})
    @POST("v1.0/training/get-exam-detail")
    Observable<BaseResult<ExamEntity>> requestExam(@Body Map<String, Object> map);


    @Headers({TokenInterceptor.HEADER_NEED_TOKEN, HEADER_SKIP_LOGIN})
    @POST("v1.0/training/list-certificate")
    Observable<BasePageResult<CertificateInfo>> requestCertificate(@Body Map<String, Object> map);


    @Headers({TokenInterceptor.HEADER_NEED_TOKEN, HEADER_SKIP_LOGIN})
    @POST("v1.0/training/face-verify")
    Observable<BaseResult<FaceRecognizeResult>> requestFaceVerify(@Body Map<String, Object> map);

}

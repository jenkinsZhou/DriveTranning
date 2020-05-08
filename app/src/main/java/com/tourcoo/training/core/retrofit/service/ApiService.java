package com.tourcoo.training.core.retrofit.service;


import com.tourcoo.training.core.base.entity.BaseMovieEntity;
import com.tourcoo.training.core.base.entity.BasePageResult;
import com.tourcoo.training.core.base.entity.BaseResult;
import com.tourcoo.training.core.retrofit.TokenInterceptor;
import com.tourcoo.training.entity.account.IdCardInfo;
import com.tourcoo.training.entity.account.PayInfo;
import com.tourcoo.training.entity.account.TradeType;
import com.tourcoo.training.entity.account.UserInfo;
import com.tourcoo.training.entity.account.register.BusinessLicenseInfo;
import com.tourcoo.training.entity.account.register.CompanyInfo;
import com.tourcoo.training.entity.account.register.IndustryCategory;
import com.tourcoo.training.entity.certificate.CertificateInfo;
import com.tourcoo.training.entity.course.CourseInfo;
import com.tourcoo.training.entity.exam.ExamEntity;
import com.tourcoo.training.entity.exam.ExamResultEntity;
import com.tourcoo.training.entity.medal.StudyMedalEntity;
import com.tourcoo.training.entity.message.MessageDetail;
import com.tourcoo.training.entity.message.MessageEntity;
import com.tourcoo.training.entity.news.NewsDetailEntity;
import com.tourcoo.training.entity.news.NewsEntity;
import com.tourcoo.training.entity.order.OrderEntity;
import com.tourcoo.training.entity.pay.CoursePayInfo;
import com.tourcoo.training.entity.recognize.FaceRecognizeResult;
import com.tourcoo.training.entity.recharge.CoinPackageEntity;
import com.tourcoo.training.entity.setting.SettingEntity;
import com.tourcoo.training.entity.study.BannerBean;
import com.tourcoo.training.entity.study.StudyDataEntity;
import com.tourcoo.training.entity.study.StudyDetail;
import com.tourcoo.training.entity.study.StudyRecord;
import com.tourcoo.training.entity.training.DRMParams;
import com.tourcoo.training.entity.training.HlsParams;
import com.tourcoo.training.entity.training.ProfessionTrainingEntity;
import com.tourcoo.training.entity.training.ProfessionalTwoTypeModel;
import com.tourcoo.training.entity.training.TrainingPlanDetail;
import com.tourcoo.training.entity.training.TwoTypeModel;

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
    @POST("v1.0/open/utils/smscode")
    Observable<BaseResult> requestVCode(@QueryMap Map<String, Object> map);

    @Headers({TokenInterceptor.HEADER_NO_NEED_TOKEN, HEADER_NOT_SKIP_LOGIN})
    @POST("v1.0/open/user/reset-password")
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
     * 获取首页banner
     */
    @POST("v1.0/open/training/list-banner")
    Observable<BaseResult<List<BannerBean>>> requestListBanner();

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

    @POST("v1.0/training/get-payinfo")
    Observable<BaseResult<CoursePayInfo>> requestGetCoursePayInfo(@Body Map<String, Object> map);

    @POST("/v1.0/training/pay")
    Observable<BaseResult<PayInfo>> requestPayCourse(@Body Map<String, Object> map);

    @POST("v1.0/open/utils/get-industry-category")
    Observable<BaseResult<List<IndustryCategory>>> requestCategory();

    @POST("v1.0/user/set-industry-category-id")
    Observable<BaseResult> setIndustryCategory(@Body Map<String, Object> map);

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
    @POST("v1.0/training/list-before-the-post-training-present-month-plan")
    Observable<BaseResult<List<CourseInfo>>> requestBeforeThePostTrainingList();

    @Headers({TokenInterceptor.HEADER_NEED_TOKEN, HEADER_NOT_SKIP_LOGIN})
    @POST("v1.0/training/list-offline-training-present-month-plan")
    Observable<BaseResult<List<CourseInfo>>> requestOffLineTrainingList();


    @Headers({TokenInterceptor.HEADER_NEED_TOKEN, HEADER_NOT_SKIP_LOGIN})
    @POST("v1.0/training/get-exam-detail")
    Observable<BaseResult<ExamEntity>> requestExam(@Body Map<String, Object> map);

    @Headers({TokenInterceptor.HEADER_NEED_TOKEN, HEADER_NOT_SKIP_LOGIN})
    @POST("v1.0/open/Special/list-special-training-present-month-plan")
    Observable<BaseResult<TwoTypeModel>> requestTwoTypeDetailsList(@Body Map<String, Object> map);


    @Headers({TokenInterceptor.HEADER_NEED_TOKEN, HEADER_NOT_SKIP_LOGIN})
    @POST("v1.0/open/Special/list-special-training-by-module-id")
    Observable<BaseResult<List<ProfessionalTwoTypeModel>>> requestTwoType(@Body Map<String, Object> map);


    @Headers({TokenInterceptor.HEADER_NEED_TOKEN, HEADER_SKIP_LOGIN})
    @POST("v1.0/training/list-certificate")
    Observable<BasePageResult<CertificateInfo>> requestCertificate(@Body Map<String, Object> map);

    @Headers({TokenInterceptor.HEADER_NEED_TOKEN, HEADER_NOT_SKIP_LOGIN})
    @POST("v1.0/open/Special/list-special-training-module")
    Observable<BasePageResult<ProfessionTrainingEntity>> requestProfessionTraining(@Body Map<String, Object> map);


    @Headers({TokenInterceptor.HEADER_NEED_TOKEN, HEADER_SKIP_LOGIN})
    @POST("v1.0/training/face-verify")
    Observable<BaseResult<FaceRecognizeResult>> requestFaceVerify(@Body Map<String, Object> map);


    @Headers({TokenInterceptor.HEADER_NEED_TOKEN, HEADER_SKIP_LOGIN})
    @POST("v1.0/user/face-and-idcard-verify")
    Observable<BaseResult<FaceRecognizeResult>> requestIdCardVerify(@Body Map<String, Object> map);


    /**
     * 学币套餐列表
     *
     * @return
     */
    @Headers({TokenInterceptor.HEADER_NEED_TOKEN, HEADER_SKIP_LOGIN})
    @POST("v1.0/trainee/list-coin-package")
    Observable<BaseResult<CoinPackageEntity>> requestCoinPackage();


    /**
     * 充值
     *
     * @param map
     * @return
     */
    @Headers({TokenInterceptor.HEADER_NEED_TOKEN, HEADER_SKIP_LOGIN})
    @POST("v1.0/trainee/recharge")
    Observable<BaseResult<PayInfo>> requestRecharge(@Body Map<String, Object> map);


    /**
     * 交卷接口
     */
    @Headers({TokenInterceptor.HEADER_NEED_TOKEN, HEADER_SKIP_LOGIN})
    @POST("v1.0/training/finish-exam")
    Observable<BaseResult<ExamResultEntity>> requestFinishExam(@Body Map<String, Object> map);

    /**
     * 专项交卷接口
     */
    @Headers({TokenInterceptor.HEADER_NEED_TOKEN, HEADER_SKIP_LOGIN})
    @POST("v1.0/Special/finish-exam")
    Observable<BaseResult<ExamResultEntity>> requestProfessionalFinishExam(@Body Map<String, Object> map);

    /**
     * 保存考试状态
     */
    @Headers({TokenInterceptor.HEADER_NEED_TOKEN, HEADER_SKIP_LOGIN})
    @POST("v1.0/training/save-exam-answers")
    Observable<BaseResult> requestSaveAnswer(@Body Map<String, Object> map);

    /**
     * 保存专项考试状态
     */
    @Headers({TokenInterceptor.HEADER_NEED_TOKEN, HEADER_SKIP_LOGIN})
    @POST("v1.0/Special/save-exam-answers")
    Observable<BaseResult> requestProfessionalSaveAnswer(@Body Map<String, Object> map);

    /**
     * 上传证书图片
     */
    @Headers({TokenInterceptor.HEADER_NEED_TOKEN, HEADER_SKIP_LOGIN})
    @POST("v1.0/open/training/upload-certificate")
    Observable<BaseResult> uploadCertificate(@Body Map<String, Object> map);


    @Headers({TokenInterceptor.HEADER_NEED_TOKEN, HEADER_SKIP_LOGIN})
    @POST("v1.0/user/logout")
    Observable<BaseResult> requestLogout();


    @Headers({TokenInterceptor.HEADER_NEED_TOKEN, HEADER_SKIP_LOGIN})
    @POST("v1.0/training/get-training-plan-detail2")
    Observable<BaseResult<TrainingPlanDetail>> requestPlanDetail(@Body Map<String, Object> map);


    @Headers({TokenInterceptor.HEADER_NEED_TOKEN, HEADER_SKIP_LOGIN})
    @POST("v1.0/training/save-progress")
    Observable<BaseResult> requestSaveProgress(@Body Map<String, Object> map);

    @Headers({TokenInterceptor.HEADER_NEED_TOKEN, HEADER_SKIP_LOGIN})
    @POST("v1.0/vod/get-drivedu-token")
    Observable<BaseResult<DRMParams>> requestVideoEncryptParamsCurrentCourse(@Body Map<String, Object> map);

    /**
     * HLS解密接口
     *
     * @param map
     * @return
     */
    @Headers({TokenInterceptor.HEADER_NEED_TOKEN, HEADER_SKIP_LOGIN})
    @POST("v1.0/vod/get-video-token-and-playauth")
    Observable<BaseResult<HlsParams>> requestHlsEncryptParams(@Body Map<String, Object> map);

    /**
     * 转线上
     *
     * @param map
     * @return
     */
    @Headers({TokenInterceptor.HEADER_NEED_TOKEN, HEADER_SKIP_LOGIN})
    @POST("v1.0/training/start-online-stage")
    Observable<BaseResult> requestTurnOnline(@Body Map<String, Object> map);

    @Headers({TokenInterceptor.HEADER_NO_NEED_TOKEN})
    @POST("v1.0/open/news/list-news")
    Observable<BasePageResult<NewsEntity>> requestNewsList(@Body Map<String, Object> map);


    @Headers({TokenInterceptor.HEADER_NO_NEED_TOKEN})
    @POST("v1.0/open/news/get-news-detail")
    Observable<BaseResult<NewsDetailEntity>> requestNewsDetail(@Body Map<String, Object> map);

    @Headers({TokenInterceptor.HEADER_NO_NEED_TOKEN})
    @POST("v1.0/open/Individual/individual-business-payinfo-pay")
    Observable<BaseResult> requestBusinessPayInfo(@Body Map<String, Object> map);

    @Headers({TokenInterceptor.HEADER_NO_NEED_TOKEN})
    @POST("v1.0/open/Special/special-payinfo-pay")
    Observable<BaseResult> requestTwoPayInfo(@Body Map<String, Object> map);

    @POST("v1.0/Special/get-exam-detail")
    Observable<BaseResult<ExamEntity>> requestProfessionalExamInfo(@Body Map<String, Object> map);


    @Headers({TokenInterceptor.HEADER_NO_NEED_TOKEN})
    @POST("v1.0/open/app/system-config")
    Observable<BaseResult<SettingEntity>> requestSystemConfig();

    /**
     * 勋章列表
     *
     * @return
     */
    @Headers({TokenInterceptor.HEADER_NO_NEED_TOKEN})
    @POST("v1.0/user/list-medal")
    Observable<BaseResult<StudyMedalEntity>> requestStudyMedalList();

    /**
     * 点赞
     *
     * @return
     */
    @Headers({TokenInterceptor.HEADER_NEED_TOKEN})
    @POST("v1.0/open/news/like-news")
    Observable<BaseResult> requestNewsLike();


    /**
     * 学习记录列表
     *
     * @return
     */
    @Headers({TokenInterceptor.HEADER_NEED_TOKEN})
    @POST("v1.0/training/list_study")
    Observable<BaseResult<List<StudyRecord>>> requestStudyRecordList(@Body Map<String, Object> map);

    /**
     * 学习详情
     * @param map
     * @return
     */
    @Headers({TokenInterceptor.HEADER_NEED_TOKEN})
    @POST("v1.0/training/get-study-detail")
    Observable<BaseResult<StudyDetail>> requestStudyDetail(@Body Map<String, Object> map);

    /**
     * 学习数据
     */
    @Headers({TokenInterceptor.HEADER_NEED_TOKEN})
    @POST("v1.0/training/list-study-statistics")
    Observable<BaseResult<StudyDataEntity>> requestStudyDataList(@Body Map<String, Object> map);

    /**
     * 订单列表
     * @param map
     * @return
     */
    @Headers({TokenInterceptor.HEADER_NEED_TOKEN, HEADER_SKIP_LOGIN})
    @POST("v1.0/training/list-order")
    Observable<BaseResult<List<OrderEntity>>> requestOrderList(@Body Map<String, Object> map);


    /**
     * 消息列表
     * @param map
     * @return
     */
    @Headers({TokenInterceptor.HEADER_NEED_TOKEN, HEADER_SKIP_LOGIN})
    @POST("v1.0/notice/list-notice")
    Observable<BasePageResult<MessageEntity>> requestMessageList(@Body Map<String, Object> map);


    @Headers({TokenInterceptor.HEADER_NEED_TOKEN, HEADER_SKIP_LOGIN})
    @POST("v1.0/notice/get-notice-detail")
    Observable<BaseResult<MessageDetail>> requestMessageDetail(@Body Map<String, Object> map);

}

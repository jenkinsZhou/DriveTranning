package com.tourcoo.training.core.retrofit.repository;


import com.blankj.utilcode.util.AppUtils;
import com.tourcoo.training.core.base.entity.BaseMovieEntity;
import com.tourcoo.training.core.base.entity.BaseResult;
import com.tourcoo.training.core.log.TourCooLogUtil;
import com.tourcoo.training.core.retrofit.CommonTransformer;
import com.tourcoo.training.core.retrofit.RetrofitHelper;
import com.tourcoo.training.core.retrofit.RetryWhen;
import com.tourcoo.training.core.retrofit.service.ApiService;
import com.tourcoo.training.entity.account.TradeType;
import com.tourcoo.training.entity.account.UserInfo;
import com.tourcoo.training.entity.account.register.CompanyInfo;
import com.tourcoo.training.entity.account.register.IndustryCategory;
import com.tourcoo.training.entity.course.CourseInfo;
import com.tourcoo.training.entity.exam.ExamEntity;
import com.tourcoo.training.utils.MapUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;

import static com.tourcoo.training.core.util.CommonUtil.getUniquePsuedoID;

/**
 * @Author: JenkinsZhou on 2018/11/19 14:25
 * @E-Mail: 971613168@qq.com
 * @Function: Retrofit api调用示例
 * @Description:
 */
public class ApiRepository extends BaseRepository {
    public static final String TAG = "ApiRepository";
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

    public ApiService getApiService() {
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
        params.put("apikey", "0b2bdeda43b5688921839c8ecb20399b");
        params.put("start", start);
        params.put("count", count);
        return CommonTransformer.switchSchedulers(getApiService().getMovie("v2/movie/top250", params).retryWhen(new RetryWhen()));
    }


    public Observable<BaseResult<List<TradeType>>> requestTradeType() {
        return CommonTransformer.switchSchedulers(getApiService().requestTradeType().retryWhen(new RetryWhen()));
    }

   /* public Observable<BaseResult<UserInfoOld>> requestIndustryRegister(Map<String, Object> map) {
        return CommonTransformer.switchSchedulers(getApiService().requestIndustryRegister(map).retryWhen(new RetryWhen()));
    }*/

    public Observable<BaseResult> requestVCode(int type, String phone) {
        Map<String, Object> params = new HashMap<>(3);
        params.put("phone", phone);
        params.put("type", type);
        return CommonTransformer.switchSchedulers(getApiService().requestVCode(params).retryWhen(new RetryWhen()));
    }

    public Observable<BaseResult> requestResetPass(String phone, String pass, String smsCode) {
        Map<String, Object> params = new HashMap<>(3);
        params.put("phoneNumber", phone);
        params.put("password", pass);
        params.put("smsCode", smsCode);
        return CommonTransformer.switchSchedulers(getApiService().requestResetPass(params).retryWhen(new RetryWhen()));
    }

    public Observable<BaseResult<List<CompanyInfo>>> requestCompanyByKeyword(String keyword) {
        Map<String, Object> params = new HashMap<>(1);
        params.put("needle", keyword);
        return CommonTransformer.switchSchedulers(getApiService().requestCompanyByKeyword(params).retryWhen(new RetryWhen()));
    }

    @SuppressWarnings("unchecked")
    public Observable<BaseResult<UserInfo>> requestRegisterDriver(Map<String, Object> map) {
        Map<String, Object> params = new HashMap<>(1);
        params.put("appType", "Android");
        params.put("appVersion", AppUtils.getAppVersionName());
        Map newMap = MapUtil.mergeMaps(params, map);
        TourCooLogUtil.i(TAG, newMap);
        return CommonTransformer.switchSchedulers(getApiService().requestRegisterDriver(newMap).retryWhen(new RetryWhen()));
    }

    public Observable<BaseResult<List<IndustryCategory>>> requestCategory() {
        return CommonTransformer.switchSchedulers(getApiService().requestCategory().retryWhen(new RetryWhen()));
    }

    @SuppressWarnings("unchecked")
    public Observable<BaseResult<Object>> requestRegisterIndustry(Map<String, Object> map) {
        Map<String, Object> params = new HashMap<>(1);
        params.put("appType", "Android");
        params.put("appVersion", AppUtils.getAppVersionName());
        Map newMap = MapUtil.mergeMaps(params, map);
        TourCooLogUtil.i(TAG, newMap);
        return CommonTransformer.switchSchedulers(getApiService().requestRegisterIndustry(newMap).retryWhen(new RetryWhen()));
    }

    public Observable<BaseResult<UserInfo>> requestLoginByIdCard(String idCard, String password) {
        Map<String, Object> params = new HashMap<>(5);
        params.put("idCard", idCard);
        params.put("password", password);
        params.put("appType", "Android");
        params.put("appVersion", AppUtils.getAppVersionName());
        params.put("deviceID", getUniquePsuedoID());
        params.put("deviceOS", android.os.Build.VERSION.RELEASE);
        TourCooLogUtil.i(TAG, params);
        return CommonTransformer.switchSchedulers(getApiService().requestLoginByIdCard(params).retryWhen(new RetryWhen()));
    }

    public Observable<BaseResult<UserInfo>> requestUserInfo() {
        return CommonTransformer.switchSchedulers(getApiService().requestUserInfo().retryWhen(new RetryWhen()));
    }

    public Observable<BaseResult<List<CourseInfo>>> requestOnLineTrainingList() {
        return CommonTransformer.switchSchedulers(getApiService().requestOnLineTrainingList().retryWhen(new RetryWhen()));
    }

    public Observable<BaseResult<ExamEntity>> requestExam(String trainingPlanID, String examId) {
        Map<String, Object> params = new HashMap<>(2);
        params.put("trainingPlanID", trainingPlanID);
        params.put("examId", examId);
        return CommonTransformer.switchSchedulers(getApiService().requestExam(params).retryWhen(new RetryWhen()));
    }

}

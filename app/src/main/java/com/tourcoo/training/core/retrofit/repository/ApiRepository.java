package com.tourcoo.training.core.retrofit.repository;


import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.SPUtils;
import com.tourcoo.training.core.base.entity.BaseMovieEntity;
import com.tourcoo.training.core.base.entity.BasePageResult;
import com.tourcoo.training.core.base.entity.BaseResult;
import com.tourcoo.training.core.log.TourCooLogUtil;
import com.tourcoo.training.core.retrofit.CommonTransformer;
import com.tourcoo.training.core.retrofit.RetrofitHelper;
import com.tourcoo.training.core.retrofit.RetryWhen;
import com.tourcoo.training.core.retrofit.service.ApiService;
import com.tourcoo.training.entity.account.PayInfo;
import com.tourcoo.training.entity.account.TradeType;
import com.tourcoo.training.entity.account.UserInfo;
import com.tourcoo.training.entity.account.register.CompanyInfo;
import com.tourcoo.training.entity.account.register.IndustryCategory;
import com.tourcoo.training.entity.certificate.CertificateInfo;
import com.tourcoo.training.entity.certificate.CertifyDetail;
import com.tourcoo.training.entity.course.CourseInfo;
import com.tourcoo.training.entity.exam.CommitAnswer;
import com.tourcoo.training.entity.exam.ExamEntity;
import com.tourcoo.training.entity.exam.ExamResultEntity;
import com.tourcoo.training.entity.feedback.FeedReasonEntity;
import com.tourcoo.training.entity.feedback.FeedbackCommitEntity;
import com.tourcoo.training.entity.medal.MedalDictionary;
import com.tourcoo.training.entity.medal.StudyMedalEntity;
import com.tourcoo.training.entity.message.MessageDetail;
import com.tourcoo.training.entity.message.MessageEntity;
import com.tourcoo.training.entity.news.NewsDetail;
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
import com.tourcoo.training.ui.update.AppUpdateInfo;
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
        String typeStr = "";
        switch (type) {
            case 0:
                typeStr = "changePhoneNumber";
                break;
            case 1:
            default:
                typeStr = "resetPassword";
                break;
        }

        params.put("phone", phone);
        params.put("type", typeStr);
        return CommonTransformer.switchSchedulers(getApiService().requestVCode(params).retryWhen(new RetryWhen()));
    }

    public Observable<BaseResult> requestResetPass(String phone, String pass, String smsCode) {
        Map<String, Object> params = new HashMap<>(3);
        params.put("phone", phone);
        params.put("password", pass);
        params.put("smsCode", smsCode);
        return CommonTransformer.switchSchedulers(getApiService().requestResetPass(params).retryWhen(new RetryWhen()));
    }

    public Observable<BaseResult<List<CompanyInfo>>> requestCompanyByKeyword(String keyword) {
        Map<String, Object> params = new HashMap<>(1);
        params.put("needle", keyword);
        return CommonTransformer.switchSchedulers(getApiService().requestCompanyByKeyword(params).retryWhen(new RetryWhen()));
    }


    public Observable<BaseResult<CoursePayInfo>> requestGetCoursePayInfo(String trainingPlanID) {
        Map<String, Object> params = new HashMap<>(1);
        params.put("trainingPlanID", trainingPlanID);
        return CommonTransformer.switchSchedulers(getApiService().requestGetCoursePayInfo(params).retryWhen(new RetryWhen()));
    }

    public Observable<BaseResult<PayInfo>> requestPayCourse(String trainingPlanID, int payType) {
        Map<String, Object> params = new HashMap<>(2);
        params.put("trainingPlanID", trainingPlanID);
        params.put("payType", payType);
        return CommonTransformer.switchSchedulers(getApiService().requestPayCourse(params).retryWhen(new RetryWhen()));
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

    public Observable<BaseResult> setIndustryCategory(String id) {
        Map<String, Object> params = new HashMap<>(1);
        params.put("id", id);
        return CommonTransformer.switchSchedulers(getApiService().setIndustryCategory(params).retryWhen(new RetryWhen()));
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

    public Observable<BaseResult<List<BannerBean>>> requesListBanner() {
        return CommonTransformer.switchSchedulers(getApiService().requestListBanner().retryWhen(new RetryWhen()));
    }


    public Observable<BaseResult<UserInfo>> requestUserInfo() {
        return CommonTransformer.switchSchedulers(getApiService().requestUserInfo().retryWhen(new RetryWhen()));
    }

    public Observable<BaseResult<List<CourseInfo>>> requestOnLineTrainingList() {
        return CommonTransformer.switchSchedulers(getApiService().requestOnLineTrainingList().retryWhen(new RetryWhen()));
    }

    public Observable<BaseResult<List<CourseInfo>>> requestBeforeThePostTrainingList() {
        return CommonTransformer.switchSchedulers(getApiService().requestBeforeThePostTrainingList().retryWhen(new RetryWhen()));
    }


    public Observable<BaseResult<List<CourseInfo>>> requestOffLineTrainingList() {
        return CommonTransformer.switchSchedulers(getApiService().requestOffLineTrainingList().retryWhen(new RetryWhen()));
    }

    public Observable<BaseResult<ExamEntity>> requestExam(String trainingPlanID, String examId) {
        Map<String, Object> params = new HashMap<>(2);
        params.put("trainingPlanID", trainingPlanID);
        params.put("examId", examId);
        return CommonTransformer.switchSchedulers(getApiService().requestExam(params).retryWhen(new RetryWhen()));
    }

    public Observable<BaseResult<List<ProfessionalTwoTypeModel>>> requestTwoType(String id) {
        Map<String, Object> params = new HashMap<>(2);
        params.put("ID", id);
        return CommonTransformer.switchSchedulers(getApiService().requestTwoType(params).retryWhen(new RetryWhen()));
    }


    public Observable<BasePageResult<CertificateInfo>> requestCertificate(int page) {
        Map<String, Object> params = new HashMap<>(2);
        if (page == 0) {
            page = 1;
        }
        params.put("page", page);
        //每次请求10条
        params.put("rows", 10);
        return CommonTransformer.switchSchedulers(getApiService().requestCertificate(params).retryWhen(new RetryWhen()));
    }

    public Observable<BaseResult<CertifyDetail>> requestCertificateDetail(String id) {
        Map<String, Object> params = new HashMap<>(1);
        params.put("id", id);
        return CommonTransformer.switchSchedulers(getApiService().requestCertificateDetail(params).retryWhen(new RetryWhen()));
    }


    public Observable<BaseResult<TwoTypeModel>> requestTwoTypeDetailsList(String ID, String childModuleId) {
        Map<String, Object> params = new HashMap<>(2);
        params.put("ID", ID);
        params.put("ChildModuleId", childModuleId);
        return CommonTransformer.switchSchedulers(getApiService().requestTwoTypeDetailsList(params).retryWhen(new RetryWhen()));
    }


    public Observable<BasePageResult<ProfessionTrainingEntity>> requestCourseProfessionalTraining(int page) {
        Map<String, Object> params = new HashMap<>(2);
        if (page == 0) {
            page = 1;
        }
        params.put("page", page);
        //每次请求10条
        params.put("rows", 10);
        return CommonTransformer.switchSchedulers(getApiService().requestProfessionTraining(params).retryWhen(new RetryWhen()));
    }


    public Observable<BaseResult<FaceRecognizeResult>> requestFaceVerify(String trainingPlanID, String photoBase64) {
        Map<String, Object> params = new HashMap<>(3);
        params.put("trainingPlanID", trainingPlanID);
        params.put("scene", 2);
        params.put("photo", photoBase64);
        return CommonTransformer.switchSchedulers(getApiService().requestFaceVerify(params).retryWhen(new RetryWhen()));
    }


    public Observable<BaseResult<FaceRecognizeResult>> requestIdCardVerify(String trainingPlanID, String idPhotoBase64, String facePhotoBase64) {
        Map<String, Object> params = new HashMap<>(2);
        params.put("idCardPhoto", idPhotoBase64);
        params.put("facePhoto", facePhotoBase64);
        return CommonTransformer.switchSchedulers(getApiService().requestIdCardVerify(params).retryWhen(new RetryWhen()));
    }

    /**
     * 培训相关的人脸验证
     *
     * @param params
     * @return
     */
    public Observable<BaseResult<FaceRecognizeResult>> requestTrainFaceVerify(Map<String, Object> params) {
        return CommonTransformer.switchSchedulers(getApiService().requestFaceVerify(params).retryWhen(new RetryWhen()));
    }

    public Observable<BaseResult<CoinPackageEntity>> requestCoinPackage() {
        return CommonTransformer.switchSchedulers(getApiService().requestCoinPackage().retryWhen(new RetryWhen()));
    }

    public Observable<BaseResult<PayInfo>> requestRecharge(String coinPackageID, int payType, String amount, String coinPackageCount) {
        Map<String, Object> params = new HashMap<>(4);
        params.put("coinPackageID", coinPackageID);
        params.put("amount", amount);
        params.put("payType", payType);
        params.put("coinPackageCount", coinPackageCount);
        return CommonTransformer.switchSchedulers(getApiService().requestRecharge(params).retryWhen(new RetryWhen()));
    }

    public Observable<BaseResult<ExamResultEntity>> requestFinishExam(String examId, List<CommitAnswer> commitAnswerList) {
        Map<String, Object> params = new HashMap<>(4);
        params.put("examId", examId);
        params.put("questions", JSON.toJSONString(commitAnswerList));
        return CommonTransformer.switchSchedulers(getApiService().requestFinishExam(params).retryWhen(new RetryWhen()));
    }

    public Observable<BaseResult<ExamResultEntity>> requestProfessionalFinishExam(String examId, List<CommitAnswer> commitAnswerList) {
        Map<String, Object> params = new HashMap<>(2);
        params.put("examId", examId);
        params.put("questions", JSON.toJSONString(commitAnswerList));
        return CommonTransformer.switchSchedulers(getApiService().requestProfessionalFinishExam(params).retryWhen(new RetryWhen()));
    }

    public Observable<BaseResult> requestProfessionalSaveAnswer(String examId, List<CommitAnswer> commitAnswerList) {
        Map<String, Object> params = new HashMap<>(4);
        params.put("examId", examId);
//        params.put("questions", commitAnswerList);
        params.put("questions", JSON.toJSONString(commitAnswerList));
        return CommonTransformer.switchSchedulers(getApiService().requestProfessionalSaveAnswer(params).retryWhen(new RetryWhen()));
    }

    public Observable<BaseResult> requestSaveAnswer(String examId, List<CommitAnswer> commitAnswerList) {
        Map<String, Object> params = new HashMap<>(4);
        params.put("examId", examId);
        params.put("questions", JSON.toJSONString(commitAnswerList));
        return CommonTransformer.switchSchedulers(getApiService().requestSaveAnswer(params).retryWhen(new RetryWhen()));
    }

    public Observable<BaseResult> uploadCertificate(String certificateId, String image) {
        Map<String, Object> params = new HashMap<>(2);
        params.put("certificateId", certificateId);
        params.put("image", image);
        return CommonTransformer.switchSchedulers(getApiService().uploadCertificate(params).retryWhen(new RetryWhen()));
    }

    public Observable<BaseResult> requestLogout() {
        return CommonTransformer.switchSchedulers(getApiService().requestLogout().retryWhen(new RetryWhen()));
    }

    public Observable<BaseResult<TrainingPlanDetail>> trainingPlanID(String trainingPlanID) {
        Map<String, Object> params = new HashMap<>(1);
        params.put("trainingPlanID", trainingPlanID);
        return CommonTransformer.switchSchedulers(getApiService().requestPlanDetail(params).retryWhen(new RetryWhen()));
    }


    /* https://api.ggjtaq.com/v1.0/training/save-progress
    request: {trainingPlanID, courseID, progress}*/
    public Observable<BaseResult> requestSaveProgress(String trainingPlanID, String courseID, String progress) {
        Map<String, Object> params = new HashMap<>(3);
        params.put("trainingPlanID", trainingPlanID);
        params.put("courseID", courseID);
        params.put("progress", progress);
        return CommonTransformer.switchSchedulers(getApiService().requestSaveProgress(params).retryWhen(new RetryWhen()));
    }


    public Observable<BaseResult<DRMParams>> requestVideoEncryptParamsCurrentCourse(String trainingPlanID, String videoID) {
        Map<String, Object> params = new HashMap<>(2);
        params.put("TrainingPlanID", trainingPlanID);
        params.put("VideoID", videoID);
        return CommonTransformer.switchSchedulers(getApiService().requestVideoEncryptParamsCurrentCourse(params).retryWhen(new RetryWhen()));
    }

    public Observable<BaseResult<HlsParams>> requestHlsEncryptParams(String trainingPlanID, String videoID) {
        Map<String, Object> params = new HashMap<>(2);
        params.put("TrainingPlanID", trainingPlanID);
        params.put("VideoID", videoID);
        return CommonTransformer.switchSchedulers(getApiService().requestHlsEncryptParams(params).retryWhen(new RetryWhen()));
    }

    /**
     * 转线上
     *
     * @param trainingPlanID
     * @return
     */
    public Observable<BaseResult> requestTurnOnline(String trainingPlanID) {
        Map<String, Object> params = new HashMap<>(1);
        params.put("trainingPlanID", trainingPlanID);
        return CommonTransformer.switchSchedulers(getApiService().requestTurnOnline(params).retryWhen(new RetryWhen()));
    }


    public Observable<BasePageResult<NewsEntity>> requestNewsList(int page) {
        Map<String, Object> params = new HashMap<>(2);
        if (page == 0) {
            page = 1;
        }
        params.put("page", page);
        //每次请求10条
        params.put("rows", 10);
        return CommonTransformer.switchSchedulers(getApiService().requestNewsList(params).retryWhen(new RetryWhen()));
    }


    /**
     * 个体工商户购买课时接口
     *
     * @param num
     * @return
     */
    public Observable<BaseResult> requestBusinessPayInfo(int num) {
        Map<String, Object> params = new HashMap<>(1);
        params.put("num", num);
        return CommonTransformer.switchSchedulers(getApiService().requestBusinessPayInfo(params).retryWhen(new RetryWhen()));
    }

    public Observable<BaseResult<SettingEntity>> requestSystemConfig() {
        return CommonTransformer.switchSchedulers(getApiService().requestSystemConfig().retryWhen(new RetryWhen()));
    }


    public Observable<BaseResult> requestTwoPayInfo(String ID, String childModuleId, String coins) {
        Map<String, Object> params = new HashMap<>(3);
        params.put("ID", ID);
        params.put("ChildModuleId", childModuleId);
        params.put("Coins", coins);
        return CommonTransformer.switchSchedulers(getApiService().requestTwoPayInfo(params).retryWhen(new RetryWhen()));
    }

    public Observable<BaseResult<ExamEntity>> requestProfessionalExamInfo(String trainingPlanID, int type, String examId) {
        Map<String, Object> params = new HashMap<>(2);
        params.put("trainingPlanID", trainingPlanID);
        params.put("type", type);
        //正式考试需要传examId
        if (type == 0) {
            params.put("examId", examId);
        }
        return CommonTransformer.switchSchedulers(getApiService().requestProfessionalExamInfo(params).retryWhen(new RetryWhen()));
    }


    public Observable<BaseResult<StudyMedalEntity>> requestStudyMedalList() {
        return CommonTransformer.switchSchedulers(getApiService().requestStudyMedalList().retryWhen(new RetryWhen()));
    }


    public Observable<BaseResult<NewsDetail>> requestNewsDetail(String newsId) {
        Map<String, Object> params = new HashMap<>(1);
        params.put("id", newsId);
        params.put("TraineeID", SPUtils.getInstance().getString("TraineeID"));
        return CommonTransformer.switchSchedulers(getApiService().requestNewsDetail(params).retryWhen(new RetryWhen()));
    }

    public Observable<BaseResult> requestNewsLike(String newsId, boolean like) {
        Map<String, Object> params = new HashMap<>(2);
        params.put("id", newsId);
        if (like) {
            params.put("isLike", 1);
        } else {
            params.put("isLike", 0);
        }
        return CommonTransformer.switchSchedulers(getApiService().requestNewsLike(params).retryWhen(new RetryWhen()));
    }


    public Observable<BaseResult<List<StudyRecord>>> requestStudyRecordList(String year) {
        Map<String, Object> params = new HashMap<>(1);
        params.put("year", year);
        return CommonTransformer.switchSchedulers(getApiService().requestStudyRecordList(params).retryWhen(new RetryWhen()));
    }

    public Observable<BaseResult<StudyDetail>> requestStudyDetail(String trainingPlanID) {
        Map<String, Object> params = new HashMap<>(1);
        params.put("trainingPlanID", trainingPlanID);
        params.put("TraineeID", SPUtils.getInstance().getString("TraineeID"));
        return CommonTransformer.switchSchedulers(getApiService().requestStudyDetail(params).retryWhen(new RetryWhen()));
    }


    public Observable<BaseResult<StudyDataEntity>> requestStudyDataList(String year) {
        Map<String, Object> params = new HashMap<>(1);
        params.put("year", year);
        return CommonTransformer.switchSchedulers(getApiService().requestStudyDataList(params).retryWhen(new RetryWhen()));
    }


    public Observable<BaseResult<List<OrderEntity>>> requestOrderList(int page, int type) {
        Map<String, Object> params = new HashMap<>(2);
        if (page == 0) {
            page = 1;
        }
        params.put("page", page);
        //每次请求10条
        params.put("rows", 10);
        params.put("type", type);
        return CommonTransformer.switchSchedulers(getApiService().requestOrderList(params).retryWhen(new RetryWhen()));
    }

    public Observable<BasePageResult<MessageEntity>> requestMessageList(int page, int type) {
        Map<String, Object> params = new HashMap<>(3);
        if (page == 0) {
            page = 1;
        }
        params.put("page", page);
        //每次请求10条
        params.put("rows", 10);
        params.put("type", type);
        return CommonTransformer.switchSchedulers(getApiService().requestMessageList(params).retryWhen(new RetryWhen()));
    }


    public Observable<BaseResult<MessageDetail>> requestMessageDetail(int id) {
        Map<String, Object> params = new HashMap<>(1);
        params.put("id", id);
        return CommonTransformer.switchSchedulers(getApiService().requestMessageDetail(params).retryWhen(new RetryWhen()));
    }

    public Observable<BaseResult<List<FeedReasonEntity>>> requestFeedbackReasonList() {
        return CommonTransformer.switchSchedulers(getApiService().requestFeedbackReasonList().retryWhen(new RetryWhen()));
    }


    /**
     * 问题反馈提交
     *
     * @param commitEntity
     * @return
     */
    public Observable<BaseResult> requestFeedbackCommit(FeedbackCommitEntity commitEntity) {
        Map<String, Object> params = new HashMap<>(1);
        params.put("id", commitEntity.getId());
        params.put("content", commitEntity.getContent());
        params.put("phone", commitEntity.getPhone());
        params.put("photo", commitEntity.getPhoto());
        return CommonTransformer.switchSchedulers(getApiService().requestFeedbackCommit(params).retryWhen(new RetryWhen()));
    }


    /**
     * 分享成功接口
     *
     * @param id
     * @return
     */
    public Observable<BaseResult> requestShareSuccess(String id) {
        Map<String, Object> params = new HashMap<>(1);
        params.put("id", id);
        return CommonTransformer.switchSchedulers(getApiService().requestShareSuccess(params).retryWhen(new RetryWhen()));
    }

    /**
     * 重置手机号
     *
     * @param phone
     * @return
     */
    public Observable<BaseResult> requestResetPhone(String phone, String code) {
        Map<String, Object> params = new HashMap<>(1);
        params.put("phone", phone);
        params.put("code", code);
        return CommonTransformer.switchSchedulers(getApiService().requestResetPhone(params).retryWhen(new RetryWhen()));
    }


    /**
     * 专项考试需要的人脸验证
     *
     * @return
     */
    public Observable<BaseResult<FaceRecognizeResult>> requestFaceVerifySpecial(String examId, String photoBase64) {
        Map<String, Object> params = new HashMap<>(2);
        params.put("examId", examId);
        params.put("photo", photoBase64);
        return CommonTransformer.switchSchedulers(getApiService().requestFaceVerifySpecial(params).retryWhen(new RetryWhen()));
    }


    public Observable<BaseResult<MedalDictionary>> requestMedalDictionary() {
        return CommonTransformer.switchSchedulers(getApiService().requestMedalDictionary().retryWhen(new RetryWhen()));
    }


    public Observable<BaseResult<AppUpdateInfo>> requestAppVersionInfo() {
        Map<String, Object> params = new HashMap<>(4);
        params.put("appType", "Android");
        params.put("appVersion", "v"+AppUtils.getAppVersionName());
        params.put("deviceID", getUniquePsuedoID());
        params.put("deviceOS", android.os.Build.VERSION.RELEASE);
        return CommonTransformer.switchSchedulers(getApiService().requestAppVersionInfo(params).retryWhen(new RetryWhen()));
    }


}

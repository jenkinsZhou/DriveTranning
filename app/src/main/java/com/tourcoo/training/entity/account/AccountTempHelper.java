package com.tourcoo.training.entity.account;

import com.tourcoo.training.entity.account.register.BusinessLicenseInfo;
import com.tourcoo.training.entity.medal.StudyMedalEntity;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2020年03月16日11:01
 * @Email: 971613168@qq.com
 */
public class AccountTempHelper {

    public static final String TAG = "AccountTempHelper";

    // 1: 列表   2 ：视频详情  3：文本详情
    private int shareNewsPageType;
    private String  shareNewsID;


    //type:  1:课程购买  2：学币购买
    public static int WX_TYPE_RETURN = 0;

    /**
     * 人脸认证缓存图片Base64数据
     */
    private String tempBase64FaceData;


    public String getTempBase64FaceData() {
        return tempBase64FaceData;
    }

    public void setTempBase64FaceData(String tempBase64FaceData) {
        this.tempBase64FaceData = tempBase64FaceData;
    }

    public String getShareNewsID() {
        return shareNewsID;
    }

    public void setShareNewsID(String shareNewsID) {
        this.shareNewsID = shareNewsID;
    }

    public int getShareNewsPageType() {
        return shareNewsPageType;
    }

    public void setShareNewsPageType(int shareNewsPageType) {
        this.shareNewsPageType = shareNewsPageType;
    }

    private AccountTempHelper() {
    }
//    private String facePhotoPath;

 /*   public String getFacePhotoPath() {
        return facePhotoPath;
    }

    public void setFacePhotoPath(String facePhotoPath) {
        this.facePhotoPath = facePhotoPath;
    }*/

    private static class SingletonInstance {
        private static final AccountTempHelper INSTANCE = new AccountTempHelper();
    }

    public static AccountTempHelper getInstance() {
        return SingletonInstance.INSTANCE;
    }

    private int recognizeType;

    private IdCardInfo idCardInfo;

    private StudyMedalEntity studyMedalEntity;

    public StudyMedalEntity getStudyMedalEntity() {
        return studyMedalEntity;
    }

    public void setStudyMedalEntity(StudyMedalEntity studyMedalEntity) {
        this.studyMedalEntity = studyMedalEntity;
    }

    private boolean isRecognizeIdCard;

    public boolean isRecognizeIdCard() {
        return isRecognizeIdCard;
    }

    public void setRecognizeIdCard(boolean recognizeIdCard) {
        isRecognizeIdCard = recognizeIdCard;
    }

    private BusinessLicenseInfo businessLicenseInfo;


    public BusinessLicenseInfo getBusinessLicenseInfo() {
        return businessLicenseInfo;
    }

    public void setBusinessLicenseInfo(BusinessLicenseInfo businessLicenseInfo) {
        this.businessLicenseInfo = businessLicenseInfo;
    }

    public IdCardInfo getIdCardInfo() {
        return idCardInfo;
    }

    public void setIdCardInfo(IdCardInfo idCardInfo) {
        this.idCardInfo = idCardInfo;
    }

    private String registerPhone;

    private String registerName;

    private String registerIdCard;

    private String businessLicensePath;

    private String faceBase64Image;

    private String idCardBase64Image;

    public String getIdCardBase64Image() {
        return idCardBase64Image;
    }

    public void setIdCardBase64Image(String idCardBase64Image) {
        this.idCardBase64Image = idCardBase64Image;
    }

    public String getFaceBase64Image() {
        return faceBase64Image;
    }

    public void setFaceBase64Image(String faceBase64Image) {
        this.faceBase64Image = faceBase64Image;
    }

    public String getBusinessLicensePath() {
        return businessLicensePath;
    }

    public void setBusinessLicensePath(String businessLicensePath) {
        this.businessLicensePath = businessLicensePath;
    }

    public String getRegisterIdCard() {
        return registerIdCard;
    }

    public void setRegisterIdCard(String registerIdCard) {
        this.registerIdCard = registerIdCard;
    }

    public int getRecognizeType() {
        return recognizeType;
    }

    public void setRecognizeType(int recognizeType) {
        this.recognizeType = recognizeType;
    }

    public String getRegisterPhone() {
        return registerPhone;
    }

    public void setRegisterPhone(String registerPhone) {
        this.registerPhone = registerPhone;
    }

    public String getRegisterName() {
        return registerName;
    }

    public void setRegisterName(String registerName) {
        this.registerName = registerName;
    }
}

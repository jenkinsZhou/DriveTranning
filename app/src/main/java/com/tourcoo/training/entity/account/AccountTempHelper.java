package com.tourcoo.training.entity.account;

import com.tourcoo.training.entity.account.register.BusinessLicenseInfo;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2020年03月16日11:01
 * @Email: 971613168@qq.com
 */
public class AccountTempHelper {

    public static final String TAG = "AccountTempHelper";

    private AccountTempHelper() {
    }
    private String facePhotoPath;

    public String getFacePhotoPath() {
        return facePhotoPath;
    }

    public void setFacePhotoPath(String facePhotoPath) {
        this.facePhotoPath = facePhotoPath;
    }

    private static class SingletonInstance {
        private static final AccountTempHelper INSTANCE = new AccountTempHelper();
    }

    public static AccountTempHelper getInstance() {
        return SingletonInstance.INSTANCE;
    }

    private int recognizeType;

    private IdCardInfo idCardInfo;

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

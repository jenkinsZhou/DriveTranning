package com.tourcoo.training.entity.account;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2020年03月16日11:01
 * @Email: 971613168@qq.com
 */
public class RegisterTempHelper {

    public static final String TAG = "AccountHelper";
    public static final String PREF_ACCESS_TOKEN = "access_token";
    public static final String PREF_REFRESH_TOKEN = "refresh_token";

    private RegisterTempHelper(){}
    private static class SingletonInstance {
        private static final RegisterTempHelper INSTANCE = new RegisterTempHelper();
    }

    public static RegisterTempHelper getInstance() {
        return SingletonInstance.INSTANCE;
    }

    private int  registerType;


    private String  registerPhone;

    private String  registerName;

    private String  registerIdCard;

    private String  businessLicensePath;

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

    public int getRegisterType() {
        return registerType;
    }

    public void setRegisterType(int registerType) {
        this.registerType = registerType;
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
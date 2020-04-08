package com.tourcoo.training.entity.account;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2020年04月08日14:36
 * @Email: 971613168@qq.com
 */
public class UserInfo  {


    /**
     * AccessToken : bf13e42bfb360422cbcb2e821c03a1e84bbb9c9c
     * Enabled : 1
     * FVEnable : 0
     * UserType : 1
     * IsAuthenticated : 0
     * Status : 2
     * IndustryCateID :
     * Name : 测试驾驶员03
     * CoinsTotal : 0
     * CoinsRemain : 0
     * Avatar :
     * Phone : 15255999917
     * IDCard : 340104199210101525
     * IDCardUrl :
     * CompanyName : 测试驾驶员03
     * IsMandatoryUpdate : 0
     * OnlineLearnProgress : 0
     * OnsiteLearnProgress : 0
     * MonthRanking : 0
     * Vehicle : {"PlateNumber":"皖P5121169","PlateColor":"","Model":"","Brand":"","ExpiredTime":"2022-01-01"}
     */

    private String AccessToken;
    private int Enabled;
    private int FVEnable;
    private int UserType;
    private int IsAuthenticated;
    private int Status;
    private String IndustryCateID;
    private String Name;
    private double CoinsTotal;
    private double CoinsRemain;
    private String Avatar;
    private String Phone;
    private String IDCard;
    private String IDCardUrl;
    private String CompanyName;
    private double IsMandatoryUpdate;
    private double OnlineLearnProgress;
    private double OnsiteLearnProgress;
    private double MonthRanking;
    private VehicleInfo Vehicle;

    public String getAccessToken() {
        return AccessToken;
    }

    public void setAccessToken(String AccessToken) {
        this.AccessToken = AccessToken;
    }

    public int getEnabled() {
        return Enabled;
    }

    public void setEnabled(int Enabled) {
        this.Enabled = Enabled;
    }

    public int getFVEnable() {
        return FVEnable;
    }

    public void setFVEnable(int FVEnable) {
        this.FVEnable = FVEnable;
    }

    public int getUserType() {
        return UserType;
    }

    public void setUserType(int UserType) {
        this.UserType = UserType;
    }

    public int getIsAuthenticated() {
        return IsAuthenticated;
    }

    public void setIsAuthenticated(int IsAuthenticated) {
        this.IsAuthenticated = IsAuthenticated;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int Status) {
        this.Status = Status;
    }

    public String getIndustryCateID() {
        return IndustryCateID;
    }

    public void setIndustryCateID(String IndustryCateID) {
        this.IndustryCateID = IndustryCateID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public double getCoinsTotal() {
        return CoinsTotal;
    }

    public void setCoinsTotal(double CoinsTotal) {
        this.CoinsTotal = CoinsTotal;
    }

    public double getCoinsRemain() {
        return CoinsRemain;
    }

    public void setCoinsRemain(double CoinsRemain) {
        this.CoinsRemain = CoinsRemain;
    }

    public String getAvatar() {
        return Avatar;
    }

    public void setAvatar(String Avatar) {
        this.Avatar = Avatar;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String Phone) {
        this.Phone = Phone;
    }

    public String getIDCard() {
        return IDCard;
    }

    public void setIDCard(String IDCard) {
        this.IDCard = IDCard;
    }

    public String getIDCardUrl() {
        return IDCardUrl;
    }

    public void setIDCardUrl(String IDCardUrl) {
        this.IDCardUrl = IDCardUrl;
    }

    public String getCompanyName() {
        return CompanyName;
    }

    public void setCompanyName(String CompanyName) {
        this.CompanyName = CompanyName;
    }

    public double getIsMandatoryUpdate() {
        return IsMandatoryUpdate;
    }

    public void setIsMandatoryUpdate(double IsMandatoryUpdate) {
        this.IsMandatoryUpdate = IsMandatoryUpdate;
    }

    public double getOnlineLearnProgress() {
        return OnlineLearnProgress;
    }

    public void setOnlineLearnProgress(double OnlineLearnProgress) {
        this.OnlineLearnProgress = OnlineLearnProgress;
    }

    public double getOnsiteLearnProgress() {
        return OnsiteLearnProgress;
    }

    public void setOnsiteLearnProgress(double OnsiteLearnProgress) {
        this.OnsiteLearnProgress = OnsiteLearnProgress;
    }

    public double getMonthRanking() {
        return MonthRanking;
    }

    public void setMonthRanking(double MonthRanking) {
        this.MonthRanking = MonthRanking;
    }

    public VehicleInfo getVehicle() {
        return Vehicle;
    }

    public void setVehicle(VehicleInfo Vehicle) {
        this.Vehicle = Vehicle;
    }


}

package com.tourcoo.training.entity.account;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

import java.util.List;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2020年04月08日14:36
 * @Email: 971613168@qq.com
 */
@Entity
public class UserInfo implements Parcelable {


    public String getMonthRanking() {
        return MonthRanking;
    }

    public void setMonthRanking(String monthRanking) {
        MonthRanking = monthRanking;
    }

    /**
     * AccessToken : bf13e42bfb360422cbcb2e821c03a1e84bbb9c9c
     * Enabled : 1
     * FVEnable : 0
     * UserType : 1
     * IsAuthenticated : 0
     * Status : 2
     * IndustryCategoryIDs :
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
    private String IndustryCategoryIDs;
    private String IndustryCategoryNames;
    private String Name;
    private String TraineeID;
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
    private String MonthRanking;

    @Convert(converter = VehicleInfo.VehicleInfoListConverter.class, columnType = String.class)
    private List<VehicleInfo> Vehicles;

    public String getTraineeID() {
        return TraineeID;
    }

    public void setTraineeID(String traineeID) {
        TraineeID = traineeID;
    }

    public List<VehicleInfo> getVehicles() {
        return Vehicles;
    }

    public void setVehicles(List<VehicleInfo> vehicles) {
        Vehicles = vehicles;
    }

    /*   public String getAccessToken() {
           return AccessToken;
       }*/
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
        return this.getStatus();
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

    public String getIndustryCategoryIDs() {
        return IndustryCategoryIDs;
    }

    public void setIndustryCategoryIDs(String IndustryCateID) {
        this.IndustryCategoryIDs = IndustryCateID;
    }

    public String getIndustryCategoryNames() {
        return IndustryCategoryNames;
    }

    public void setIndustryCategoryNames(String industryCategoryNames) {
        IndustryCategoryNames = industryCategoryNames;
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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.AccessToken);
        dest.writeInt(this.Enabled);
        dest.writeInt(this.FVEnable);
        dest.writeInt(this.UserType);
        dest.writeInt(this.IsAuthenticated);
        dest.writeInt(this.Status);
        dest.writeString(this.IndustryCategoryIDs);
        dest.writeString(this.IndustryCategoryNames);
        dest.writeString(this.Name);
        dest.writeDouble(this.CoinsTotal);
        dest.writeDouble(this.CoinsRemain);
        dest.writeString(this.Avatar);
        dest.writeString(this.Phone);
        dest.writeString(this.IDCard);
        dest.writeString(this.IDCardUrl);
        dest.writeString(this.CompanyName);
        dest.writeDouble(this.IsMandatoryUpdate);
        dest.writeDouble(this.OnlineLearnProgress);
        dest.writeDouble(this.OnsiteLearnProgress);
        dest.writeString(this.MonthRanking);
        dest.writeTypedList(this.Vehicles);
    }

    protected UserInfo(Parcel in) {
        this.AccessToken = in.readString();
        this.Enabled = in.readInt();
        this.FVEnable = in.readInt();
        this.UserType = in.readInt();
        this.IsAuthenticated = in.readInt();
        this.Status = in.readInt();
        this.IndustryCategoryIDs = in.readString();
        this.IndustryCategoryNames = in.readString();
        this.Name = in.readString();
        this.CoinsTotal = in.readDouble();
        this.CoinsRemain = in.readDouble();
        this.Avatar = in.readString();
        this.Phone = in.readString();
        this.IDCard = in.readString();
        this.IDCardUrl = in.readString();
        this.CompanyName = in.readString();
        this.IsMandatoryUpdate = in.readDouble();
        this.OnlineLearnProgress = in.readDouble();
        this.OnsiteLearnProgress = in.readDouble();
        this.MonthRanking = in.readString();
        this.Vehicles = in.createTypedArrayList(VehicleInfo.CREATOR);
    }

    @Generated(hash = 2034659939)
    public UserInfo(String AccessToken, int Enabled, int FVEnable, int UserType, int IsAuthenticated, int Status,
            String IndustryCategoryIDs, String IndustryCategoryNames, String Name, String TraineeID,
            double CoinsTotal, double CoinsRemain, String Avatar, String Phone, String IDCard, String IDCardUrl,
            String CompanyName, double IsMandatoryUpdate, double OnlineLearnProgress, double OnsiteLearnProgress,
            String MonthRanking, List<VehicleInfo> Vehicles) {
        this.AccessToken = AccessToken;
        this.Enabled = Enabled;
        this.FVEnable = FVEnable;
        this.UserType = UserType;
        this.IsAuthenticated = IsAuthenticated;
        this.Status = Status;
        this.IndustryCategoryIDs = IndustryCategoryIDs;
        this.IndustryCategoryNames = IndustryCategoryNames;
        this.Name = Name;
        this.TraineeID = TraineeID;
        this.CoinsTotal = CoinsTotal;
        this.CoinsRemain = CoinsRemain;
        this.Avatar = Avatar;
        this.Phone = Phone;
        this.IDCard = IDCard;
        this.IDCardUrl = IDCardUrl;
        this.CompanyName = CompanyName;
        this.IsMandatoryUpdate = IsMandatoryUpdate;
        this.OnlineLearnProgress = OnlineLearnProgress;
        this.OnsiteLearnProgress = OnsiteLearnProgress;
        this.MonthRanking = MonthRanking;
        this.Vehicles = Vehicles;
    }

    @Generated(hash = 1279772520)
    public UserInfo() {
    }

    public static final Creator<UserInfo> CREATOR = new Creator<UserInfo>() {
        @Override
        public UserInfo createFromParcel(Parcel source) {
            return new UserInfo(source);
        }

        @Override
        public UserInfo[] newArray(int size) {
            return new UserInfo[size];
        }
    };
}

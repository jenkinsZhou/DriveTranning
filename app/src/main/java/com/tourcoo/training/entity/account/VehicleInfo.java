package com.tourcoo.training.entity.account;

import android.os.Parcel;
import android.os.Parcelable;

import com.alibaba.fastjson.JSON;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.converter.PropertyConverter;

import java.util.List;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2020年04月08日14:37
 * @Email: 971613168@qq.com
 */

@Entity
public class VehicleInfo implements Parcelable {

    /**
     * PlateNumber : 皖P5121169
     * PlateColor :
     * Model :
     * Brand :
     * ExpiredTime : 2022-01-01
     */

    private String PlateNumber;
    private String PlateColor;
    private String Model;
    private String Brand;
    private String ExpiredTime;

    public String getPlateNumber() {
        return PlateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        PlateNumber = plateNumber;
    }

    public String getPlateColor() {
        return PlateColor;
    }

    public void setPlateColor(String plateColor) {
        PlateColor = plateColor;
    }

    public String getModel() {
        return Model;
    }

    public void setModel(String model) {
        Model = model;
    }

    public String getBrand() {
        return Brand;
    }

    public void setBrand(String brand) {
        Brand = brand;
    }

    public String getExpiredTime() {
        return ExpiredTime;
    }

    public void setExpiredTime(String expiredTime) {
        ExpiredTime = expiredTime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.PlateNumber);
        dest.writeString(this.PlateColor);
        dest.writeString(this.Model);
        dest.writeString(this.Brand);
        dest.writeString(this.ExpiredTime);
    }

    public VehicleInfo() {
    }

    protected VehicleInfo(Parcel in) {
        this.PlateNumber = in.readString();
        this.PlateColor = in.readString();
        this.Model = in.readString();
        this.Brand = in.readString();
        this.ExpiredTime = in.readString();
    }

    @Generated(hash = 832687502)
    public VehicleInfo(String PlateNumber, String PlateColor, String Model, String Brand, String ExpiredTime) {
        this.PlateNumber = PlateNumber;
        this.PlateColor = PlateColor;
        this.Model = Model;
        this.Brand = Brand;
        this.ExpiredTime = ExpiredTime;
    }

    public static final Parcelable.Creator<VehicleInfo> CREATOR = new Parcelable.Creator<VehicleInfo>() {
        @Override
        public VehicleInfo createFromParcel(Parcel source) {
            return new VehicleInfo(source);
        }

        @Override
        public VehicleInfo[] newArray(int size) {
            return new VehicleInfo[size];
        }
    };


    public static class VehicleInfoListConverter implements PropertyConverter<List<VehicleInfo>, String> {

        @Override
        public List<VehicleInfo> convertToEntityProperty(String databaseValue) {
            return JSON.parseArray(databaseValue, VehicleInfo.class);
        }

        @Override
        public String convertToDatabaseValue(List<VehicleInfo> entityProperty) {
            return JSON.toJSONString(entityProperty);
        }
    }
}

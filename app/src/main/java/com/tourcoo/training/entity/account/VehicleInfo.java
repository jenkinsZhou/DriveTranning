package com.tourcoo.training.entity.account;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2020年04月08日14:37
 * @Email: 971613168@qq.com
 */

@Entity
public class VehicleInfo {

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

    @Generated(hash = 832687502)
    public VehicleInfo(String PlateNumber, String PlateColor, String Model,
            String Brand, String ExpiredTime) {
        this.PlateNumber = PlateNumber;
        this.PlateColor = PlateColor;
        this.Model = Model;
        this.Brand = Brand;
        this.ExpiredTime = ExpiredTime;
    }

    @Generated(hash = 280466907)
    public VehicleInfo() {
    }

    public String getPlateNumber() {
        return PlateNumber;
    }

    public void setPlateNumber(String PlateNumber) {
        this.PlateNumber = PlateNumber;
    }

    public String getPlateColor() {
        return PlateColor;
    }

    public void setPlateColor(String PlateColor) {
        this.PlateColor = PlateColor;
    }

    public String getModel() {
        return Model;
    }

    public void setModel(String Model) {
        this.Model = Model;
    }

    public String getBrand() {
        return Brand;
    }

    public void setBrand(String Brand) {
        this.Brand = Brand;
    }

    public String getExpiredTime() {
        return ExpiredTime;
    }

    public void setExpiredTime(String ExpiredTime) {
        this.ExpiredTime = ExpiredTime;
    }
}

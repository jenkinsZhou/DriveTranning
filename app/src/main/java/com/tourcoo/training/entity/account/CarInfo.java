package com.tourcoo.training.entity.account;


import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2020年03月16日14:15
 * @Email: 971613168@qq.com
 */
@Entity
public class CarInfo {


    /**
     * CarNum : 车牌号
     * CarModel : 车型
     * CarBrand : 车辆品牌
     * ExpiredTime : 50
     */

    private String CarNum;
    private String CarModel;
    private String CarBrand;
    private String ExpiredTime;





    @Generated(hash = 1829370122)
    public CarInfo(String CarNum, String CarModel, String CarBrand,
            String ExpiredTime) {
        this.CarNum = CarNum;
        this.CarModel = CarModel;
        this.CarBrand = CarBrand;
        this.ExpiredTime = ExpiredTime;
    }

    @Generated(hash = 850322869)
    public CarInfo() {
    }





    public String getCarNum() {
        return CarNum;
    }

    public void setCarNum(String CarNum) {
        this.CarNum = CarNum;
    }

    public String getCarModel() {
        return CarModel;
    }

    public void setCarModel(String CarModel) {
        this.CarModel = CarModel;
    }

    public String getCarBrand() {
        return CarBrand;
    }

    public void setCarBrand(String CarBrand) {
        this.CarBrand = CarBrand;
    }

    public String getExpiredTime() {
        return ExpiredTime;
    }

    public void setExpiredTime(String ExpiredTime) {
        this.ExpiredTime = ExpiredTime;
    }





}

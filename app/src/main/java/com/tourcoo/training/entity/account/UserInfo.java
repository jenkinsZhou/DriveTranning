package com.tourcoo.training.entity.account;

import com.alibaba.fastjson.JSON;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.converter.PropertyConverter;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2020年03月16日14:14
 * @Email: 971613168@qq.com
 */
@Entity
public class UserInfo  {

    /**
     * AccessToken : token
     * UserType : 1
     * IsAuthenticate : 0
     * Status : 0
     * SelectCourseDuration : 2
     * SelectTradeType : 0
     * Name : 顶哥
     * LearnCurrency : 2000
     * Avatar : https://www.postman.com/img/pages/downloads/canary-treated-logo.svg
     * Phone : 17730212464
     * IdCard : 341124199202174815
     * CompanyName : 顶级公司
     * IdCardImg : https://www.postman.com/img/pages/downloads/canary-treated-logo.svg
     * LearnProcess : 80
     * LearnLevel : 2
     * Car : {"CarNum":"车牌号","CarModel":"车型","CarBrand":"车辆品牌","ExpiredTime":"50"}
     */
    private String AccessToken;
    private int UserType;
    private int IsAuthenticate;
    private int Status;
    private int SelectCourseDuration;
    private int SelectTradeType;
    private String Name;
    private int LearnCurrency;
    private String Avatar;
    private String Phone;
    private String IdCard;
    private String CompanyName;
    private String IdCardImg;
    private int LearnProcess;
    private int LearnLevel;
    //和person关联的id
    private Long pid;

    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }


    public CarInfo getCar() {
        return Car;
    }

    public void setCar(CarInfo car) {
        Car = car;
    }
    
    @Convert(converter = CarConverter.class, columnType = String.class)
    private CarInfo Car;

    public String getAccessToken() {
        return AccessToken;
    }

    public void setAccessToken(String AccessToken) {
        this.AccessToken = AccessToken;
    }

    public int getUserType() {
        return UserType;
    }

    public void setUserType(int UserType) {
        this.UserType = UserType;
    }

    public int getIsAuthenticate() {
        return IsAuthenticate;
    }

    public void setIsAuthenticate(int IsAuthenticate) {
        this.IsAuthenticate = IsAuthenticate;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int Status) {
        this.Status = Status;
    }

    public int getSelectCourseDuration() {
        return SelectCourseDuration;
    }

    public void setSelectCourseDuration(int SelectCourseDuration) {
        this.SelectCourseDuration = SelectCourseDuration;
    }

    public int getSelectTradeType() {
        return SelectTradeType;
    }

    public void setSelectTradeType(int SelectTradeType) {
        this.SelectTradeType = SelectTradeType;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public int getLearnCurrency() {
        return LearnCurrency;
    }

    public void setLearnCurrency(int LearnCurrency) {
        this.LearnCurrency = LearnCurrency;
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

    public String getIdCard() {
        return IdCard;
    }

    public void setIdCard(String IdCard) {
        this.IdCard = IdCard;
    }

    public String getCompanyName() {
        return CompanyName;
    }

    public void setCompanyName(String CompanyName) {
        this.CompanyName = CompanyName;
    }

    public String getIdCardImg() {
        return IdCardImg;
    }

    public void setIdCardImg(String IdCardImg) {
        this.IdCardImg = IdCardImg;
    }

    public int getLearnProcess() {
        return LearnProcess;
    }

    public void setLearnProcess(int LearnProcess) {
        this.LearnProcess = LearnProcess;
    }

    public int getLearnLevel() {
        return LearnLevel;
    }

    public void setLearnLevel(int LearnLevel) {
        this.LearnLevel = LearnLevel;
    }





    @Generated(hash = 497647764)
    public UserInfo(String AccessToken, int UserType, int IsAuthenticate, int Status,
            int SelectCourseDuration, int SelectTradeType, String Name, int LearnCurrency,
            String Avatar, String Phone, String IdCard, String CompanyName, String IdCardImg,
            int LearnProcess, int LearnLevel, Long pid, CarInfo Car) {
        this.AccessToken = AccessToken;
        this.UserType = UserType;
        this.IsAuthenticate = IsAuthenticate;
        this.Status = Status;
        this.SelectCourseDuration = SelectCourseDuration;
        this.SelectTradeType = SelectTradeType;
        this.Name = Name;
        this.LearnCurrency = LearnCurrency;
        this.Avatar = Avatar;
        this.Phone = Phone;
        this.IdCard = IdCard;
        this.CompanyName = CompanyName;
        this.IdCardImg = IdCardImg;
        this.LearnProcess = LearnProcess;
        this.LearnLevel = LearnLevel;
        this.pid = pid;
        this.Car = Car;
    }

    @Generated(hash = 1279772520)
    public UserInfo() {
    }

  


    public static class CarConverter implements PropertyConverter<CarInfo, String> {
        @Override
        public CarInfo convertToEntityProperty(String databaseValue) {
            if (databaseValue == null ) {
                return null;
            }
            return JSON.parseObject(databaseValue,CarInfo.class);
        }

        @Override
        public String convertToDatabaseValue(CarInfo entityProperty) {
            return entityProperty == null ? null : JSON.toJSONString(entityProperty);
        }
    }


}

package com.tourcoo.training.entity.study;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2020年05月07日16:30
 * @Email: 971613168@qq.com
 */
public class StudyInfo {
    private String Time;
    private int ClassDuration;
    private int Progress;
    private String SecurityOfficer;
    private String Address;


    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public int getClassDuration() {
        return ClassDuration;
    }

    public void setClassDuration(int classDuration) {
        ClassDuration = classDuration;
    }

    public int getProgress() {
        return Progress;
    }

    public void setProgress(int progress) {
        Progress = progress;
    }

    public String getSecurityOfficer() {
        return SecurityOfficer;
    }

    public void setSecurityOfficer(String securityOfficer) {
        SecurityOfficer = securityOfficer;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }
}

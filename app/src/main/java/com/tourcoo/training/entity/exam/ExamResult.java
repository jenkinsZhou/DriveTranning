package com.tourcoo.training.entity.exam;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2020年04月16日17:03
 * @Email: 971613168@qq.com
 */
public class ExamResult {

    /**
     * CertificateId : 9044-33496
     * Name : 王宇九
     * IdCard : 340811199311135335
     * TrainingPlanName : 安全培训
     * StartTime : 2020-04-02 00:00:00
     * EndTime : 2020-04-30 23:59:59
     * CertificateNumber : 904433496
     * CreateTime : 2020-04-16 17:01:29
     */

    private String CertificateId;
    private String Name;
    private String IdCard;
    private String TrainingPlanName;
    private String StartTime;
    private String EndTime;
    private String CertificateNumber;
    private String CreateTime;

    public String getCertificateId() {
        return CertificateId;
    }

    public void setCertificateId(String CertificateId) {
        this.CertificateId = CertificateId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getIdCard() {
        return IdCard;
    }

    public void setIdCard(String IdCard) {
        this.IdCard = IdCard;
    }

    public String getTrainingPlanName() {
        return TrainingPlanName;
    }

    public void setTrainingPlanName(String TrainingPlanName) {
        this.TrainingPlanName = TrainingPlanName;
    }

    public String getStartTime() {
        return StartTime;
    }

    public void setStartTime(String StartTime) {
        this.StartTime = StartTime;
    }

    public String getEndTime() {
        return EndTime;
    }

    public void setEndTime(String EndTime) {
        this.EndTime = EndTime;
    }

    public String getCertificateNumber() {
        return CertificateNumber;
    }

    public void setCertificateNumber(String CertificateNumber) {
        this.CertificateNumber = CertificateNumber;
    }

    public String getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(String CreateTime) {
        this.CreateTime = CreateTime;
    }

}

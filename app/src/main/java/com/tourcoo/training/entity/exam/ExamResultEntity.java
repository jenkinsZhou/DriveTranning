package com.tourcoo.training.entity.exam;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2020年04月16日17:02
 * @Email: 971613168@qq.com
 */
public class ExamResultEntity {


    /**
     * Status : 1
     * Tips : 总共3道题，答对0道题，总分0分。(80分合格）
     * Data : {"CertificateId":"9044-33496","Name":"王宇九","IdCard":"340811199311135335","TrainingPlanName":"安全培训","StartTime":"2020-04-02 00:00:00","EndTime":"2020-04-30 23:59:59","CertificateNumber":"904433496","CreateTime":"2020-04-16 17:01:29"}
     */

    private int Status;
    private String Tips;
    private ExamResult Data;

    public int getStatus() {
        return Status;
    }

    public void setStatus(int Status) {
        this.Status = Status;
    }

    public String getTips() {
        return Tips;
    }

    public void setTips(String Tips) {
        this.Tips = Tips;
    }

    public ExamResult getData() {
        return Data;
    }

    public void setData(ExamResult Data) {
        this.Data = Data;
    }


}

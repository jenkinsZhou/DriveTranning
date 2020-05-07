package com.tourcoo.training.entity.study;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.Date;

/**
 * @author :JenkinsZhou
 * @description :学习记录实体
 * @company :途酷科技
 * @date 2020年05月07日11:13
 * @Email: 971613168@qq.com
 */
public class StudyRecord implements MultiItemEntity {


    /**
     * CoverUrl : null
     * TrainingPlanStatus : 0
     * TrainingPlanName : html + 视频 课件
     * TrainingPlanStartTime : 2020-05-06
     * TrainingPlanEndTime : 2020-05-31
     * TrainingPlanID : 9168
     * IsOnlineLearning : 1
     * ExamScore : -
     */
    private Date trainDate;
    private String CoverUrl;
    private int TrainingPlanStatus;
    private String TrainingPlanName;
    private String TrainingPlanStartTime;
    private String TrainingPlanEndTime;
    private int TrainingPlanID;
    private int IsOnlineLearning;
    private String ExamScore;
    private boolean header;
    private String title;

    /**
     * 是否折叠(默认折叠所有)
     */
    private boolean folding = false ;

    public boolean isFolding() {
        return folding;
    }

    public void setFolding(boolean folding) {
        this.folding = folding;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCoverUrl() {
        return CoverUrl;
    }

    public void setCoverUrl(String CoverUrl) {
        this.CoverUrl = CoverUrl;
    }

    public int getTrainingPlanStatus() {
        return TrainingPlanStatus;
    }

    public void setTrainingPlanStatus(int TrainingPlanStatus) {
        this.TrainingPlanStatus = TrainingPlanStatus;
    }

    public String getTrainingPlanName() {
        return TrainingPlanName;
    }

    public void setTrainingPlanName(String TrainingPlanName) {
        this.TrainingPlanName = TrainingPlanName;
    }

    public String getTrainingPlanStartTime() {
        return TrainingPlanStartTime;
    }

    public void setTrainingPlanStartTime(String TrainingPlanStartTime) {
        this.TrainingPlanStartTime = TrainingPlanStartTime;
    }

    public String getTrainingPlanEndTime() {
        return TrainingPlanEndTime;
    }

    public void setTrainingPlanEndTime(String TrainingPlanEndTime) {
        this.TrainingPlanEndTime = TrainingPlanEndTime;
    }

    public int getTrainingPlanID() {
        return TrainingPlanID;
    }

    public void setTrainingPlanID(int TrainingPlanID) {
        this.TrainingPlanID = TrainingPlanID;
    }

    public int getIsOnlineLearning() {
        return IsOnlineLearning;
    }

    public void setIsOnlineLearning(int IsOnlineLearning) {
        this.IsOnlineLearning = IsOnlineLearning;
    }

    public String getExamScore() {
        return ExamScore;
    }

    public void setExamScore(String ExamScore) {
        this.ExamScore = ExamScore;
    }

    public boolean isHeader() {
        return header;
    }

    public void setHeader(boolean header) {
        this.header = header;
    }

    @Override
    public int getItemType() {

        return 0;
    }

    public Date getTrainDate() {
        return trainDate;
    }

    public void setTrainDate(Date trainDate) {
        this.trainDate = trainDate;
    }



}

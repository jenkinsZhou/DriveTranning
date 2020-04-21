package com.tourcoo.training.entity.course;

/**
 * @author :JenkinsZhou
 * @description :课程信息
 * @company :途酷科技
 * @date 2020年04月13日14:53
 * @Email: 971613168@qq.com
 */
public class CourseInfo {


    /**
     * CoverUrl : null
     * Tag : 1
     * Status : 1
     * Title : 安全培训
     * Progress : 45
     * TimeRange : 2020-04-02至2020-04-30
     * TrainingPlanID : 9044
     */

    private String CoverUrl;
    private int Tag;
    private int Status;
    private String Title;
    private double Progress;
    private String TimeRange;
    private String TrainingPlanID;
    private String TotalDuration;
    private String address;
    private int Role;

    public String getCoverUrl() {
        return CoverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        CoverUrl = coverUrl;
    }

    public int getTag() {
        return Tag;
    }

    public void setTag(int Tag) {
        this.Tag = Tag;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int Status) {
        this.Status = Status;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String Title) {
        this.Title = Title;
    }

    public double getProgress() {
        return Progress;
    }

    public void setProgress(double Progress) {
        this.Progress = Progress;
    }

    public String getTimeRange() {
        return TimeRange;
    }

    public void setTimeRange(String TimeRange) {
        this.TimeRange = TimeRange;
    }

    public String getTrainingPlanID() {
        return TrainingPlanID;
    }

    public void setTrainingPlanID(String TrainingPlanID) {
        this.TrainingPlanID = TrainingPlanID;
    }

    public String getTotalDuration() {
        return TotalDuration;
    }

    public void setTotalDuration(String totalDuration) {
        TotalDuration = totalDuration;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getRole() {
        return Role;
    }

    public void setRole(int role) {
        Role = role;
    }
}

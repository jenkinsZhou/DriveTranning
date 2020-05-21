package com.tourcoo.training.entity.training;

public class ProfessionalTwoTypeModel {


    /**
     * specialId : 1
     * ChildModuleId : 1
     * Title : 专项01
     * SubTitle : 专项01
     * Coins : 1
     * Type : 0
     * planStatus
     * TrainingPlanStatus
     */

    private String specialId;
    private String ChildModuleId;
    private String Title;
    private String SubTitle;
    private String Coins;
    private String Type;
    private int Status;
    /**
     * "planStatus" :计划是否完成  1：完成  0：没完成
     */
    private int planStatus;

    /**
     * TrainingPlanStatus":计划时间是否开始 0未开始 1进行中 2已过期 （说明：未开始和已过期状态下不允许支付学币）
     */
    private int TrainingPlanStatus;

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public String getSpecialId() {
        return specialId;
    }

    public void setSpecialId(String specialId) {
        this.specialId = specialId;
    }

    public String getChildModuleId() {
        return ChildModuleId;
    }

    public void setChildModuleId(String ChildModuleId) {
        this.ChildModuleId = ChildModuleId;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String Title) {
        this.Title = Title;
    }

    public String getSubTitle() {
        return SubTitle;
    }

    public void setSubTitle(String SubTitle) {
        this.SubTitle = SubTitle;
    }

    public String getCoins() {
        return Coins;
    }

    public void setCoins(String Coins) {
        this.Coins = Coins;
    }

    public String getType() {
        return Type;
    }

    public void setType(String Type) {
        this.Type = Type;
    }

    public int getPlanStatus() {
        return planStatus;
    }

    public void setPlanStatus(int planStatus) {
        this.planStatus = planStatus;
    }

    public int getTrainingPlanStatus() {
        return TrainingPlanStatus;
    }

    public void setTrainingPlanStatus(int trainingPlanStatus) {
        TrainingPlanStatus = trainingPlanStatus;
    }
}

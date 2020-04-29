package com.tourcoo.training.entity.training;

import com.tourcoo.training.entity.course.CourseInfo;

import java.util.List;

public class TwoTypeModel {

    private int Status;
    private List<CourseInfo> PlanData;


    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public List<CourseInfo> getPlanData() {
        return PlanData;
    }

    public void setPlanData(List<CourseInfo> planData) {
        PlanData = planData;
    }
}

package com.tourcoo.training.entity.study;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2020年05月08日9:06
 * @Email: 971613168@qq.com
 */
public class StudyDataInfo implements Serializable {
    private String Time;
    private long StudyHour;
    private ArrayList<StudyDataInfo> Day;

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public long getStudyHour() {
        return StudyHour;
    }

    public void setStudyHour(long studyHour) {
        StudyHour = studyHour;
    }

    public ArrayList<StudyDataInfo> getDay() {
        return Day;
    }

    public void setDay(ArrayList<StudyDataInfo> day) {
        Day = day;
    }
}

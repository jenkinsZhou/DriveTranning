package com.tourcoo.training.entity.study;

import java.io.Serializable;
import java.util.List;

/**
 * @author :JenkinsZhou
 * @description :学习数据
 * @company :途酷科技
 * @date 2020年05月08日9:02
 * @Email: 971613168@qq.com
 */
public class StudyDataEntity implements Serializable {


    /**
     * TotalHour : 25826
     * Months : [{"Time":"2020-01","StudyHour":0,"Day":[]},{"Time":"2020-02","StudyHour":0,"Day":[]},{"Time":"2020-03","StudyHour":0,"Day":[]},{"Time":"2020-04","StudyHour":23272,"Day":[{"Time":"2020-04-21","StudyHour":9},{"Time":"2020-04-22","StudyHour":6474},{"Time":"2020-04-23","StudyHour":969},{"Time":"2020-04-24","StudyHour":10977},{"Time":"2020-04-25","StudyHour":1163},{"Time":"2020-04-26","StudyHour":2892},{"Time":"2020-04-27","StudyHour":414},{"Time":"2020-04-28","StudyHour":118},{"Time":"2020-04-29","StudyHour":196},{"Time":"2020-04-30","StudyHour":60}]},{"Time":"2020-05","StudyHour":2554,"Day":[{"Time":"2020-05-06","StudyHour":1460},{"Time":"2020-05-07","StudyHour":1094}]},{"Time":"2020-06","StudyHour":0,"Day":[]},{"Time":"2020-07","StudyHour":0,"Day":[]},{"Time":"2020-08","StudyHour":0,"Day":[]},{"Time":"2020-09","StudyHour":0,"Day":[]},{"Time":"2020-10","StudyHour":0,"Day":[]},{"Time":"2020-11","StudyHour":0,"Day":[]},{"Time":"2020-12","StudyHour":0,"Day":[]}]
     */

    private double TotalHour;
    private List<StudyDataInfo> Months;

    public double getTotalHour() {
        return TotalHour;
    }

    public void setTotalHour(double TotalHour) {
        this.TotalHour = TotalHour;
    }

    public List<StudyDataInfo> getMonths() {
        return Months;
    }

    public void setMonths(List<StudyDataInfo> months) {
        Months = months;
    }
}

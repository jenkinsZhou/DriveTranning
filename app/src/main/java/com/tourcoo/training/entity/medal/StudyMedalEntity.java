package com.tourcoo.training.entity.medal;

import java.util.List;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2020年05月06日15:57
 * @Email: 971613168@qq.com
 */
public class StudyMedalEntity {


    /**
     * CurrentNum : 2
     * StudyMedals : [{"ID":1001,"Status":1},{"ID":1002,"Status":1},{"ID":1003,"Status":0},{"ID":1004,"Status":0},{"ID":1005,"Status":0}]
     * CertificateMedals : [{"ID":2001,"Status":0},{"ID":2002,"Status":0},{"ID":2003,"Status":0},{"ID":2004,"Status":0},{"ID":2005,"Status":0}]
     * ConsumptionMedals : [{"ID":3001,"Status":0},{"ID":3002,"Status":0},{"ID":3003,"Status":0},{"ID":3004,"Status":0},{"ID":3005,"Status":0}]
     */

    private int CurrentNum;
    private List<MedalInfo> StudyMedals;
    private List<MedalInfo> CertificateMedals;
    private List<MedalInfo> ConsumptionMedals;

    public int getCurrentNum() {
        return CurrentNum;
    }

    public void setCurrentNum(int CurrentNum) {
        this.CurrentNum = CurrentNum;
    }

    public List<MedalInfo> getStudyMedals() {
        return StudyMedals;
    }

    public void setStudyMedals(List<MedalInfo> StudyMedals) {
        this.StudyMedals = StudyMedals;
    }

    public List<MedalInfo> getCertificateMedals() {
        return CertificateMedals;
    }

    public void setCertificateMedals(List<MedalInfo> CertificateMedals) {
        this.CertificateMedals = CertificateMedals;
    }

    public List<MedalInfo> getConsumptionMedals() {
        return ConsumptionMedals;
    }

    public void setConsumptionMedals(List<MedalInfo> ConsumptionMedals) {
        this.ConsumptionMedals = ConsumptionMedals;
    }


}

package com.tourcoo.training.entity.recognize;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2020年04月15日23:07
 * @Email: 971613168@qq.com
 */
public class FaceRecognizeResult {


    /**
     * Passing : 1
     * Confidence : 99.99999237060547
     * LiveFaceConfidence : 0
     */

    private String Passing;
    private double Confidence;
    private String LiveFaceConfidence;

    public String getPassing() {
        return Passing;
    }

    public void setPassing(String Passing) {
        this.Passing = Passing;
    }

    public double getConfidence() {
        return Confidence;
    }

    public void setConfidence(double Confidence) {
        this.Confidence = Confidence;
    }

    public String getLiveFaceConfidence() {
        return LiveFaceConfidence;
    }

    public void setLiveFaceConfidence(String LiveFaceConfidence) {
        this.LiveFaceConfidence = LiveFaceConfidence;
    }
}

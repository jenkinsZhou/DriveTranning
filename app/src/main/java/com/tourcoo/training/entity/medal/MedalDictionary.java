package com.tourcoo.training.entity.medal;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2020年05月11日10:24
 * @Email: 971613168@qq.com
 */
public class MedalDictionary {


    /**
     * hour : 12
     * certificate : 3
     * consume : 0.84
     */

    private String hour;
    private String certificate;
    private double consume;

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getCertificate() {
        return certificate;
    }

    public void setCertificate(String certificate) {
        this.certificate = certificate;
    }

    public double getConsume() {
        return consume;
    }

    public void setConsume(double consume) {
        this.consume = consume;
    }
}

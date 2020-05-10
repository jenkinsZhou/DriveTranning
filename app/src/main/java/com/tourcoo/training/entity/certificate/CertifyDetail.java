package com.tourcoo.training.entity.certificate;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2020年05月10日17:10
 * @Email: 971613168@qq.com
 */
public class CertifyDetail {


    /**
     * TrainingPlanID : 9047
     * Avatar : http://cdn-runtime.ggjtaq.com/face/20200416/33496-fe5022d2099813923ceeb647b3b999cf2a4335e4.png
     * CertificateImage : http://cdn-runtime.ggjtaq.com/certificate/9047-33496/5dbfd0b3251e23e8dc56f36644f8b319bda940ac.jpeg
     */

    private int TrainingPlanID;
    private String Avatar;
    private String CertificateImage;

    public int getTrainingPlanID() {
        return TrainingPlanID;
    }

    public void setTrainingPlanID(int TrainingPlanID) {
        this.TrainingPlanID = TrainingPlanID;
    }

    public String getAvatar() {
        return Avatar;
    }

    public void setAvatar(String Avatar) {
        this.Avatar = Avatar;
    }

    public String getCertificateImage() {
        return CertificateImage;
    }

    public void setCertificateImage(String CertificateImage) {
        this.CertificateImage = CertificateImage;
    }
}

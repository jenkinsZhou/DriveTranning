package com.tourcoo.training.entity.certificate;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.Date;

import static com.tourcoo.training.adapter.certificate.CertificateInfoAdapter.ITEM_TYPE_CONTENT;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2020年04月15日9:15
 * @Email: 971613168@qq.com
 */
public class CertificateInfo implements MultiItemEntity {


    /**
     * TrainingPlanName : 测试4人
     * TrainingPlanID : 3431
     * Id : 3431-33496
     * CertificateNumber : 33496
     * Url : null
     * CertificateTime:yyyy-mm-dd
     */
    private String CreateTime;
    private String TrainingPlanName;
    private int TrainingPlanID;
    private String Id;
    private String CertificateNumber;
    private String Url;
    private int  itemType = ITEM_TYPE_CONTENT ;
    private Date date;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    private String CertificateTime ="2019-12-03" ;

    public String getCertificateTime() {
        return CertificateTime;
    }

    public void setCertificateTime(String certificateTime) {
        CertificateTime = certificateTime;
    }

    private String headerContent;

    public String getHeaderContent() {
        return headerContent;
    }

    public void setHeaderContent(String headerContent) {
        this.headerContent = headerContent;
    }

    private boolean header;

    public boolean isHeader() {
        return header;
    }

    public void setHeader(boolean header) {
        this.header = header;
    }

    public String getTrainingPlanName() {
        return TrainingPlanName;
    }

    public void setTrainingPlanName(String TrainingPlanName) {
        this.TrainingPlanName = TrainingPlanName;
    }

    public int getTrainingPlanID() {
        return TrainingPlanID;
    }

    public void setTrainingPlanID(int TrainingPlanID) {
        this.TrainingPlanID = TrainingPlanID;
    }

    public String getId() {
        return Id;
    }

    public void setId(String Id) {
        this.Id = Id;
    }

    public String getCertificateNumber() {
        return CertificateNumber;
    }

    public void setCertificateNumber(String CertificateNumber) {
        this.CertificateNumber = CertificateNumber;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String Url) {
        this.Url = Url;
    }

    @Override
    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public String getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(String createTime) {
        CreateTime = createTime;
    }
}

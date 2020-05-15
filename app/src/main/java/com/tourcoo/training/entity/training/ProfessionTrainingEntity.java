package com.tourcoo.training.entity.training;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2020年03月10日23:08
 * @Email: 971613168@qq.com
 */
public class ProfessionTrainingEntity {


    /**
     * ID : 1
     * Name : 专项2020.04.28.01
     * NumberofQuestions : 10
     * STime : 2020-04-28 00:00:00
     * ETime : 2020-05-31 00:00:00
     * CoverUrl : http://cdn-runtime.ggjtaq.com/upload/special/20200428/d8cff1b211a6be96412c6d103e864697556b2de3.jpeg
     * CreatorID : 0000000000002512
     * CreateTime : 2020-04-28 10:37:58
     */

    private String ID;
    private String Name;
    private String NumberofQuestions;
    private String STime;
    private String ETime;
    private String CoverUrl;
    private String CreatorID;
    private String CreateTime;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getNumberofQuestions() {
        return NumberofQuestions;
    }

    public void setNumberofQuestions(String NumberofQuestions) {
        this.NumberofQuestions = NumberofQuestions;
    }

    public String getSTime() {
        return STime;
    }

    public void setSTime(String STime) {
        this.STime = STime;
    }

    public String getETime() {
        return ETime;
    }

    public void setETime(String ETime) {
        this.ETime = ETime;
    }

    public String getCoverUrl() {
        return CoverUrl;
    }

    public void setCoverUrl(String CoverUrl) {
        this.CoverUrl = CoverUrl;
    }

    public String getCreatorID() {
        return CreatorID;
    }

    public void setCreatorID(String CreatorID) {
        this.CreatorID = CreatorID;
    }

    public String getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(String CreateTime) {
        this.CreateTime = CreateTime;
    }
}

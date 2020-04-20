package com.tourcoo.training.entity.training;

import android.text.TextUtils;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2020年04月20日9:44
 * @Email: 971613168@qq.com
 */
public class VideoStream {
    /**
     * CourseID : 612008
     * Bitrate : 0
     * Definition : SD
     * Duration : 201
     * Encrypt : 0
     * EncryptType : null
     * URL : http://cdn.course.ggjtaq.com/720/renmenjiaotongchubansheshuzikejian/weixianhuowudaoluyunshujiashiyuananquanjiaoyupeixunshuzikechengbanben/diyizhangjiashiyuandeshehuizerenyuzhiyedaode/diyijiedaoluyunshujiashiyuandezhiyetedian/jiaotongshigudeweihai/jiaotongshigudeweihai.mp4
     * Width : 1280
     * Height : 720
     */

    private int CourseID;
    private int Bitrate;
    private String Definition;
    private int Duration;
    private int Encrypt;
    private String EncryptType;
    private String URL;
    private int Width;
    private int Height;
    private String DefinitionDesc;

    public String getDefinitionDesc() {
        if(TextUtils.isEmpty(Definition)){
            return "默认";
        }
        switch (DefinitionDesc) {
            case "SD":
                return "SD";
            case "LD":
                return "LD";
            default:
                return "默认";
        }
    }

    public void setDefinitionDesc(String definitionDesc) {
        DefinitionDesc = definitionDesc;
    }

    public int getCourseID() {
        return CourseID;
    }

    public void setCourseID(int CourseID) {
        this.CourseID = CourseID;
    }

    public int getBitrate() {
        return Bitrate;
    }

    public void setBitrate(int Bitrate) {
        this.Bitrate = Bitrate;
    }

    public String getDefinition() {
        return Definition;
    }

    public void setDefinition(String Definition) {
        this.Definition = Definition;
    }

    public int getDuration() {
        return Duration;
    }

    public void setDuration(int Duration) {
        this.Duration = Duration;
    }

    public int getEncrypt() {
        return Encrypt;
    }

    public void setEncrypt(int Encrypt) {
        this.Encrypt = Encrypt;
    }

    public String getEncryptType() {
        return EncryptType;
    }

    public void setEncryptType(String EncryptType) {
        this.EncryptType = EncryptType;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public int getWidth() {
        return Width;
    }

    public void setWidth(int Width) {
        this.Width = Width;
    }

    public int getHeight() {
        return Height;
    }

    public void setHeight(int Height) {
        this.Height = Height;
    }
}

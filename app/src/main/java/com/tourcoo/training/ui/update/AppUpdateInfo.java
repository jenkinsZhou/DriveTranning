package com.tourcoo.training.ui.update;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2020年05月13日21:01
 * @Email: 971613168@qq.com
 */
public class AppUpdateInfo {


    /**
     * IsUpdate : 1
     * IsMandatoryUpdate : 0
     * VersionName : v1.0.0
     * Content :
     * Link :
     */

    private int IsUpdate;
    private int IsMandatoryUpdate;
    private String VersionName;
    private String Content;
    private String Link;

    public int getIsUpdate() {
        return IsUpdate;
    }

    public void setIsUpdate(int IsUpdate) {
        this.IsUpdate = IsUpdate;
    }

    public int getIsMandatoryUpdate() {
        return IsMandatoryUpdate;
    }

    public void setIsMandatoryUpdate(int IsMandatoryUpdate) {
        this.IsMandatoryUpdate = IsMandatoryUpdate;
    }

    public String getVersionName() {
        return VersionName;
    }

    public void setVersionName(String VersionName) {
        this.VersionName = VersionName;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String Content) {
        this.Content = Content;
    }

    public String getLink() {
        return Link;
    }

    public void setLink(String Link) {
        this.Link = Link;
    }
}

package com.tourcoo.training.entity.uploads;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2020年05月09日8:32
 * @Email: 971613168@qq.com
 */
public class UploadResultEntity {


    /**
     * Name : ic_card.jpg
     * Url : http://cdn-runtime.ggjtaq.com/upload/20200509/7f84a9b40e3d2cc0f1d9f6605a8894d118f2964b.jpg
     * FileID : 7eb90c96bb9da14b002f16f7cbb50318a9e67f04
     */

    private String Name;
    private String Url;
    private String FileID;

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String Url) {
        this.Url = Url;
    }

    public String getFileID() {
        return FileID;
    }

    public void setFileID(String FileID) {
        this.FileID = FileID;
    }
}

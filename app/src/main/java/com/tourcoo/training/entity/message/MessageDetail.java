package com.tourcoo.training.entity.message;

import java.util.List;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2020年05月08日20:29
 * @Email: 971613168@qq.com
 */
public class MessageDetail {


    /**
     * Content : <p>测试2020.4.22.02</p>
     * Time : 2020-04-22 09:31:43
     * ID : 525
     * Title : 测试2020.4.22.02
     * file : []
     */

    private String Content;
    private String Time;
    private int ID;
    private String Title;
    private List<?> file;

    public String getContent() {
        return Content;
    }

    public void setContent(String Content) {
        this.Content = Content;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String Time) {
        this.Time = Time;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String Title) {
        this.Title = Title;
    }

    public List<?> getFile() {
        return file;
    }

    public void setFile(List<?> file) {
        this.file = file;
    }
}

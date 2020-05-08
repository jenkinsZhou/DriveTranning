package com.tourcoo.training.entity.message;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2020年05月08日17:00
 * @Email: 971613168@qq.com
 */
public class MessageEntity {


    /**
     * CreateTime : 2020-04-24 14:06:46
     * ID : 535
     * Receipt : 2020-04-24 14:59:42
     * Content : <p>1111111111111</p>
     * Title : 0424公告转发
     * IsRead : 1
     */

    private String CreateTime;
    private int ID;
    private String Receipt;
    private String Content;
    private String Title;
    private int IsRead;

    public String getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(String CreateTime) {
        this.CreateTime = CreateTime;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getReceipt() {
        return Receipt;
    }

    public void setReceipt(String Receipt) {
        this.Receipt = Receipt;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String Content) {
        this.Content = Content;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String Title) {
        this.Title = Title;
    }

    public int getIsRead() {
        return IsRead;
    }

    public void setIsRead(int IsRead) {
        this.IsRead = IsRead;
    }
}

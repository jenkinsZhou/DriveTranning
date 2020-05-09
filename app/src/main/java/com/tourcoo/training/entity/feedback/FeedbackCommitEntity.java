package com.tourcoo.training.entity.feedback;

import java.util.List;

/**
 * @author :JenkinsZhou
 * @description :提交问题反馈的对象
 * @company :途酷科技
 * @date 2020年05月09日9:07
 * @Email: 971613168@qq.com
 */
public class FeedbackCommitEntity {


    /**
     * id : 99
     * content : 问题反馈其他问题
     * phone : 15755178971
     * photo : ["//cdn.other.ggjtaq.com/document/20181101082542766978494.png","//cdn.other.ggjtaq.com/document/20181101082542766978494.png","//cdn.other.ggjtaq.com/document/20181101082542766978494.png"]
     */

    private int id;
    private String content;
    private String phone;
    private List<String> photo;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<String> getPhoto() {
        return photo;
    }

    public void setPhoto(List<String> photo) {
        this.photo = photo;
    }
}

package com.tourcoo.training.entity.exam;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2020年04月16日16:26
 * @Email: 971613168@qq.com
 */
public class CommitAnswer {

    /**
     * ID : 2221649
     * Answer : A
     */
    private String Answer;
    private String ID;
    @JSONField(name = "Answer")
    public String getAnswer() {
        return Answer;
    }


    @JSONField(name = "ID")
    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }


    public void setAnswer(String Answer) {
        this.Answer = Answer;
    }
}

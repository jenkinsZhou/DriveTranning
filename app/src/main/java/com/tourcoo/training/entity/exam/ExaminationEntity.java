package com.tourcoo.training.entity.exam;

import java.util.List;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2020年03月12日9:34
 * @Email: 971613168@qq.com
 */
public class ExaminationEntity {
    public static final String QUESTION_TYPE_SINGLE = "1";
    public static final String QUESTION_TYPE_MULTIPLE = "2";
    public static final String QUESTION_TYPE_JUDGE = "3";

    /**
     * QuestionId : 题目Id
     * QuestionContent : 题目内容
     * HasAnswered : 是否已选择过答案（Boolean）
     * QuestionType : 1
     * AnswerList : [{"AnswerId":"1","AnswerContent":"选项答案选项答案选项答案"},{"AnswerId":"2","AnswerContent":"选项答案选项答案选项答案"},{"AnswerId":"3","AnswerContent":"选项答案选项答案选项答案"},{"AnswerId":"4","AnswerContent":"选项答案选项答案选项答案"}]
     * CorrectAnswerList : [{"AnswerId":"1","AnswerContent":"选项答案选项答案选项答案"}]
     */

    private String QuestionId;
    private String QuestionContent;
    private boolean HasAnswered;
    private String QuestionType;

    public String getQuestionType() {
        return QuestionType;
    }

    public void setQuestionType(String questionType) {
        QuestionType = questionType;
    }

    private List<Answer> AnswerList;
    private List<Answer> CorrectAnswerList;

    public String getQuestionId() {
        return QuestionId;
    }

    public void setQuestionId(String QuestionId) {
        this.QuestionId = QuestionId;
    }

    public String getQuestionContent() {
        return QuestionContent;
    }

    public void setQuestionContent(String QuestionContent) {
        this.QuestionContent = QuestionContent;
    }

    public boolean isHasAnswered() {
        return HasAnswered;
    }

    public void setHasAnswered(boolean hasAnswered) {
        HasAnswered = hasAnswered;
    }

    public List<Answer> getAnswerList() {
        return AnswerList;
    }

    public void setAnswerList(List<Answer> answerList) {
        AnswerList = answerList;
    }

    public List<Answer> getCorrectAnswerList() {
        return CorrectAnswerList;
    }

    public void setCorrectAnswerList(List<Answer> correctAnswerList) {
        CorrectAnswerList = correctAnswerList;
    }
}

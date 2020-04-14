package com.tourcoo.training.entity.exam;

import java.util.List;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2020年03月12日9:34
 * @Email: 971613168@qq.com
 */
public class ExaminationEntityOld {


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

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    /**
     * 题目编号
     */
    private int number;
    /**
     * 是否回答正确
     */
    private boolean correct;

    public boolean isCorrect() {
        return correct;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }

    private String QuestionType;

    public String getQuestionType() {
        return QuestionType;
    }

    public void setQuestionType(String questionType) {
        QuestionType = questionType;
    }

    private List<AnswerOld> answerOldList;
    private List<AnswerOld> correctAnswerOldList;

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

    public List<AnswerOld> getAnswerOldList() {
        return answerOldList;
    }

    public void setAnswerOldList(List<AnswerOld> answerOldList) {
        this.answerOldList = answerOldList;
    }

    public List<AnswerOld> getCorrectAnswerOldList() {
        return correctAnswerOldList;
    }

    public void setCorrectAnswerOldList(List<AnswerOld> correctAnswerOldList) {
        this.correctAnswerOldList = correctAnswerOldList;
    }
}

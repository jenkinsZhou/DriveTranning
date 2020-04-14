package com.tourcoo.training.entity.exam;

import java.util.List;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2020年04月14日15:56
 * @Email: 971613168@qq.com
 */
public class Question {


    /**
     * ID : 2221654
     * Type : 2
     * Question : 以下哪种行为不属于道路运输优质服务的内容？
     * ImageUrl :
     * MaxScore : null
     * Analysis :
     * CorrectAnswer : ["D"]
     * AnswerItems : [{"AnswerId":"A","AnswerName":"建立服务规范"},{"AnswerId":"B","AnswerName":"保障乘客和托运人利益"},{"AnswerId":"C","AnswerName":"使用文明礼貎用语"},{"AnswerId":"D","AnswerName":"保证运营利润"}]
     * Answer : ["D"]
     * CreateTime : 2020-04-07 10:40:08
     * Score : null
     */

    private int ID;
    private int Type;
    private String Question;
    private String ImageUrl;
    private String MaxScore;
    private String Analysis;
    private String CreateTime;
    private String Score;
    private List<String> CorrectAnswer;
    private List<Answer> AnswerItems;
    private List<String> Answer;
    private String questionNumber;

    public String getQuestionNumber() {
        return questionNumber;
    }

    public void setQuestionNumber(String questionNumber) {
        this.questionNumber = questionNumber;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getType() {
        return Type;
    }

    public void setType(int Type) {
        this.Type = Type;
    }

    public String getQuestion() {
        return Question;
    }

    public void setQuestion(String Question) {
        this.Question = Question;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String ImageUrl) {
        this.ImageUrl = ImageUrl;
    }

    public String getMaxScore() {
        return MaxScore;
    }

    public void setMaxScore(String MaxScore) {
        this.MaxScore = MaxScore;
    }

    public String getAnalysis() {
        return Analysis;
    }

    public void setAnalysis(String Analysis) {
        this.Analysis = Analysis;
    }

    public String getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(String CreateTime) {
        this.CreateTime = CreateTime;
    }

    public String getScore() {
        return Score;
    }

    public void setScore(String Score) {
        this.Score = Score;
    }

    public List<String> getCorrectAnswer() {
        return CorrectAnswer;
    }

    public void setCorrectAnswer(List<String> CorrectAnswer) {
        this.CorrectAnswer = CorrectAnswer;
    }

    public List<Answer> getAnswerItems() {
        return AnswerItems;
    }

    public void setAnswerItems(List<Answer> AnswerItems) {
        this.AnswerItems = AnswerItems;
    }

    public List<String> getAnswer() {
        return Answer;
    }

    public void setAnswer(List<String> Answer) {
        this.Answer = Answer;
    }


}

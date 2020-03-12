package com.tourcoo.training.entity.exam;


/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2020年03月12日9:43
 * @Email: 971613168@qq.com
 */
public class Answer {

    /**
     * AnswerId : 1
     * AnswerContent : 选项答案选项答案选项答案
     */

    private String AnswerId;
    private String AnswerContent;

    private boolean canMultiSelect;

    public boolean isCanMultiSelect() {
        return canMultiSelect;
    }


    public void setCanMultiSelect(boolean canMultiSelect) {
        this.canMultiSelect = canMultiSelect;
    }

    /**
     * 是否被选中
     */
    private boolean selected;

    private boolean hasAnswered;


    private int selectedIcon;

    private int unSelectedIcon;

    public int getSelectedIcon() {
        return selectedIcon;
    }

    public int getUnSelectedIcon() {
        return unSelectedIcon;
    }

    public void setUnSelectedIcon(int unSelectedIcon) {
        this.unSelectedIcon = unSelectedIcon;
    }

    public void setSelectedIcon(int selectedIcon) {
        this.selectedIcon = selectedIcon;
    }

    /**
     * 是否是正确答案
     * @return
     */
    public boolean isCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(boolean correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    private boolean correctAnswer;


    public boolean isHasAnswered() {
        return hasAnswered;
    }

    public void setHasAnswered(boolean hasAnswered) {
        this.hasAnswered = hasAnswered;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getAnswerId() {
        return AnswerId;
    }

    public void setAnswerId(String AnswerId) {
        this.AnswerId = AnswerId;
    }

    public String getAnswerContent() {
        return AnswerContent;
    }

    public void setAnswerContent(String AnswerContent) {
        this.AnswerContent = AnswerContent;
    }


}

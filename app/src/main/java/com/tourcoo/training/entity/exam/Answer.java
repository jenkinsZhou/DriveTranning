package com.tourcoo.training.entity.exam;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2020年04月14日15:54
 * @Email: 971613168@qq.com
 */
public class Answer implements Parcelable {
    /**
     * AnswerId : A
     * AnswerName : 建立服务规范
     */

    private String AnswerId;
    private String AnswerName;

    private boolean select;

    private boolean canMultiSelect;

    private boolean correctAnswer;

    private boolean hasAnswered;


    private int selectedIcon;

    private int unSelectedIcon;

    public boolean isHasAnswered() {
        return hasAnswered;
    }

    public void setHasAnswered(boolean hasAnswered) {
        this.hasAnswered = hasAnswered;
    }

    public int getSelectedIcon() {
        return selectedIcon;
    }

    public void setSelectedIcon(int selectedIcon) {
        this.selectedIcon = selectedIcon;
    }

    public int getUnSelectedIcon() {
        return unSelectedIcon;
    }

    public void setUnSelectedIcon(int unSelectedIcon) {
        this.unSelectedIcon = unSelectedIcon;
    }

    public boolean isCanMultiSelect() {
        return canMultiSelect;
    }


    public void setCanMultiSelect(boolean canMultiSelect) {
        this.canMultiSelect = canMultiSelect;
    }


    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    public String getAnswerId() {
        return AnswerId;
    }

    public void setAnswerId(String AnswerId) {
        this.AnswerId = AnswerId;
    }

    public String getAnswerName() {
        return AnswerName;
    }

    public void setAnswerName(String AnswerName) {
        this.AnswerName = AnswerName;
    }

    public boolean isCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(boolean correctAnswer) {
        this.correctAnswer = correctAnswer;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.AnswerId);
        dest.writeString(this.AnswerName);
        dest.writeByte(this.select ? (byte) 1 : (byte) 0);
        dest.writeByte(this.canMultiSelect ? (byte) 1 : (byte) 0);
        dest.writeByte(this.correctAnswer ? (byte) 1 : (byte) 0);
        dest.writeByte(this.hasAnswered ? (byte) 1 : (byte) 0);
        dest.writeInt(this.selectedIcon);
        dest.writeInt(this.unSelectedIcon);
    }

    public Answer() {
    }

    protected Answer(Parcel in) {
        this.AnswerId = in.readString();
        this.AnswerName = in.readString();
        this.select = in.readByte() != 0;
        this.canMultiSelect = in.readByte() != 0;
        this.correctAnswer = in.readByte() != 0;
        this.hasAnswered = in.readByte() != 0;
        this.selectedIcon = in.readInt();
        this.unSelectedIcon = in.readInt();
    }

    public static final Creator<Answer> CREATOR = new Creator<Answer>() {
        @Override
        public Answer createFromParcel(Parcel source) {
            return new Answer(source);
        }

        @Override
        public Answer[] newArray(int size) {
            return new Answer[size];
        }
    };
}

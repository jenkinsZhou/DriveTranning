package com.tourcoo.training.entity.training;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2020年04月23日15:43
 * @Email: 971613168@qq.com
 */
public class QrScanResult implements Parcelable {


    /**
     * Scene : 121
     * EventId : fsvlojeq050f86e71jm2bcgs2f
     * TrainingPlanID : 9093
     * CTime : 1587556588
     * Duration : 180
     */

    private String Scene;
    private String EventId;
    private String TrainingPlanID;
    private String CTime;
    private String Duration;

    public String getScene() {
        return Scene;
    }

    public void setScene(String Scene) {
        this.Scene = Scene;
    }

    public String getEventId() {
        return EventId;
    }

    public void setEventId(String EventId) {
        this.EventId = EventId;
    }

    public String getTrainingPlanID() {
        return TrainingPlanID;
    }

    public void setTrainingPlanID(String TrainingPlanID) {
        this.TrainingPlanID = TrainingPlanID;
    }

    public String getCTime() {
        return CTime;
    }

    public void setCTime(String CTime) {
        this.CTime = CTime;
    }

    public String getDuration() {
        return Duration;
    }

    public void setDuration(String Duration) {
        this.Duration = Duration;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.Scene);
        dest.writeString(this.EventId);
        dest.writeString(this.TrainingPlanID);
        dest.writeString(this.CTime);
        dest.writeString(this.Duration);
    }

    public QrScanResult() {
    }

    protected QrScanResult(Parcel in) {
        this.Scene = in.readString();
        this.EventId = in.readString();
        this.TrainingPlanID = in.readString();
        this.CTime = in.readString();
        this.Duration = in.readString();
    }

    public static final Parcelable.Creator<QrScanResult> CREATOR = new Parcelable.Creator<QrScanResult>() {
        @Override
        public QrScanResult createFromParcel(Parcel source) {
            return new QrScanResult(source);
        }

        @Override
        public QrScanResult[] newArray(int size) {
            return new QrScanResult[size];
        }
    };
}

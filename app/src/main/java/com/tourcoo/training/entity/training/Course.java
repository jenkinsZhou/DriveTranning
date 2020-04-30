package com.tourcoo.training.entity.training;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * @author :JenkinsZhou
 * @description :课程实体
 *  @company :途酷科技
 * @date 2020年04月20日9:46
 * @Email: 971613168@qq.com
 */
public class Course implements Parcelable {

    /**
     * ID : 612010
     * PID : 612009
     * Name : 1.道路运输驾驶员的职业特点.mp4
     * SN : 1
     * Remark : null
     * Enabled : 1
     * Duration : 284
     * CoverURL : null
     * MediaUrl : renmenjiaotongchubansheshuzikejian/weixianhuowudaoluyunshujiashiyuananquanjiaoyupeixunshuzikechengbanben/diyizhangjiashiyuandeshehuizerenyuzhiyedaode/diyijiedaoluyunshujiashiyuandezhiyetedian/daoluyunshujiashiyuandezhiyetedian/daoluyunshujiashiyuandezhiyetedian.mp4
     * MediaType : 0
     * Level : 4
     * subjectName : 危险货物道路运输驾驶员安全教育培训数字课程-交通出版社1.0版本
     * SubjectID : 1062
     * VideoID : null
     * Progress : 0
     * Completed : 0
     * ThirdOrderID : null
     * Streams : [{"CourseID":612010,"Bitrate":0,"Definition":"SD","Duration":284,"Encrypt":0,"EncryptType":null,"URL":"http://cdn.course.ggjtaq.com/720/renmenjiaotongchubansheshuzikejian/weixianhuowudaoluyunshujiashiyuananquanjiaoyupeixunshuzikechengbanben/diyizhangjiashiyuandeshehuizerenyuzhiyedaode/diyijiedaoluyunshujiashiyuandezhiyetedian/daoluyunshujiashiyuandezhiyetedian/daoluyunshujiashiyuandezhiyetedian.mp4","Width":1280,"Height":720},{"CourseID":612010,"Bitrate":0,"Definition":"HD","Duration":284,"Encrypt":0,"EncryptType":null,"URL":"http://cdn.course.ggjtaq.com/1080/renmenjiaotongchubansheshuzikejian/weixianhuowudaoluyunshujiashiyuananquanjiaoyupeixunshuzikechengbanben/diyizhangjiashiyuandeshehuizerenyuzhiyedaode/diyijiedaoluyunshujiashiyuandezhiyetedian/daoluyunshujiashiyuandezhiyetedian/daoluyunshujiashiyuandezhiyetedian.mp4","Width":1920,"Height":1080},{"CourseID":612010,"Bitrate":0,"Definition":"LD","Duration":284,"Encrypt":0,"EncryptType":null,"URL":"http://cdn.course.ggjtaq.com/540/renmenjiaotongchubansheshuzikejian/weixianhuowudaoluyunshujiashiyuananquanjiaoyupeixunshuzikechengbanben/diyizhangjiashiyuandeshehuizerenyuzhiyedaode/diyijiedaoluyunshujiashiyuandezhiyetedian/daoluyunshujiashiyuandezhiyetedian/daoluyunshujiashiyuandezhiyetedian.mp4","Width":960,"Height":540},{"CourseID":612010,"Bitrate":0,"Definition":"FD","Duration":284,"Encrypt":0,"EncryptType":null,"URL":"http://cdn.course.ggjtaq.com/360/renmenjiaotongchubansheshuzikejian/weixianhuowudaoluyunshujiashiyuananquanjiaoyupeixunshuzikechengbanben/diyizhangjiashiyuandeshehuizerenyuzhiyedaode/diyijiedaoluyunshujiashiyuandezhiyetedian/daoluyunshujiashiyuandezhiyetedian/daoluyunshujiashiyuandezhiyetedian.mp4","Width":640,"Height":360}]
     */

    private int ID;
    private int PID;
    private String Name;
    private int SN;
    private String Remark;
    private int Enabled;
    private int Duration;
    private String CoverURL;
    private String MediaUrl;
    private int MediaType;
    private int Level;
    private String subjectName;
    private int SubjectID;
    private String VideoID;
    private int Progress;
    private int Completed;
    private String ThirdOrderID;
    private List<VideoStream> Streams;
    private int currentPlayStatus;
    private VideoStream currentVideoStream;

    public int getCurrentPlayStatus() {
        return currentPlayStatus;
    }

    public void setCurrentPlayStatus(int currentPlayStatus) {
        this.currentPlayStatus = currentPlayStatus;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getPID() {
        return PID;
    }

    public void setPID(int PID) {
        this.PID = PID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public int getSN() {
        return SN;
    }

    public void setSN(int SN) {
        this.SN = SN;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String Remark) {
        this.Remark = Remark;
    }

    public int getEnabled() {
        return Enabled;
    }

    public void setEnabled(int Enabled) {
        this.Enabled = Enabled;
    }

    public int getDuration() {
        return Duration;
    }

    public void setDuration(int Duration) {
        this.Duration = Duration;
    }

    public String getCoverURL() {
        return CoverURL;
    }

    public void setCoverURL(String CoverURL) {
        this.CoverURL = CoverURL;
    }

    public String getMediaUrl() {
        return MediaUrl;
    }

    public void setMediaUrl(String MediaUrl) {
        this.MediaUrl = MediaUrl;
    }

    public int getMediaType() {
        return MediaType;
    }

    public void setMediaType(int MediaType) {
        this.MediaType = MediaType;
    }

    public int getLevel() {
        return Level;
    }

    public void setLevel(int Level) {
        this.Level = Level;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public int getSubjectID() {
        return SubjectID;
    }

    public void setSubjectID(int SubjectID) {
        this.SubjectID = SubjectID;
    }

    public String getVideoID() {
        return VideoID;
    }

    public void setVideoID(String VideoID) {
        this.VideoID = VideoID;
    }

    public int getProgress() {
        return Progress;
    }

    public void setProgress(int Progress) {
        this.Progress = Progress;
    }

    public int getCompleted() {
        return Completed;
    }

    public void setCompleted(int Completed) {
        this.Completed = Completed;
    }

    public String getThirdOrderID() {
        return ThirdOrderID;
    }

    public void setThirdOrderID(String ThirdOrderID) {
        this.ThirdOrderID = ThirdOrderID;
    }

    public List<VideoStream> getStreams() {
        return Streams;
    }

    public void setStreams(List<VideoStream> Streams) {
        this.Streams = Streams;
    }

    public VideoStream getCurrentVideoStream() {
        return currentVideoStream;
    }

    public void setCurrentVideoStream(VideoStream currentVideoStream) {
        this.currentVideoStream = currentVideoStream;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.ID);
        dest.writeInt(this.PID);
        dest.writeString(this.Name);
        dest.writeInt(this.SN);
        dest.writeString(this.Remark);
        dest.writeInt(this.Enabled);
        dest.writeInt(this.Duration);
        dest.writeString(this.CoverURL);
        dest.writeString(this.MediaUrl);
        dest.writeInt(this.MediaType);
        dest.writeInt(this.Level);
        dest.writeString(this.subjectName);
        dest.writeInt(this.SubjectID);
        dest.writeString(this.VideoID);
        dest.writeInt(this.Progress);
        dest.writeInt(this.Completed);
        dest.writeString(this.ThirdOrderID);
        dest.writeTypedList(this.Streams);
        dest.writeInt(this.currentPlayStatus);
        dest.writeParcelable(this.currentVideoStream, flags);
    }

    public Course() {
    }

    protected Course(Parcel in) {
        this.ID = in.readInt();
        this.PID = in.readInt();
        this.Name = in.readString();
        this.SN = in.readInt();
        this.Remark = in.readString();
        this.Enabled = in.readInt();
        this.Duration = in.readInt();
        this.CoverURL = in.readString();
        this.MediaUrl = in.readString();
        this.MediaType = in.readInt();
        this.Level = in.readInt();
        this.subjectName = in.readString();
        this.SubjectID = in.readInt();
        this.VideoID = in.readString();
        this.Progress = in.readInt();
        this.Completed = in.readInt();
        this.ThirdOrderID = in.readString();
        this.Streams = in.createTypedArrayList(VideoStream.CREATOR);
        this.currentPlayStatus = in.readInt();
        this.currentVideoStream = in.readParcelable(VideoStream.class.getClassLoader());
    }

    public static final Parcelable.Creator<Course> CREATOR = new Parcelable.Creator<Course>() {
        @Override
        public Course createFromParcel(Parcel source) {
            return new Course(source);
        }

        @Override
        public Course[] newArray(int size) {
            return new Course[size];
        }
    };
}

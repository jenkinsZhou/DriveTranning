package com.tourcoo.training.entity.account;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2020年04月07日16:21
 * @Email: 971613168@qq.com
 */
public class IdCardInfo implements Parcelable {


    /**
     * Name : 潘双全
     * IDCard : 510132198606091238
     * PhotoID : 42
     * Url : http://cdn-runtime.ggjtaq.com/id-card/20200407/dca47de13f53baeee791f1f7e918e3c0362face3.jpg
     */

    private String Name;
    private String IDCard;
    private String PhotoID;
    private String Url;

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getIDCard() {
        return IDCard;
    }

    public void setIDCard(String IDCard) {
        this.IDCard = IDCard;
    }

    public String getPhotoID() {
        return PhotoID;
    }

    public void setPhotoID(String PhotoID) {
        this.PhotoID = PhotoID;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String Url) {
        this.Url = Url;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.Name);
        dest.writeString(this.IDCard);
        dest.writeString(this.PhotoID);
        dest.writeString(this.Url);
    }

    public IdCardInfo() {
    }

    protected IdCardInfo(Parcel in) {
        this.Name = in.readString();
        this.IDCard = in.readString();
        this.PhotoID = in.readString();
        this.Url = in.readString();
    }

    public static final Parcelable.Creator<IdCardInfo> CREATOR = new Parcelable.Creator<IdCardInfo>() {
        @Override
        public IdCardInfo createFromParcel(Parcel source) {
            return new IdCardInfo(source);
        }

        @Override
        public IdCardInfo[] newArray(int size) {
            return new IdCardInfo[size];
        }
    };
}

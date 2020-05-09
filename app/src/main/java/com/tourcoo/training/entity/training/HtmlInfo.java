package com.tourcoo.training.entity.training;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2020年05月09日16:42
 * @Email: 971613168@qq.com
 */
public class HtmlInfo implements Parcelable {
    private String Url;

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.Url);
    }

    public HtmlInfo() {
    }

    protected HtmlInfo(Parcel in) {
        this.Url = in.readString();
    }

    public static final Parcelable.Creator<HtmlInfo> CREATOR = new Parcelable.Creator<HtmlInfo>() {
        @Override
        public HtmlInfo createFromParcel(Parcel source) {
            return new HtmlInfo(source);
        }

        @Override
        public HtmlInfo[] newArray(int size) {
            return new HtmlInfo[size];
        }
    };
}

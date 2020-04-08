package com.tourcoo.training.entity.account.register;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2020年04月08日11:48
 * @Email: 971613168@qq.com
 */
public class CompanyInfo implements Parcelable {


    /**
     * ID : 454
     * Name : 合肥吉通测试账号
     */

    private int ID;
    private String Name;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.ID);
        dest.writeString(this.Name);
    }

    public CompanyInfo() {
    }

    protected CompanyInfo(Parcel in) {
        this.ID = in.readInt();
        this.Name = in.readString();
    }

    public static final Parcelable.Creator<CompanyInfo> CREATOR = new Parcelable.Creator<CompanyInfo>() {
        @Override
        public CompanyInfo createFromParcel(Parcel source) {
            return new CompanyInfo(source);
        }

        @Override
        public CompanyInfo[] newArray(int size) {
            return new CompanyInfo[size];
        }
    };
}

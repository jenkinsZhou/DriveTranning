package com.tourcoo.training.entity.account.register;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2020年04月08日17:28
 * @Email: 971613168@qq.com
 */
public class Supervisors implements Parcelable {
    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    private int ID;
    private String Name;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.ID);
        dest.writeString(this.Name);
    }

    public Supervisors() {
    }

    protected Supervisors(Parcel in) {
        this.ID = in.readInt();
        this.Name = in.readString();
    }

    public static final Parcelable.Creator<Supervisors> CREATOR = new Parcelable.Creator<Supervisors>() {
        @Override
        public Supervisors createFromParcel(Parcel source) {
            return new Supervisors(source);
        }

        @Override
        public Supervisors[] newArray(int size) {
            return new Supervisors[size];
        }
    };
}

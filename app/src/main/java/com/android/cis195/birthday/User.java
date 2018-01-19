package com.android.cis195.birthday;

import android.os.Parcel;
import android.os.Parcelable;
import android.provider.BaseColumns;

/**
 * Created by Jason on 10/5/2017.
 */

public class User implements Parcelable {

    static class UserEntry implements BaseColumns {
        static final String TABLE_NAME = "user";
        static final String COLUMN_NAME = "name";
        static final String COLUMN_MONTH = "month";
        static final String COLUMN_DAY = "day";
        static final String COLUMN_URL = "url";
    }

    private String name;
    private String month;
    private int day;
    private String url;

    public User(String name, String month, int day) {
        this(name, month, day, "");
    }

    public User(String name, String month, int day, String url) {
        this.name = name;
        this.month = month;
        this.day = day;
        this.url = url;
    }

    public User(Parcel parcel) {
        this.name = parcel.readString();
        this.month = parcel.readString();
        this.day = parcel.readInt();
        if (parcel.dataSize() > 3) {
            this.url = parcel.readString();
        }
    }

    public String getName() {
        return name;
    }

    public String getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(month);
        parcel.writeInt(day);
        parcel.writeString(url);
    }

    public static final Parcelable.Creator<User> CREATOR
            = new Parcelable.Creator<User>() {
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };


}

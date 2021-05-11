package com.example.pointsproject.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class Redeem implements Parcelable {
    private String email;
    private int points;

    public Redeem() {
    }

    protected Redeem(Parcel in) {
        email = in.readString();
        points = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(email);
        dest.writeInt(points);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Redeem> CREATOR = new Creator<Redeem>() {
        @Override
        public Redeem createFromParcel(Parcel in) {
            return new Redeem(in);
        }

        @Override
        public Redeem[] newArray(int size) {
            return new Redeem[size];
        }
    };

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}

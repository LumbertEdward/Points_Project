package com.example.pointsproject.Model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class NotificationAll {
    @SerializedName("data")
    private ArrayList<NotificationModel> notificationModelArrayList = new ArrayList<>();
    @SerializedName("status")
    private String status;

    public ArrayList<NotificationModel> getNotificationModelArrayList() {
        return notificationModelArrayList;
    }

    public void setNotificationModelArrayList(ArrayList<NotificationModel> notificationModelArrayList) {
        this.notificationModelArrayList = notificationModelArrayList;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

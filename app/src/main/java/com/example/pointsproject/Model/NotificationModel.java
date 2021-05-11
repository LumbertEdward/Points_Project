package com.example.pointsproject.Model;

import com.google.gson.annotations.SerializedName;

public class NotificationModel {
    private String emailNot;
    private String notificationNot;
    private String dateNot;

    public String getEmailNot() {
        return emailNot;
    }

    public void setEmailNot(String emailNot) {
        this.emailNot = emailNot;
    }

    public String getNotificationNot() {
        return notificationNot;
    }

    public void setNotificationNot(String notificationNot) {
        this.notificationNot = notificationNot;
    }

    public String getDateNot() {
        return dateNot;
    }

    public void setDateNot(String dateNot) {
        this.dateNot = dateNot;
    }
}

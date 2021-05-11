package com.example.pointsproject.Model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class UsersAll {
    @SerializedName("data")
    private ArrayList<Users> usersArrayList;
    @SerializedName("status")
    private String status;

    public ArrayList<Users> getUsersArrayList() {
        return usersArrayList;
    }

    public void setUsersArrayList(ArrayList<Users> usersArrayList) {
        this.usersArrayList = usersArrayList;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

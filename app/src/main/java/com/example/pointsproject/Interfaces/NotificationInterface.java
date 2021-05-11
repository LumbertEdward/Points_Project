package com.example.pointsproject.Interfaces;

import com.example.pointsproject.Model.NotificationAll;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface NotificationInterface {
    @FormUrlEncoded
    @POST("read_notifications.php")
    Call<String> readNotifications(
            @Field("email") String email
    );
}

package com.example.pointsproject.Interfaces;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface NewNotificationInterface {
    @FormUrlEncoded
    @POST("notifications.php")
    Call<String> sendNotification(
            @Field("email") String email,
            @Field("notification") String notification,
            @Field("date") String date
    );
}

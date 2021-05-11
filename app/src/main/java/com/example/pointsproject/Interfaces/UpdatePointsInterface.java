package com.example.pointsproject.Interfaces;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface UpdatePointsInterface {
    @FormUrlEncoded
    @POST("updatepoints.php")
    Call<String> updatePoints(
            @Field("email") String email,
            @Field("points") int points,
            @Field("pointsRedeemed") int redeemedPoints
    );
}

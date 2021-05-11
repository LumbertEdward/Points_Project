package com.example.pointsproject.Interfaces;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ViewPointsInterface {
    @FormUrlEncoded
    @POST("points.php")
    Call<String> viewPoints(
            @Field("email") String email
    );
}

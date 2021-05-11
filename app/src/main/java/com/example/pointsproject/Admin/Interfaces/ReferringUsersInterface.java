package com.example.pointsproject.Admin.Interfaces;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ReferringUsersInterface {
    @FormUrlEncoded
    @POST("allusers.php")
    Call<String> getAllUsers(
            @Field("email") String email
    );
}

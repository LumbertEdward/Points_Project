package com.example.pointsproject.Admin.Interfaces;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface AdminLoginInterface {
    @FormUrlEncoded
    @POST("adminlogin.php")
    Call<String> sendData(
            @Field("email") String email,
            @Field("password") String password
    );
}

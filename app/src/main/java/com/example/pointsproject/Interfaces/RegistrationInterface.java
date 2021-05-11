package com.example.pointsproject.Interfaces;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface RegistrationInterface {
    @FormUrlEncoded
    @POST("register.php")
    Call<String> sendData(
            @Field("firstname") String firstName,
            @Field("lastname") String lastName,
            @Field("email") String email,
            @Field("password") String password,
            @Field("phone") String phone
    );
}

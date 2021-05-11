package com.example.pointsproject.Admin.Interfaces;

import com.example.pointsproject.Model.UsersAll;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface AllUsersInterface {
    @FormUrlEncoded
    @POST("allusers.php")
    Call<String> getAllUsers(
            @Field("email") String email
    );
}

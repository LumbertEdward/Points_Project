package com.example.pointsproject.Admin.Interfaces;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface DeletePayedUserInterface {
    @FormUrlEncoded
    @POST("deleteuser.php")
    Call<String> deleteUser(
            @Field("email") String email
    );
}

package com.example.pointsproject.Interfaces;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface UpdateUserInterface {
    @FormUrlEncoded
    @POST("updateuser.php")
    Call<String> updateUser(
            @Field("firstname") String firstName,
            @Field("lastname") String lastName,
            @Field("phone") String phone,
            @Field("email") String email
    );
}

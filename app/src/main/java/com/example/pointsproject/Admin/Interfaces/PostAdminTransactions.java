package com.example.pointsproject.Admin.Interfaces;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface PostAdminTransactions {
    @FormUrlEncoded
    @POST("senttransactions.php")
    Call<String> sendTransaction(
            @Field("email") String email,
            @Field("transaction") String transaction,
            @Field("date") String date
    );
}

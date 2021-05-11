package com.example.pointsproject.Interfaces;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface SearchInterface {
    @FormUrlEncoded
    @POST("transactions.php")
    Call<String> getTransactions(
            @Field("email") String email
    );
}

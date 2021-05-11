package com.example.pointsproject.Admin.Interfaces;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface AdminTransactionsInterface {
    @FormUrlEncoded
    @POST("transactions.php")
    Call<String> getTransactions(
            @Field("email") String email
    );
}

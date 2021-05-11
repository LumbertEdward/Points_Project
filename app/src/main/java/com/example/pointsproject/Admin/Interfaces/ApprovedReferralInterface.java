package com.example.pointsproject.Admin.Interfaces;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApprovedReferralInterface {
    @FormUrlEncoded
    @POST("referredusers.php")
    Call<String> sendReferredUser(
            @Field("referredEmail") String emailReferred,
            @Field("referrerEmail") String emailReferrer,
            @Field("notification") String notification,
            @Field("date") String date
    );
}

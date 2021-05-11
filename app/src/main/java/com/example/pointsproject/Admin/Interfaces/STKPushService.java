package com.example.pointsproject.Admin.Interfaces;

import com.example.pointsproject.Admin.ModelsMpesa.AccessTokens;
import com.example.pointsproject.Admin.ModelsMpesa.STKPush;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface STKPushService {
    @POST("mpesa/stkpush/v1/processrequest")
    Call<STKPush> sendPush(@Body STKPush stkPush);

    @GET("oauth/v1/generate?grant_type=client_credentials")
    Call<AccessTokens> getAccessTokens();
}

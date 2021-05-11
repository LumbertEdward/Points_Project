package com.example.pointsproject.Admin.Interceptors;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthInterceptor implements Interceptor {
    private String mAuthToken;

    public AuthInterceptor(String mAuthToken) {
        this.mAuthToken = mAuthToken;
    }

    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        Request request = chain.request().newBuilder()
                .addHeader("Authorization", "Bearer " + mAuthToken)
                .build();
        return chain.proceed(request);
    }
}

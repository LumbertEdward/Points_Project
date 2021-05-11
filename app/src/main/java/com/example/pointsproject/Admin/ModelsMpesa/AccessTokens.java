package com.example.pointsproject.Admin.ModelsMpesa;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AccessTokens {
    @SerializedName("access_token")
    @Expose
    public String accessToken;
    @SerializedName("expires_in")
    @Expose
    private String expiresIn;

    public AccessTokens(String accessToken, String expiresIn) {
        this.accessToken = accessToken;
        this.expiresIn = expiresIn;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(String expiresIn) {
        this.expiresIn = expiresIn;
    }
}

package com.example.pointsproject.Model;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesHelper {
    private static final String LOGGED_IN = "loggedIn";
    private static final String FIRST_NAME = "firstName";
    private static final  String EMAIL = "email";
    private Context context;
    private SharedPreferences sharedPreferences;

    public PreferencesHelper(Context context) {
        sharedPreferences = context.getSharedPreferences("shared", Context.MODE_PRIVATE);
        this.context = context;
    }

    public void setLoggedIn(boolean logged){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(LOGGED_IN, logged);
        editor.commit();
    }

    public boolean getLoggedIn(){
        return sharedPreferences.getBoolean(LOGGED_IN, false);
    }

    public void setFirstName(String name){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(FIRST_NAME, name);
        editor.commit();
    }

    public String getFirstName(){
        return sharedPreferences.getString(FIRST_NAME, "");
    }

    public void setEmail(String email){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(EMAIL, email);
        editor.commit();
    }

    public String getEmail(){
        return sharedPreferences.getString(EMAIL, "");
    }
}

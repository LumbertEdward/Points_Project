package com.example.pointsproject.Classes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.example.pointsproject.Account.Login;
import com.example.pointsproject.R;

public class SplashScreen extends AppCompatActivity {
    private TextView title;
    private TextView subTitle;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            getWindow().setNavigationBarColor(getResources().getColor(R.color.purple_700));
        }

        //init
        title = (TextView) findViewById(R.id.titleSplash);
        subTitle = (TextView) findViewById(R.id.subtitleSplash);

        //load animations
        Animation anim1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_left);
        Animation anim2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_right);

        //setAnimations
        title.setAnimation(anim1);
        subTitle.setAnimation(anim2);

        //handler
        handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashScreen.this, Login.class));
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                finish();
            }
        }, 3000);
    }
}
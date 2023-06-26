package com.example.libmanager_btl;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import com.airbnb.lottie.LottieAnimationView;

public class SplashScreenActivity extends AppCompatActivity {
    private ImageView logo, appname, splashimg;
    private LottieAnimationView lottie;
    private static final int SPLASH_TIMEOUT = 5000;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        //  getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FIRST_APPLICATION_WINDOW);
        logo = findViewById(R.id.logo);
        appname=findViewById(R.id.app_name);
        splashimg=findViewById(R.id.img);
        lottie=findViewById(R.id.lottie);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_TIMEOUT);

        splashimg.animate().translationY(-3000).setDuration(4000).setStartDelay(1000);
        logo.animate().translationY(3000).setDuration(4000).setStartDelay(1000);
        appname.animate().translationY(3000).setDuration(4000).setStartDelay(1000);
        lottie.animate().translationY(3000).setDuration(4000).setStartDelay(1000);

    }
}
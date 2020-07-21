package com.example.cityguideapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cityguideapp.User.OnBoarding;
import com.example.cityguideapp.User.UserDashboard;

public class Splash_screen extends AppCompatActivity {
    private static int SPLASH_TIMER=5000;
    ImageView backgroundImage;
    TextView PoweredByLine;
    //Animation
    Animation sideAnim, bottomAnim;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        backgroundImage = findViewById(R.id.background_image);
        PoweredByLine = findViewById(R.id.powered_by_line);
        //Animations
        sideAnim= AnimationUtils.loadAnimation(this,R.anim.side_anim);
        bottomAnim= AnimationUtils.loadAnimation(this,R.anim.bottom_anim);
        //set Animations on elements
        backgroundImage.setAnimation(sideAnim);
        PoweredByLine.setAnimation(bottomAnim);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                sharedPreferences=getSharedPreferences("onBoardingScreen",MODE_PRIVATE);
                boolean isFirstTime=sharedPreferences.getBoolean("firstTime",true);
                if(isFirstTime){
                    SharedPreferences.Editor editor=sharedPreferences.edit();
                    editor.putBoolean("firstTime",false);
                    editor.commit();
                    Intent intent=new Intent(getApplicationContext(),OnBoarding.class);
                    startActivity(intent);
                    finish();
                }else {
                    Intent intent=new Intent(getApplicationContext(),OnBoarding.class);
                    startActivity(intent);
                    finish();
                }
                Intent intent=new Intent(Splash_screen.this, UserDashboard.class);
                startActivity(intent);
                finish();


            }
        },SPLASH_TIMER);



    }
}

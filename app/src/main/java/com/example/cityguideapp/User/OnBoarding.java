package com.example.cityguideapp.User;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.cityguideapp.Helper.slider_adapter;
import com.example.cityguideapp.R;

public class OnBoarding extends AppCompatActivity {
        ViewPager viewPager;
        LinearLayout dotslayout;
        TextView[] dots;
        Button letsGetStarted;
        Animation animation;
        int currentPos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_boarding);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //hooks
        viewPager=findViewById(R.id.slider);
        dotslayout=findViewById(R.id.dots);
        letsGetStarted=findViewById(R.id.get_button_started);
        //call adapter
        slider_adapter sliderAdapter=new slider_adapter(this);
        viewPager.setAdapter(sliderAdapter);
        addDots(0);
        viewPager.addOnPageChangeListener(changeListener);
    }
    public  void skip(View view){
             startActivity(new Intent(this,UserDashboard.class));
    }
    public void next(View view){
            viewPager.setCurrentItem(currentPos+1);
    }
    private  void addDots(int position) {
        dots = new TextView[4];
        dotslayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dotslayout.addView(dots[i]);
        }
        if (dots.length > 0) {
            dots[position].setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        }
    }
        ViewPager.OnPageChangeListener changeListener=new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                addDots(position);
                currentPos=position;
                if(position==0){
                    letsGetStarted.setVisibility(View.INVISIBLE);
                }else if(position==1){
                    letsGetStarted.setVisibility(View.INVISIBLE);
                }else if(position==2){
                    letsGetStarted.setVisibility(View.INVISIBLE);
                }else{
                    animation= AnimationUtils.loadAnimation(OnBoarding.this,R.anim.bottom_anim);
                    letsGetStarted.setAnimation(animation);
                    letsGetStarted.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        };
}

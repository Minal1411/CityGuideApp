package com.example.cityguideapp.LoginSignup;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cityguideapp.R;

import java.util.Calendar;

public class SignUp2ndClass extends AppCompatActivity {
    ImageView backbtn;
    Button next, login;
    TextView titleText, slideText;
    RadioGroup radioGroup;
    RadioButton selectedGender;
    DatePicker datePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up2nd_class);
        //Hooks
        backbtn = findViewById(R.id.signup_back_button);
        next = findViewById(R.id.signup_next_button);
        login = findViewById(R.id.signup_login_button);
        titleText = findViewById(R.id.signup_title_text);
        slideText = findViewById(R.id.signup_slide_text);
        radioGroup = findViewById(R.id.radio_group);
        datePicker = findViewById(R.id.age_picker);
    }

    public void call3rdSignupScreen(View view) {
        if (!validateGender() | !validateAge()) {
            return;
        }
      String  _fullName1 = getIntent().getStringExtra("fullName");
      String   _email1 = getIntent().getStringExtra("email");
      String  _username1 = getIntent().getStringExtra("username");
      String  _password1 = getIntent().getStringExtra("password");
        selectedGender = findViewById(radioGroup.getCheckedRadioButtonId());
        String _gender1 = selectedGender.getText().toString();
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year = datePicker.getYear();
        String _date1 = day + "/" + month + "/" + year;
        Intent intent = new Intent(getApplicationContext(), SignUp3rdClass.class);
        intent.putExtra("fullName",_fullName1);
        intent.putExtra("email",_email1);
        intent.putExtra("username",_username1);
        intent.putExtra("password",_password1);
        intent.putExtra("date",_date1);
        intent.putExtra("gender",_gender1);
        Pair[] pairs = new Pair[5];
        pairs[0] = new Pair<View, String>(backbtn, "transition_back_arrow_btn");
        pairs[1] = new Pair<View, String>(next, "transition_next_btn");
        pairs[2] = new Pair<View, String>(login, "transition_login_btn");
        pairs[3] = new Pair<View, String>(titleText, "transition_title_text");
        pairs[4] = new Pair<View, String>(slideText, "transition_slide_text");
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SignUp2ndClass.this, pairs);
            startActivity(intent, options.toBundle());
        } else
            startActivity(intent);

    }

    private boolean validateGender() {
        if (radioGroup.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, "Please Select Gender", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    private boolean validateAge() {
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        int userAge = datePicker.getYear();
        int isAgeValid = currentYear - userAge;

        if (isAgeValid < 14) {
            Toast.makeText(this, "You are not eligible to apply", Toast.LENGTH_SHORT).show();
            return false;
        } else
            return true;
    }

}

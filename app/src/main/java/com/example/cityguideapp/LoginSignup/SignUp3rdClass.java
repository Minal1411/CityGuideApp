package com.example.cityguideapp.LoginSignup;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.ScrollView;

import com.example.cityguideapp.R;
import com.example.cityguideapp.VerifyOTP;
import com.google.android.material.textfield.TextInputLayout;
import com.hbb20.CountryCodePicker;

public class SignUp3rdClass extends AppCompatActivity {
    ScrollView scrollView;
    TextInputLayout phoneNumber;
    CountryCodePicker countryCodePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up3rd_class);

        //Hooks
        scrollView = findViewById(R.id.signup_3rd_screen_scroll_view);
        countryCodePicker = findViewById(R.id.country_code_picker);
        phoneNumber = findViewById(R.id.signup_phone_number);
    }

    public void callVerifyOTPScreen(View view) {
        //Validate fields
        if (!validatePhoneNumber()) {
            return;
        }
        String _fullName2 = getIntent().getStringExtra("fullName");
        String _email2 = getIntent().getStringExtra("email");
        String _username2 = getIntent().getStringExtra("username");
        String _password2 = getIntent().getStringExtra("password");
        String _date2 = getIntent().getStringExtra("date");
        String _gender2 = getIntent().getStringExtra("gender");
        //Get complete phone number
        String _getUserEnteredPhoneNumber = phoneNumber.getEditText().getText().toString().trim();
//Remove first zero if entered!
        if (_getUserEnteredPhoneNumber.charAt(0) == '0') {
            _getUserEnteredPhoneNumber = _getUserEnteredPhoneNumber.substring(1);
        }
//Complete phone number
        final String _phoneNo = "+" + countryCodePicker.getFullNumber() + _getUserEnteredPhoneNumber;
        Intent intent = new Intent(getApplicationContext(), VerifyOTP.class);
        intent.putExtra("fullName", _fullName2);
        intent.putExtra("email", _email2);
        intent.putExtra("username", _username2);
        intent.putExtra("password", _password2);
        intent.putExtra("date", _date2);
        intent.putExtra("gender", _gender2);
        intent.putExtra("phoneNo", _phoneNo);
        Pair[] pairs=new Pair[1];
        pairs[0]=new Pair<View,String>(scrollView,"transition_OTP_screen");
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptions options=ActivityOptions.makeSceneTransitionAnimation(SignUp3rdClass.this,pairs);
            startActivity(intent,options.toBundle());
        }
        else {
            startActivity(intent);
        }

    }

    private boolean validatePhoneNumber() {
        String val = phoneNumber.getEditText().getText().toString().trim();
        String checkspaces = "\\A\\w{1,20}\\z";
        if (val.isEmpty()) {
            phoneNumber.setError("Enter valid phone number");
            return false;
        } else if (!val.matches(checkspaces)) {
            phoneNumber.setError("No White spaces are allowed!");
            return false;
        } else {
            phoneNumber.setError(null);
            phoneNumber.setErrorEnabled(false);
            return true;
        }
    }
}

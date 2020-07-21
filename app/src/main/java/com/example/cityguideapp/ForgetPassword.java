package com.example.cityguideapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cityguideapp.LoginSignup.Login;
import com.example.cityguideapp.LoginSignup.RetailerStartUpScreen;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;

public class ForgetPassword extends AppCompatActivity {
    //variables
    private ImageView screenIcon;
    private TextView title, description;
    TextInputLayout phoneNumbertextField, password;
    private CountryCodePicker countryCodePicker;
    private Button nextBtn;
    private Animation animation;
    RelativeLayout progressbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        //Hooks
        screenIcon = findViewById(R.id.forget_password_icon);
        title = findViewById(R.id.forget_password_title);
        description = findViewById(R.id.forget_password_description);
        phoneNumbertextField = findViewById(R.id.forget_password_phone_number);
        countryCodePicker = findViewById(R.id.forget_country_code_picker);
        nextBtn = findViewById(R.id.forget_password_next_btn);
        progressbar = findViewById(R.id.progress_bar);

        //Animation Hook
        animation = AnimationUtils.loadAnimation(this, R.anim.side_anim);
        screenIcon.setAnimation(animation);
        title.setAnimation(animation);
        description.setAnimation(animation);
        phoneNumbertextField.setAnimation(animation);
//        countryCodePicker.setAnimation(animation);
        nextBtn.setAnimation(animation);

    }
     /*
    call the OTP screen
    and pass phone number
    for verification
     */
    //validate phone number

    public void callVerifyOTPScreen(View view) {
        if (!isConnected()) {
            showCustomDialog();
            return ;
        }
        //validate phone number
        if (!validateFields()) {
            return;
        }
        progressbar.setVisibility(View.VISIBLE);
        //get data
        String _phoneNumber = phoneNumbertextField.getEditText().getText().toString().trim();
        if (_phoneNumber.charAt(0) == '0') {
            _phoneNumber = _phoneNumber.substring(1);
        }

        final String _completePhoneNumber = "+" + countryCodePicker.getFullNumber() + _phoneNumber;
        //check whether user exist or not in database
        Query checkUser = FirebaseDatabase.getInstance().getReference("Users").orderByChild("phoneNo").equalTo(_completePhoneNumber);
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    phoneNumbertextField.setError(null);
                    phoneNumbertextField.setErrorEnabled(false);
                    Intent intent = new Intent(getApplicationContext(), VerifyOTP.class);
                    intent.putExtra("phoneNo", _completePhoneNumber);
                    intent.putExtra("whatToDo", "updateData");
                    startActivity(intent);
                    finish();
                    progressbar.setVisibility(View.GONE);
                } else {
                    progressbar.setVisibility(View.GONE);
                    phoneNumbertextField.setError("No such user exist!");
                    phoneNumbertextField.requestFocus();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressbar.setVisibility(View.GONE);
                Toast.makeText(ForgetPassword.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }


    private boolean validateFields() {
        String val = phoneNumbertextField.getEditText().getText().toString().trim();
        String checkspaces = "\\A\\w{1,20}\\z";
        if (val.isEmpty()) {
            phoneNumbertextField.setError("Enter valid phone number");
            return false;
        } else if (!val.matches(checkspaces)) {
            phoneNumbertextField.setError("No White spaces are allowed!");
            return false;
        } else {
            phoneNumbertextField.setError(null);
            phoneNumbertextField.setErrorEnabled(false);
            return true;
        }
    }

    private boolean isConnected() {
        ConnectivityManager manager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = manager.getActiveNetworkInfo();
        if (null != activeNetwork) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                //Toast.makeText(this, "wifi Enabled", Toast.LENGTH_SHORT).show();
            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                //Toast.makeText(this, "Data network enabled", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(this, "No internet Connection try again", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    private void showCustomDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Please connect to the internet to proceed further")
                .setCancelable(false)
                .setPositiveButton("Connect", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivity(new Intent(getApplicationContext(), RetailerStartUpScreen.class));
                        finish();
                    }
                });
    }


}

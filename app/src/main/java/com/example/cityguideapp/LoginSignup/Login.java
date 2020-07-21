package com.example.cityguideapp.LoginSignup;
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
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.cityguideapp.Databases.SessionManager;
import com.example.cityguideapp.ForgetPassword;
import com.example.cityguideapp.R;
import com.example.cityguideapp.RetailerDashboard;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;

import java.util.HashMap;

public class Login extends AppCompatActivity {
    CountryCodePicker countryCodePicker;
    TextInputLayout phoneNumber, password;
    RelativeLayout progressbar;
    CheckBox rememberMe;
    TextInputEditText

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retailer_login);
        countryCodePicker = findViewById(R.id.login_country_code_picker);
        phoneNumber = findViewById(R.id.login_phone_number);
        password = findViewById(R.id.login_password);
        progressbar = findViewById(R.id.login_progress_bar);
        rememberMe=findViewById(R.id.remember_me);
        //Check weather phone number and password is already saved in shared preferences or not
        SessionManager sessionManager=new SessionManager(Login.this,SessionManager.SESSION_REMEMBERME);
        if (sessionManager.checkRememberMe()){
            HashMap<String,String> rememberMeDetails=sessionManager.getRememberMeDetailsFromSession();
        }

    }


    public void letTheUserLoggedIn(View view) {
        //check internet connection
        if (!isConnected()){
            showCustomDialog();

        }
        //validate username and password
        if (!validatePassword() | !validatePhoneNumber()) {
            return;
        }
        progressbar.setVisibility(View.VISIBLE);
        //get data
        String _phoneNumber = phoneNumber.getEditText().getText().toString().trim();
        final String _password = password.getEditText().getText().toString().trim();
        if (_phoneNumber.charAt(0) == '0') {
            _phoneNumber = _phoneNumber.substring(1);
        }

        final String _completePhoneNumber = "+" + countryCodePicker.getFullNumber() + _phoneNumber;
            if(rememberMe.isChecked()){
                SessionManager sessionManager=new SessionManager(Login.this,SessionManager.SESSION_REMEMBERME);
                sessionManager.createrRememberMeSession(_phoneNumber,_password);
            }
        //Database
        Query checkUser = FirebaseDatabase.getInstance().getReference("Users").orderByChild("phoneNo").equalTo(_completePhoneNumber);
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    phoneNumber.setError(null);
                    phoneNumber.setErrorEnabled(false);
                    String systemPassword = snapshot.child(_completePhoneNumber).child("password").getValue(String.class);
                    if (systemPassword.equals(_password)) {
                        password.setError(null);
                        password.setErrorEnabled(false);
                        String _fullname = snapshot.child(_completePhoneNumber).child("fullname").getValue(String.class);
                        String _username = snapshot.child(_completePhoneNumber).child("username").getValue(String.class);
                        String _email = snapshot.child(_completePhoneNumber).child("email").getValue(String.class);
                        String _phoneNo = snapshot.child(_completePhoneNumber).child("phoneNo").getValue(String.class);
                        String _password = snapshot.child(_completePhoneNumber).child("password").getValue(String.class);
                        String _dateOfBirth = snapshot.child(_completePhoneNumber).child("date").getValue(String.class);
                        String _gender = snapshot.child(_completePhoneNumber).child("gender").getValue(String.class);

                        //Create a Session
                        SessionManager sessionManager=new SessionManager(Login.this,SessionManager.SESSION_USERSESSION);
                        sessionManager.createLoginSession(_fullname,_username,_email,_phoneNo,_password,_dateOfBirth,_gender);
                        startActivity(new Intent(getApplicationContext(), RetailerDashboard.class));
                        Toast.makeText(Login.this, _fullname + "\n" + "\n" + _email + "\n" + _phoneNo + "\n" + _dateOfBirth, Toast.LENGTH_SHORT).show();
                    } else {
                        progressbar.setVisibility(View.GONE);
                        Toast.makeText(Login.this, "Password doesnot exist", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    progressbar.setVisibility(View.GONE);
                    Toast.makeText(Login.this, "data doesnot exist", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressbar.setVisibility(View.GONE);
                Toast.makeText(Login.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void showCustomDialog() {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
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
                        startActivity(new Intent(getApplicationContext(),RetailerStartUpScreen.class));
                        finish();
                    }
                });
    }

    /*
    check internet connection
     */

    private boolean isConnected() {
        ConnectivityManager manager=(ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork=manager.getActiveNetworkInfo();
        if (null!=activeNetwork){
            if (activeNetwork.getType()==ConnectivityManager.TYPE_WIFI){
                Toast.makeText(this, "wifi Enabled", Toast.LENGTH_SHORT).show();
            }
            else if (activeNetwork.getType()==ConnectivityManager.TYPE_MOBILE){
                Toast.makeText(this, "Data network enabled", Toast.LENGTH_SHORT).show();
            }

        }
        else{
            Toast.makeText(this, "No internet Connection try again", Toast.LENGTH_SHORT).show();
        }
        return false;
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

    private boolean validatePassword() {
        String val = password.getEditText().getText().toString().trim();
        String checkPassword = "^" +
                //"(?=.*[0-9])" +         //at least 1 digit
                //"(?=.*[a-z])" +         //at least 1 lower case letter
                //"(?=.*[A-Z])" +         //at least 1 upper case letter
                "(?=.*[a-zA-Z])" +      //any letter
                "(?=.*[@#$%^&+=])" +    //at least 1 special character
                "(?=\\S+$)" +           //no white spaces
                ".{4,}" +               //at least 4 characters
                "$";

        if (val.isEmpty()) {
            password.setError("Field can not be empty");
            return false;
        } else if (!val.matches(checkPassword)) {
            password.setError("Password should contain 4 characters!");
            return false;
        } else {
            password.setError(null);
            password.setErrorEnabled(false);
            return true;
        }
    }


    public void callForgetPassword(View view) {
        startActivity(new Intent(getApplicationContext(), ForgetPassword.class));

    }
}

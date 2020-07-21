package com.example.cityguideapp;

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
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.cityguideapp.LoginSignup.RetailerStartUpScreen;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class SetNewPassword extends AppCompatActivity {
    RelativeLayout progressbar;
    TextInputLayout newpassword,confirmpassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_new_password);
    }

    public void setNewPasswordBtn(View view) {
        if (!isConnected()){
            showCustomDialog();
            return;
        }
//validate phone number
        if (!validatePassword() | !validateConfirmPassword()){
            return;
        }
        progressbar.setVisibility(View.GONE);
        //Get data from fields
        String _newpassword=newpassword.getEditText().getText().toString().trim();
        String _phoneNumber=getIntent().getStringExtra("phoneNo");
        //Update Data in Firebase and in sessions
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Users");
        reference.child(_phoneNumber).child("password").setValue(_newpassword);
        startActivity(new Intent(getApplicationContext(),ForgetPasswordSuccessMessage.class));
        finish();
        
    }

    private boolean validateConfirmPassword() {
            String val = newpassword.getEditText().getText().toString().trim();
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
                newpassword.setError("Field can not be empty");
                return false;
            } else if (!val.matches(checkPassword)) {
                newpassword.setError("Password should contain 4 characters!");
                return false;
            } else {
                newpassword.setError(null);
                newpassword.setErrorEnabled(false);
                return true;
            }
        }


    private boolean validatePassword() {
            String val = confirmpassword.getEditText().getText().toString().trim();
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
                confirmpassword.setError("Field can not be empty");
                return false;
            }
            else if(confirmpassword!=newpassword){
                confirmpassword.setError("password does not match");
                return  false;

            }


            else if (!val.matches(checkPassword)) {
                confirmpassword.setError("Password should contain 4 characters!");
                return false;
            } else {
                confirmpassword.setError(null);
                confirmpassword.setErrorEnabled(false);
                return true;
            }
        }


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
                        startActivity(new Intent(getApplicationContext(), RetailerStartUpScreen.class));
                        finish();
                    }
                });
    }
}

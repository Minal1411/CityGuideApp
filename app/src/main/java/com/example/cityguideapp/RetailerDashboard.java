package com.example.cityguideapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.cityguideapp.Databases.SessionManager;

import java.util.HashMap;

public class RetailerDashboard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retailer_dashboard);
        TextView textView=findViewById(R.id.textview);
        SessionManager sessionManager=new SessionManager(this);
        HashMap<String,String> usersDetails =sessionManager.getUsersDetailFromSession();
        String fullname=usersDetails.get(sessionManager.KEY_FULLNAME);
        String phoneNumber=usersDetails.get(sessionManager.KEY_PHONENUMBER);
        textView.setText(fullname+"\n"+ phoneNumber);
    }
}

package com.example.cityguideapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.example.cityguideapp.Databases.UserHelperClass;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

public class VerifyOTP extends AppCompatActivity {
    PinView pinFromUser;
    String codeBySystem;
    String _fullName3,_email3,_username3,_password3,_date3,_gender3,_phoneNo,_whatToDo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp);
        pinFromUser=findViewById(R.id.pin_view);

        //Get all the data from Intent
      _fullName3 = getIntent().getStringExtra("fullName");
         _email3 = getIntent().getStringExtra("email");
         _username3 = getIntent().getStringExtra("username");
         _password3 = getIntent().getStringExtra("password");
         _date3 = getIntent().getStringExtra("date");
         _gender3 = getIntent().getStringExtra("gender");
         _phoneNo=getIntent().getStringExtra("phoneNo");
         _whatToDo=getIntent().getStringExtra("whatToDo");
        sendVerificationCodeToUser(_phoneNo);
    }

    private void sendVerificationCodeToUser(String phoneNo) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNo,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                TaskExecutors.MAIN_THREAD,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks

    }
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks=
            new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                @Override
                public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                    super.onCodeSent(s, forceResendingToken);
                    codeBySystem=s;
                }

                @Override
                public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                    String code=phoneAuthCredential.getSmsCode();
                    if(code!=null){
                        pinFromUser.setText(code);
                        verifyCode(code);
                    }

                }

                @Override
                public void onVerificationFailed(FirebaseException e) {
                    Toast.makeText(VerifyOTP.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            };

    private void verifyCode(String code) {
        PhoneAuthCredential credential=PhoneAuthProvider.getCredential(codeBySystem,code);
        signInWithPhoneAuthCredential(credential);

    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //Verification completed successfully here Either
                            //store the data or do whatever desire

                           if (_whatToDo.equals("updateData")){
                              updateOldUsersData();
                           }else {
                               storeNewUsersData();
                           }



                        } else {

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(VerifyOTP.this, "Verification Not completed! Try again", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    private void updateOldUsersData() {
        Intent intent=new Intent(getApplicationContext(),SetNewPassword.class);
        intent.putExtra("phoneNo",_phoneNo);
        startActivity(intent);
        finish();
    }

    private void storeNewUsersData() {
        FirebaseDatabase rootnode=FirebaseDatabase.getInstance();
        DatabaseReference reference=rootnode.getReference("Users");
        UserHelperClass addNewUser=new UserHelperClass(_fullName3,_username3,_email3,_phoneNo,_password3,_date3,_gender3);
        reference.child(_phoneNo).setValue(addNewUser);
    }


    public void callNextScreenFromOTP(View view) {
           String code=pinFromUser.getText().toString();
           if(!code.isEmpty()){
               verifyCode(code);
           }
    }
}

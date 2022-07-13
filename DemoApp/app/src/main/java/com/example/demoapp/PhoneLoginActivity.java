package com.example.demoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.demoapp.databinding.ActivityChatDetailBinding;
import com.example.demoapp.databinding.ActivityPhoneLoginBinding;
import com.example.demoapp.Models.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

public class PhoneLoginActivity extends AppCompatActivity {

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;

    private ProgressDialog loadingBar;
    private FirebaseAuth mAuth;


    FirebaseDatabase database;
    ActivityPhoneLoginBinding binding;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPhoneLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
       // setContentView(R.layout.activity_phone_login);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();



        loadingBar = new ProgressDialog(this);

        binding.sendVerCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phoneNumber = binding.phoneNumberInput.getEditText().getText().toString();
                if(TextUtils.isEmpty(phoneNumber))
                {
                    Toast.makeText(PhoneLoginActivity.this, "Phone number is required ...", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    loadingBar.setTitle("Phone Verification");
                    loadingBar.setMessage("Please wait, while  we are authenticating your phone");
                    loadingBar.setCanceledOnTouchOutside(false);
                    loadingBar.show();
                    PhoneAuthOptions options =
                            PhoneAuthOptions.newBuilder(mAuth)
                                    .setPhoneNumber(phoneNumber)       // Phone number to verify
                                    .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                                    .setActivity(PhoneLoginActivity.this)                 // Activity (for callback binding)
                                    .setCallbacks(callbacks)          // OnVerificationStateChangedCallbacks
                                    .build();
                    PhoneAuthProvider.verifyPhoneNumber(options);
                }
            }
        });

        callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                binding.sendVerCodeButton.setVisibility(View.VISIBLE);
                binding.phoneNumberInput.setVisibility(View.VISIBLE);

                binding.verifyButton.setVisibility(View.INVISIBLE);
                binding.verificationCodeInput.setVisibility(View.INVISIBLE);

                Toast.makeText(PhoneLoginActivity.this, "Invalid Phone Number", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(String VerificationId, PhoneAuthProvider.ForceResendingToken token) {

                mVerificationId = VerificationId;
                mResendToken = token;

                loadingBar.dismiss();

                binding.sendVerCodeButton.setVisibility(View.INVISIBLE);
                binding.phoneNumberInput.setVisibility(View.VISIBLE);

                binding.verifyButton.setVisibility(View.VISIBLE);
                binding.verificationCodeInput.setVisibility(View.VISIBLE);

                Toast.makeText(PhoneLoginActivity.this, "Code has been sent ...", Toast.LENGTH_SHORT).show();
            }
        };

        binding.verifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.sendVerCodeButton.setVisibility(View.INVISIBLE);
                binding.phoneNumberInput.setVisibility(View.INVISIBLE);
                String verificationCode =  binding.verificationCodeInput.getEditText().getText().toString();
                if(TextUtils.isEmpty(verificationCode))
                {
                    Toast.makeText(PhoneLoginActivity.this, "Please write verification code first...", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    loadingBar.setTitle("Verification Code");
                    loadingBar.setMessage("Please wait, while we are authenticating your code");
                    loadingBar.setCanceledOnTouchOutside(false);
                    loadingBar.show();

                    PhoneAuthCredential credential =  PhoneAuthProvider.getCredential(mVerificationId, verificationCode );
                    signInWithPhoneAuthCredential(credential);
                }
            }
        });
    }


    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            loadingBar.dismiss();
                            Users user = new Users( binding.phoneNumberInput.getEditText().getText().toString(),binding.phoneNumberInput.getEditText().getText().toString(),binding.passwordInput.getEditText().getText().toString());
                            String id = task.getResult().getUser().getUid();
                            database.getReference().child("Users").child(id).setValue(user);
                            Toast.makeText(PhoneLoginActivity.this, "Sign Up Successful", Toast.LENGTH_SHORT).show();

//                            mAuth.signOut();
                            Intent intent = new Intent(PhoneLoginActivity.this, SignUpActivity.class);
                            startActivity(intent);
                        } else {
                            String message = task.getException().toString();
                            Toast.makeText(PhoneLoginActivity.this, "Error: " + message, Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}


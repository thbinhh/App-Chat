package com.example.demoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.demoapp.Models.Users;
import com.example.demoapp.databinding.ActivitySignUpBinding;
import com.example.demoapp.databinding.ActivitySignInBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Locale;

public class SignUpActivity extends AppCompatActivity {
    ActivitySignUpBinding binding;
    private FirebaseAuth mAuth;
    FirebaseDatabase database;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

//        getSupportActionBar().hide();
        binding.totalSignup.setAlpha(0f);
        binding.totalSignup.animate().alpha(1f).setDuration(1500);

        progressDialog = new ProgressDialog(SignUpActivity.this);
        progressDialog.setTitle("Creating Account");
        progressDialog.setMessage("We are creating your account.");

        binding.btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!binding.txtUsername.getEditText().getText().toString().isEmpty() && !binding.txtEmail.getEditText().getText().toString().isEmpty() && !binding.txtPassword.getEditText().getText().toString().isEmpty()   )
                {
                    String str1 = binding.txtPassword.getEditText().getText().toString();
                    String str2 = binding.txtPasswordAgain.getEditText().getText().toString();
                    if(str1.equals(str2) == true)
                    {
                        progressDialog.show();
                        mAuth.createUserWithEmailAndPassword(binding.txtEmail.getEditText().getText().toString(),binding.txtPassword.getEditText().getText().toString())
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        progressDialog.dismiss();
                                        if(task.isSuccessful())
                                        {
                                            Users user = new Users(binding.txtUsername.getEditText().getText().toString(),binding.txtEmail.getEditText().getText().toString(),binding.txtPassword.getEditText().getText().toString());
                                            String id = task.getResult().getUser().getUid();
                                            database.getReference().child("Users").child(id).setValue(user);
                                            Toast.makeText(SignUpActivity.this, "Sign Up Successful", Toast.LENGTH_SHORT).show();
                                        }
                                        else
                                        {
                                            Toast.makeText(SignUpActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                    else
                    {
                        Toast.makeText(SignUpActivity.this, "Please Check Password Again", Toast.LENGTH_SHORT).show();
                    }

                }
                else
                {
                    Toast.makeText(SignUpActivity.this, "Enter Credentials", Toast.LENGTH_SHORT).show();

                }
            }
        });

        binding.txtAlreadyHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                startActivity(intent);
            }
        });
    }
}
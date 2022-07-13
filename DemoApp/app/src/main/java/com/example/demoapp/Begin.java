package com.example.demoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.example.demoapp.databinding.ActivityBeginBinding;
import com.example.demoapp.databinding.ActivityChatDetailBinding;

public class Begin extends AppCompatActivity {
    ActivityBeginBinding binding;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityBeginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.bounce);
        getSupportActionBar().setTitle("");
        binding.beginApp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                binding.beginApp.startAnimation(animation);
//                progressDialog = new ProgressDialog(Begin.this);
//                progressDialog.setTitle("Login");
//                progressDialog.setMessage("Please Wait\n, Validation in Progress.");
//                progressDialog.show();

                Intent intent = new Intent(Begin.this, SignInActivity.class) ;
                startActivity(intent);



            }
        });
    }

    @Override
    protected void onPause() {
//        progressDialog.dismiss();
        super.onPause();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();


    }
}

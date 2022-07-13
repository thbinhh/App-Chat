package com.example.demoapp;

import static org.webrtc.VideoFrameDrawer.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.demoapp.databinding.ActivityForgetPassWordBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ForgetPassWord extends AppCompatActivity {
    ActivityForgetPassWordBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityForgetPassWordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        FirebaseAuth auth = FirebaseAuth.getInstance();

binding.btnSentPass.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        auth.sendPasswordResetEmail(binding.txtEmailRs.getEditText().getText().toString())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(ForgetPassWord.this, "Email Sent", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
});



    }
}
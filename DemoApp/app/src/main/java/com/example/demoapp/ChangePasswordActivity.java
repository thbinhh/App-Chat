package com.example.demoapp;

import static org.webrtc.VideoFrameDrawer.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.demoapp.databinding.ActivityChangePassWordBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class ChangePasswordActivity extends AppCompatActivity {
    ActivityChangePassWordBinding binding;
    FirebaseDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChangePassWordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        database = FirebaseDatabase.getInstance();


//binding.btnSentPass.setOnClickListener(new View.OnClickListener() {
//    @Override
//    public void onClick(View view) {
//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        String newPassword = binding.txtPasswordNew.getEditText().getText().toString();
//
//        user.updatePassword(newPassword)
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        if (task.isSuccessful()) {
//                            Toast.makeText(ChangePasswordActivity.this, "Done", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//
//    }
//});




            binding.btnSentPass.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {

                    FirebaseDatabase.getInstance().getReference().child("Users")
                            .child(FirebaseAuth.getInstance().getUid())
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    try {
                                        if (snapshot.child("password").getValue().toString().equals(binding.txtPasswordOld.getEditText().getText().toString())==true){
                                            String newPassword = binding.txtPasswordNew.getEditText().getText().toString();
                                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                            user.updatePassword(newPassword)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                HashMap<String, Object> obj = new HashMap<>();
                                                                obj.put("password", newPassword);
                                                                database.getReference().child("Users").child(FirebaseAuth.getInstance().getUid())
                                                                        .updateChildren(obj);

                                                            }
                                                        }
                                                    });
                                        } else {
                                           Toast.makeText(ChangePasswordActivity.this, "Password Wrong", Toast.LENGTH_SHORT).show();
                                        }
                                   }catch(Exception e){      Toast.makeText(ChangePasswordActivity.this, "Password Wrong", Toast.LENGTH_SHORT).show(); }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Toast.makeText(ChangePasswordActivity.this, "Password Wrong", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            });





    }
}
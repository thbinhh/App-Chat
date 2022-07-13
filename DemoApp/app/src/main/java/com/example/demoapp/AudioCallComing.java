package com.example.demoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.example.demoapp.Models.MessageModel;
import com.example.demoapp.databinding.ActivityAudioCallComingBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;

import java.net.URL;
import java.util.Date;
import java.util.HashMap;


public class AudioCallComing extends AppCompatActivity {

    ActivityAudioCallComingBinding binding;
    FirebaseDatabase database;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().hide();
        binding = ActivityAudioCallComingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        final String senderId = auth.getUid();

        String recieveId = getIntent().getStringExtra("userID");
        String userName = getIntent().getStringExtra("userName");
        String profilePic = getIntent().getStringExtra("profilePic");

        binding.callusername.setText(userName);
        Picasso.get().load(profilePic).placeholder(R.drawable.avatar).into(binding.profileimagecall);

        final String senderRoom = senderId + recieveId;
        final String receiverRoom = recieveId + senderId;

        Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                FirebaseDatabase.getInstance().getReference().child("checkCall").child("Audio")
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                try {
                                    if(!snapshot.child("inComeRoom").child(recieveId).child(senderId).getValue().toString().equals("true") )
                                    {
                                        Intent intent = new Intent(AudioCallComing.this,MainActivity.class);
                                        startActivity(intent);
                                        mHandler.removeCallbacksAndMessages(null);
                                    }
                                }catch(Exception e){ }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }
                        });


                mHandler.postDelayed(this, 1000);
            }
        }, 1000);

        binding.btnAppect.setOnClickListener(new View.OnClickListener() {  /// đồng ý trả lời
            @Override
            public void onClick(View view) {
                String recieveId = getIntent().getStringExtra("userID");
                final String senderId = auth.getUid();
                database = FirebaseDatabase.getInstance();

                HashMap<String, Object> obj = new HashMap<>();
                obj.put(senderId , "true");
                database.getReference().child("checkCall").child("Audio").child("outComeRoom").child(recieveId)
                        .updateChildren(obj);
                try {
                    JitsiMeetConferenceOptions options = new JitsiMeetConferenceOptions.Builder()
                            .setServerURL(new URL("https://meet.jit.si"))
                            .setAudioOnly(true)
                            .setRoom(receiverRoom + senderRoom)
                            .build();
                    JitsiMeetActivity.launch(AudioCallComing.this, options);
                    finish();
                }catch (Exception e){}
            }
        });

        binding.btnDecline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String recieveId = getIntent().getStringExtra("userID");
                final String senderId = auth.getUid();
                database = FirebaseDatabase.getInstance();

                HashMap<String, Object> obj = new HashMap<>();
                obj.put(senderId , "false");
                database.getReference().child("checkCall").child("Audio").child("outComeRoom").child(recieveId)
                        .updateChildren(obj);

                Intent intent = new Intent(AudioCallComing.this ,MainActivity.class );
                startActivity(intent);
            }
        });



    }
}
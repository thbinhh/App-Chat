package com.example.demoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.demoapp.databinding.ActivityAudioCallOutComingBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;

import java.net.URL;
import java.util.HashMap;

public class AudioCallOutGoing extends AppCompatActivity {
    ImageView imageView;
    TextView tvname, tvprof;
    FloatingActionButton declinebtn;
    String receiver_url, receiver_prof, receiver_name, receiver_token;
    ActivityAudioCallOutComingBinding binding;
    FirebaseDatabase database;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().hide();
        binding = ActivityAudioCallOutComingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        final String senderId = auth.getUid();

        String recieveId = getIntent().getStringExtra("userID");
        String userName = getIntent().getStringExtra("userName");
        String profilePic = getIntent().getStringExtra("profilePic");

        final String senderRoom = senderId + recieveId;
        final String receiverRoom = recieveId + senderId;

        binding.callusername.setText(userName);
        Picasso.get().load(profilePic).placeholder(R.drawable.avatar).into(binding.profileimagecall);

        HashMap<String, Object> obj = new HashMap<>();
        obj.put(recieveId, "true");
        database.getReference().child("checkCall").child("Audio").child("inComeRoom").child(senderId)
                .updateChildren(obj);
        FirebaseDatabase.getInstance().getReference().child("checkBlock")
                .child(senderRoom)
                .addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        try {
                            if (!snapshot.child("isblockhide").getValue().toString().equals("true")) {
                                HashMap<String, Object> obj = new HashMap<>();
                                obj.put(recieveId, "true");
                                database.getReference().child("checkCall").child("Audio").child("inComeRoom").child(senderId)
                                        .updateChildren(obj);
                            } else{}
                        } catch (Exception e) {}
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });


        Handler mHandler = new Handler();
        Runnable my_runnable = new Runnable() {
            @Override
            public void run() {

            }
        };
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {

                binding.btnAppect.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        HashMap<String, Object> obj = new HashMap<>();
                        obj.put(recieveId, "false");
                        database.getReference().child("checkCall").child("Audio").child("inComeRoom").child(senderId)
                                .updateChildren(obj);
                        mHandler.removeCallbacksAndMessages(null);
                        Intent intent = new Intent(AudioCallOutGoing.this , MainActivity.class);
                        startActivity(intent);
                    }
                });

                final String senderRoom = senderId + recieveId;
                final String receiverRoom = recieveId + senderId;
                FirebaseDatabase.getInstance().getReference().child("checkCall").child("Audio")
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                try {
                                    if(snapshot.child("outComeRoom").child(senderId).child(recieveId).getValue().toString().equals("true") )
                                    {
                                        JitsiMeetConferenceOptions options = new JitsiMeetConferenceOptions.Builder()
                                                .setServerURL(new URL("https://meet.jit.si"))
                                                .setAudioOnly(true)
                                                .setRoom(senderRoom+receiverRoom)
                                                .build();
                                        JitsiMeetActivity.launch(AudioCallOutGoing.this, options);
                                        finish();
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




    }
}
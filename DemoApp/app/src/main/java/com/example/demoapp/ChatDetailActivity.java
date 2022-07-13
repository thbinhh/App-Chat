package com.example.demoapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaParser;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.demoapp.Adapter.ChatAdapter;
import com.example.demoapp.Adapter.FragmentsAdapter;
import com.example.demoapp.Models.MessageModel;
import com.example.demoapp.databinding.ActivityChatDetailBinding;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;

import java.io.InvalidObjectException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class ChatDetailActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    ActivityChatDetailBinding binding;
    FirebaseDatabase database;
    FirebaseStorage storage;
    FirebaseAuth auth;
    Dialog dialog;

//    final String senderId = auth.getUid();

    // lấy về uid của người dùng
 //   String recieveId = getIntent().getStringExtra("userID");      // lấy dữ liệu userID truyền qua thông qua key = userID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        dialog = new Dialog(this);
        binding.send.setVisibility(View.GONE);
        binding.show.setVisibility(View.GONE);
        getSupportActionBar().hide();
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        final String senderId = auth.getUid();
        storage = FirebaseStorage.getInstance();


//        binding.totalBtnBlock.setVisibility(View.GONE);
//        binding.btnUnadd.setVisibility(View.GONE);
//        binding.btnUnadd.setVisibility(View.GONE);
//        binding.btnAdd.setVisibility(View.GONE);



        NavigationView navigationView = findViewById(R.id.nav_layout);
        navigationView.setNavigationItemSelectedListener(ChatDetailActivity.this);

        // lấy về uid của người dùng

        String recieveId = getIntent().getStringExtra("userID");      // lấy dữ liệu userID truyền qua thông qua key = userID
        String userName = getIntent().getStringExtra("userName");     // lấy dữ liệu userID truyền qua thông qua key = userName
        String profilePic = getIntent().getStringExtra("profilePic");  // lấy dữ liệu userID truyền qua thông qua key = profilePic

        binding.userName.setText(userName);   // set userName = với thông tin người dùng
        Picasso.get().load(profilePic).placeholder(R.drawable.avatar).into(binding.profileImage);  // load image

        binding.backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChatDetailActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });   // nút back

        final ArrayList<MessageModel> messageModels = new ArrayList<>();  // tạo mảng MessageModel
        final ChatAdapter chatAdapter = new ChatAdapter(messageModels, this, recieveId); // biến chatadapter()

        binding.chatRecyclerView.setAdapter(chatAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd (true);
        binding.chatRecyclerView.setLayoutManager(linearLayoutManager);


        binding.hide.setVisibility(View.VISIBLE);
        binding.show.setVisibility(View.GONE);

        final String senderRoom = senderId + recieveId;
        final String receiverRoom = recieveId + senderId;

//        HashMap<String, Object> obj = new HashMap<>();
//        obj.put("isSeen", "true");
//        database.getReference().child("Checkseen").child(senderRoom)
//                .updateChildren(obj);




        Handler mHandler = new Handler();
        Runnable my_runnable = new Runnable() {
            @Override
            public void run() {
                // your code here
            }
        };
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                FirebaseDatabase.getInstance().getReference().child("checkCall").child("Video")
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                try {
                                    if(snapshot.child("inComeRoom").child(recieveId).child(senderId).getValue().toString().equals("true") )
                                    {
                                        Intent intent = new Intent(ChatDetailActivity.this, VideoCallComing.class);
                                        intent.putExtra("userID", recieveId);
                                        intent.putExtra("userName", userName);
                                        intent.putExtra("profilePic", profilePic);
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

        Handler mHandler1 = new Handler();
        Runnable my_runnable1 = new Runnable() {
            @Override
            public void run() {
                // your code here
            }
        };
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                FirebaseDatabase.getInstance().getReference().child("checkCall").child("Audio")
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                try {
                                    if(snapshot.child("inComeRoom").child(recieveId).child(senderId).getValue().toString().equals("true") )
                                    {
                                        Intent intent = new Intent(ChatDetailActivity.this, AudioCallComing.class);
                                        intent.putExtra("userID", recieveId);
                                        intent.putExtra("userName", userName);
                                        intent.putExtra("profilePic", profilePic);
                                        startActivity(intent);
                                        mHandler1.removeCallbacksAndMessages(null);
                                    }
                                }catch(Exception e){ }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }
                        });


                mHandler1.postDelayed(this, 1000);
            }
        }, 1000);



        database.getReference().child("chats")
                .child(receiverRoom)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        messageModels.clear();
                        for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                            MessageModel model = snapshot1.getValue(MessageModel.class);
                            model.setMessageId(snapshot1.getKey());
                            messageModels.add(model);
                        }
                        chatAdapter.notifyDataSetChanged();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


        binding.show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.showInfor.setVisibility(View.VISIBLE);
                binding.show.setVisibility(View.GONE);
                binding.hide.setVisibility(View.VISIBLE);

            }
        });
        binding.hide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.showInfor.setVisibility(View.GONE);
                binding.hide.setVisibility(View.GONE);
                binding.show.setVisibility(View.VISIBLE);

            }
        });
        FirebaseDatabase.getInstance().getReference().child("Status")
                .child(recieveId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        try {
                            if (snapshot.child("users").getValue().toString().equals("Ngu")) {
                             binding.txtShowstatus.setText("(Đang Ngủ)");
                            }
                            else if (snapshot.child("users").getValue().toString().equals("LamViec")) {
                                binding.txtShowstatus.setText("(Bận)");
                            }
                                else
                                binding.txtShowstatus.setText("(Rảnh)");


                        }catch(Exception e){ }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        binding.mainEnterMessage.setVisibility(View.VISIBLE);
                    }
                });


        FirebaseDatabase.getInstance().getReference().child("checkBlock")
                    .child(senderRoom)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            try {
                                if (snapshot.child("isblock").getValue().toString().equals("true")) {
                                    binding.mainEnterMessage.setVisibility(View.GONE);
                                    binding.sendItem.setVisibility(View.GONE);
                                    binding.tvCall.setEnabled(false);
                                } else {
                                    binding.enterMessage.setVisibility(View.VISIBLE);
                                    binding.tvCall.setEnabled(true);
                                }

                            }catch(Exception e){ }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            binding.mainEnterMessage.setVisibility(View.VISIBLE);
                        }
                    });
            FirebaseDatabase.getInstance().getReference().child("checkBlock")
                    .child(senderRoom)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            try {
                                if (snapshot.child("isblock").getValue().toString().equals("true")) {
                                    binding.mainEnterMessage.setVisibility(View.GONE);
                                    binding.sendItem.setVisibility(View.GONE);
                                } else
                                    binding.enterMessage.setVisibility(View.VISIBLE);
                            }catch (Exception e) {}
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            binding.mainEnterMessage.setVisibility(View.VISIBLE);
                        }
                    });



            FirebaseDatabase.getInstance().getReference().child("checkBlock")
                    .child(senderRoom)
                    .addListenerForSingleValueEvent(new ValueEventListener() {

                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            try {
                                if (snapshot.child("isblockhide").getValue().toString().equals("true")) {
                                    binding.txtShowblock.setText("Block Chat");
                                } else
                                    binding.txtShowblock.setText("Friend");
                            } catch (Exception e) {}

                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            binding.mainEnterMessage.setVisibility(View.VISIBLE);
                        }
                    });


        binding.enterMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    binding.send.setVisibility(View.GONE);
                binding.sendItem.setVisibility(View.VISIBLE);

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if(charSequence.length() > 0) {
                    binding.send.setVisibility(View.VISIBLE);
                    binding.sendItem.setVisibility(View.GONE);

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        binding.tvCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChatDetailActivity.this, VideoCallOutGoing.class);
                intent.putExtra("userID", recieveId);
                intent.putExtra("userName", userName);
                intent.putExtra("profilePic", profilePic);
                startActivity(intent);
            }
        });
        binding.tvCallAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(ChatDetailActivity.this, AudioCallOutGoing.class);
                intent1.putExtra("userID", recieveId);
                intent1.putExtra("userName", userName);
                intent1.putExtra("profilePic", profilePic);
                startActivity(intent1);
            }
        });


        binding.send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, Object> obj = new HashMap<>();
                obj.put("isSeen", "false");
                database.getReference().child("Checkseen").child(receiverRoom)
                        .updateChildren(obj);

                FirebaseDatabase.getInstance().getReference().child("checkBlock")
                        .child(receiverRoom)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                try {
                                    if (snapshot.child("isblockhide").getValue().toString().equals("true") != true) {
                                        if (!binding.enterMessage.getText().toString().isEmpty()) {
                                            binding.sendItemShow.setVisibility(View.GONE);
                                            String message = binding.enterMessage.getText().toString();
                                            final MessageModel model = new MessageModel(senderId, message, "","");
                                            model.setTimestamp(new Date().getTime());
                                            binding.enterMessage.setText("");
                                            database.getReference().child("chats")
                                                    .child(senderRoom)
                                                    .push()
                                                    .setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    database.getReference().child("chats")
                                                            .child(receiverRoom)
                                                            .push()
                                                            .setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void unused) {
                                                        }
                                                    });
                                                }
                                            });
                                        }
                                    } else {
                                        String message = binding.enterMessage.getText().toString();
                                        final MessageModel model = new MessageModel(senderId, message, "","");
                                        model.setTimestamp(new Date().getTime());
                                        database.getReference().child("chats")
                                                .child(receiverRoom)
                                                .push()
                                                .setValue(model);
                                    }
                                }catch (Exception e) {
                                    String message = binding.enterMessage.getText().toString();
                                    final MessageModel model = new MessageModel(senderId, message, "","");
                                    model.setTimestamp(new Date().getTime());
                                    binding.enterMessage.setText("");
                                    database.getReference().child("chats")
                                            .child(senderRoom)
                                            .push()
                                            .setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            database.getReference().child("chats")
                                                    .child(receiverRoom)
                                                    .push()
                                                    .setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {

                                                }
                                            });
                                        }
                                    });
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                binding.mainEnterMessage.setVisibility(View.VISIBLE);
                            }
                        });
            }
        });

        binding.sendItemAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("audio/*");
                startActivityForResult(intent, 24);

            }
        });



        binding.sendItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 25);

            }
        });

    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        final String senderId = auth.getUid();


        // lấy về uid của người dùng
        String recieveId = getIntent().getStringExtra("userID");      // lấy dữ liệu userID truyền qua thông qua key = userID
        final String senderRoom = senderId + recieveId;
        final String receiverRoom = recieveId + senderId;

            if (requestCode == 25) {
                if (resultCode == Activity.RESULT_OK) {
                    Uri sFile = data.getData();
                    //   binding.profileImage.setImageURI(sFile);

                    final StorageReference reference = storage.getReference().child("profile_pic_mess")
                            .child(senderRoom).child(data.toString());
                    reference.putFile(sFile).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // thêm hàm ở đây
                            reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    FirebaseDatabase.getInstance().getReference().child("checkBlock")
                                            .child(receiverRoom)
                                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    try {
                                                        if (snapshot.child("isblockhide").getValue().toString().equals("true") != true) {


                                                            HashMap<String, Object> obj = new HashMap<>();
                                                            obj.put("isSeen", "false");
                                                            database.getReference().child("Checkseen").child(receiverRoom)
                                                                    .updateChildren(obj);

                                                            final MessageModel model = new MessageModel(senderId, "", uri.toString(), "");
                                                            model.setTimestamp(new Date().getTime());
                                                            binding.enterMessage.setText("");
                                                            database.getReference().child("chats")
                                                                    .child(senderRoom)
                                                                    .push()
                                                                    .setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void unused) {
                                                                    database.getReference().child("chats")
                                                                            .child(receiverRoom)
                                                                            .push()
                                                                            .setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                        @Override
                                                                        public void onSuccess(Void unused) {
                                                                        }
                                                                    });
                                                                }
                                                            });
                                                        } else {
                                                            HashMap<String, Object> obj = new HashMap<>();
                                                            obj.put("isSeen", "false");
                                                            database.getReference().child("Checkseen").child(receiverRoom)
                                                                    .updateChildren(obj);
                                                            String message = binding.enterMessage.getText().toString();
                                                            final MessageModel model = new MessageModel(senderId, "", uri.toString(), "");
                                                            model.setTimestamp(new Date().getTime());
                                                            database.getReference().child("chats")
                                                                    .child(receiverRoom)
                                                                    .push()
                                                                    .setValue(model);
                                                        }
                                                    } catch (Exception e) {
                                                        HashMap<String, Object> obj = new HashMap<>();
                                                        obj.put("isSeen", "false");
                                                        database.getReference().child("Checkseen").child(receiverRoom)
                                                                .updateChildren(obj);
                                                        final MessageModel model = new MessageModel(senderId, "", uri.toString(), "");
                                                        model.setTimestamp(new Date().getTime());
                                                        binding.enterMessage.setText("");
                                                        database.getReference().child("chats")
                                                                .child(senderRoom)
                                                                .push()
                                                                .setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void unused) {
                                                                database.getReference().child("chats")
                                                                        .child(receiverRoom)
                                                                        .push()
                                                                        .setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void unused) {

                                                                    }
                                                                });
                                                            }
                                                        });
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {
                                                    binding.mainEnterMessage.setVisibility(View.VISIBLE);
                                                }
                                            });
                                }
                            });   // hết hàm
                        }
                    });


                } else {
                    // Intent intent = new Intent(SettingsActivity.this, SettingsActivity.class);
                    // startActivity(intent);
                }
            }


    if (requestCode == 24) {
        if (resultCode == Activity.RESULT_OK) {
            Uri sFile = data.getData();
            //   binding.profileImage.setImageURI(sFile);

            final StorageReference reference = storage.getReference().child("profile_audio_mess")
                    .child(senderRoom).child(data.toString());
            reference.putFile(sFile).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // thêm hàm ở đây
                    reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            FirebaseDatabase.getInstance().getReference().child("checkBlock")
                                    .child(receiverRoom)
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            try {
                                                if (snapshot.child("isblockhide").getValue().toString().equals("true") != true) {

                                                    HashMap<String, Object> obj = new HashMap<>();
                                                    obj.put("isSeen", "false");
                                                    database.getReference().child("Checkseen").child(receiverRoom)
                                                            .updateChildren(obj);

                                                    final MessageModel model = new MessageModel(senderId, "", "", uri.toString());
                                                    model.setTimestamp(new Date().getTime());
                                                    binding.enterMessage.setText("");
                                                    database.getReference().child("chats")
                                                            .child(senderRoom)
                                                            .push()
                                                            .setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void unused) {
                                                            database.getReference().child("chats")
                                                                    .child(receiverRoom)
                                                                    .push()
                                                                    .setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void unused) {
                                                                }
                                                            });
                                                        }
                                                    });
                                                } else {
                                                    HashMap<String, Object> obj = new HashMap<>();
                                                    obj.put("isSeen", "false");
                                                    database.getReference().child("Checkseen").child(receiverRoom)
                                                            .updateChildren(obj);
                                                    String message = binding.enterMessage.getText().toString();
                                                    final MessageModel model = new MessageModel(senderId, "", "", uri.toString());
                                                    model.setTimestamp(new Date().getTime());
                                                    database.getReference().child("chats")
                                                            .child(receiverRoom)
                                                            .push()
                                                            .setValue(model);
                                                }
                                            } catch (Exception e) {
                                                HashMap<String, Object> obj = new HashMap<>();
                                                obj.put("isSeen", "false");
                                                database.getReference().child("Checkseen").child(receiverRoom)
                                                        .updateChildren(obj);
                                                final MessageModel model = new MessageModel(senderId, "", "", uri.toString());
                                                model.setTimestamp(new Date().getTime());
                                                binding.enterMessage.setText("");
                                                database.getReference().child("chats")
                                                        .child(senderRoom)
                                                        .push()
                                                        .setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        database.getReference().child("chats")
                                                                .child(receiverRoom)
                                                                .push()
                                                                .setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void unused) {

                                                            }
                                                        });
                                                    }
                                                });
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                            binding.mainEnterMessage.setVisibility(View.VISIBLE);
                                        }
                                    });
                        }
                    });   // hết hàm
                }
            });


        } else {
            // Intent intent = new Intent(SettingsActivity.this, SettingsActivity.class);
            // startActivity(intent);
        }
    }
    }





    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        final String senderId = auth.getUid();
        String recieveId = getIntent().getStringExtra("userID");
        final String senderRoom = senderId + recieveId;
        final String receiverRoom = recieveId + senderId;
        switch (item.getItemId())
        {
            case R.id.UnBlock:
                new AlertDialog.Builder(ChatDetailActivity.this)
                        .setTitle("Unblock")
                        .setMessage("Are you sure want to Remove ? (This function takes only effect when two people press unblock at the same time)")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

//                                binding.btnBlock.setVisibility(View.VISIBLE);
//                                binding.btnUnblock.setVisibility(View.GONE);
                                HashMap<String, Object> obj = new HashMap<>();
                                obj.put("isblock", "false");
                                database.getReference().child("checkBlock").child(senderRoom)
                                        .updateChildren(obj);


                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.dismiss();
                    }
                }).show();
                return false;
            case R.id.Block:
                new AlertDialog.Builder(ChatDetailActivity.this)
                        .setTitle("Block")
                        .setMessage("Are you sure want to block your friend? (Unblock function only takes effect when two people press unblock)")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
//                                binding.btnBlock.setVisibility(View.GONE);
//                                binding.btnUnblock.setVisibility(View.VISIBLE);

                                HashMap<String, Object> obj = new HashMap<>();
                                obj.put("isblock", "true");
                                database.getReference().child("checkBlock").child(senderRoom)
                                        .updateChildren(obj).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        obj.put("isblock", "true");
                                        database.getReference().child("checkBlock")
                                                .child(receiverRoom)
                                                .updateChildren(obj);
                                    }

                                });
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.dismiss();
                    }
                }).show();
                return false;

            case R.id.BlockChat:
                new AlertDialog.Builder(ChatDetailActivity.this)
                        .setTitle("Block")
                        .setMessage("Are you sure want to block your friend? (Unblock function only takes effect when two people press unblock)")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
//                                binding.btnBlockHide.setVisibility(View.GONE);
//                                binding.btnUnblockHide.setVisibility(View.VISIBLE);
                                binding.txtShowblock.setText("Block Chat");
                                HashMap<String, Object> obj = new HashMap<>();
                                obj.put("isblockhide", "true");
                                database.getReference().child("checkBlock").child(senderRoom)
                                        .updateChildren(obj);
//                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                    @Override
//                                    public void onSuccess(Void unused) {
//                                        obj.put("isblockhide", "false");
//                                        database.getReference().child("checkBlock")
//                                                .child(receiverRoom)
//                                                .updateChildren(obj);
//                                    }
//
//                                });
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.dismiss();
                    }
                }).show();
                return false;
            case R.id.UnBlockChat:
                new AlertDialog.Builder(ChatDetailActivity.this)
                        .setTitle("Unblock")
                        .setMessage("Are you sure want to Remove ? (This function takes only effect when two people press unblock at the same time)")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
//                                binding.btnBlockHide.setVisibility(View.VISIBLE);
//                                binding.btnUnblockHide.setVisibility(View.GONE);
                                binding.txtShowblock.setText("Friend");
                                HashMap<String, Object> obj = new HashMap<>();
                                obj.put("isblockhide", "false");
                                database.getReference().child("checkBlock").child(senderRoom)
                                        .updateChildren(obj);
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.dismiss();
                    }
                }).show();
                return false;


        }
        return super.onOptionsItemSelected(item);
    }



    public class SenderViewHolder extends RecyclerView.ViewHolder{
        TextView senderMsg, senderTime;
        ImageView imageView;
        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);
            senderMsg = itemView.findViewById(R.id.senderText);
            senderTime = itemView.findViewById(R.id.senderTime);
            imageView = itemView.findViewById(R.id.image_sent);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        HashMap<String, Object> obj = new HashMap<>();
        obj.put("statusof", "Offline");
        database.getReference().child("Users").child(FirebaseAuth.getInstance().getUid())
                .updateChildren(obj);


    }
    @Override
    protected void onResume() {
        super.onResume();
        HashMap<String, Object> obj = new HashMap<>();
        obj.put("statusof", "Online");
        database.getReference().child("Users").child(FirebaseAuth.getInstance().getUid())
                .updateChildren(obj);
    }
}
package com.example.demoapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.demoapp.databinding.ActivityUpStoryBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class UpStory extends AppCompatActivity {
    ActivityUpStoryBinding binding;
    FirebaseAuth mAuth;
    Dialog dialog;
    FirebaseDatabase database;
    FirebaseStorage storage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        binding = ActivityUpStoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        storage = FirebaseStorage.getInstance();



        binding.btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                HashMap<String, Object> obj1 = new HashMap<>();
                obj1.put("text", binding.txtStory.getText().toString());
                database.getReference().child("Story").child(mAuth.getUid())
                        .updateChildren(obj1);

                HashMap<String, Object> obj5 = new HashMap<>();
                obj5.put("checkStory", "true");
                database.getReference().child("Users").child(mAuth.getUid())
                        .updateChildren(obj5);

                HashMap<String, Object> obj2 = new HashMap<>();
                obj2.put("like", 0 );
                database.getReference().child("Story").child(mAuth.getUid())
                        .updateChildren(obj2);

                HashMap<String, Object> obj3 = new HashMap<>();
                obj3.put("said", 0 );
                database.getReference().child("Story").child(mAuth.getUid())
                        .updateChildren(obj3);

                HashMap<String, Object> obj4 = new HashMap<>();
                obj4.put("laugh", 0 );
                database.getReference().child("Story").child(mAuth.getUid())
                        .updateChildren(obj4);
                

                database.getReference().child("FeelStory").child(mAuth.getUid()).removeValue();

                Toast.makeText(UpStory.this, "Your story has been uploaded.", Toast.LENGTH_SHORT).show();


            }
        });

        binding.SelectImage.setOnClickListener(new View.OnClickListener() {
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


        if (resultCode == Activity.RESULT_OK) {
            Uri sFile = data.getData();
            binding.storyImageShow.setImageURI(sFile);

            final StorageReference reference = storage.getReference().child("profile_pic_story")
                    .child(FirebaseAuth.getInstance().getUid());
            reference.putFile(sFile).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            database.getReference().child("Story").child(FirebaseAuth.getInstance().getUid())
                                    .child("profilePic").setValue(uri.toString());
                        }
                    });
                }
            });
        }
        else
        {
            Intent intent = new Intent(UpStory.this, UpStory.class);
            startActivity(intent);
        }

    }
}
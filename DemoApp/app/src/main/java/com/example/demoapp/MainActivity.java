package com.example.demoapp;

import static org.webrtc.VideoFrameDrawer.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
//import androidx.navigation.ui.AppBarConfiguration;


import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.ims.ImsMmTelManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.demoapp.Adapter.ChatAdapter;
import com.example.demoapp.Adapter.FragmentsAdapter;
import com.example.demoapp.Models.Users;
import com.example.demoapp.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener  {


    ActivityMainBinding binding;
    FirebaseAuth mAuth;
    Dialog dialog;
    FirebaseDatabase database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        database = FirebaseDatabase.getInstance();
//        binding.lnTablayout.setVisibility(View.GONE);
//        binding.showTablayout1.setVisibility(View.GONE);

        setContentView(binding.getRoot());
        dialog = new Dialog(this);
        mAuth = FirebaseAuth.getInstance();





        final String senderId = mAuth.getUid();

        HashMap<String, Object> obj = new HashMap<>();

        obj.put("statusof", "Online");
        database.getReference().child("Users").child(FirebaseAuth.getInstance().getUid())
                .updateChildren(obj);


        binding.viewPager.setAdapter(new FragmentsAdapter(getSupportFragmentManager()));
        binding.tabLayout.setupWithViewPager(binding.viewPager);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        binding.tabLayout.getTabAt(0).setIcon(R.drawable.ic_baseline_chat_bubble_outline_24_123);
        binding.tabLayout.getTabAt(1).setIcon(R.drawable.add);
        binding.tabLayout.getTabAt(2).setIcon(R.drawable.ic_baseline_auto_stories_24);



        NavigationView navigationView = findViewById(R.id.nav_layout);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);

    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.settings:
                Intent intent2 = new Intent(MainActivity.this,SettingsActivity.class);
                startActivity(intent2);
                break;

            case R.id.findFriend:
                Intent intent4 = new Intent(MainActivity.this,FindFriendsActivity.class);
                startActivity(intent4);
                break;
            case R.id.delete:

              //  database.getReference().child("Users").child(mAuth.getUid()).removeValue();
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                user.delete()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(MainActivity.this, "User is Delete", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            case R.id.logout:

                HashMap<String, Object> obj = new HashMap<>();

        obj.put("statusof", "Offline");
       database.getReference().child("Users").child(FirebaseAuth.getInstance().getUid())
               .updateChildren(obj);
                mAuth.signOut();
                Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(intent);
                break;

            case R.id.aboutus:

                dialog.setContentView(R.layout.aboutus);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
                ImageView nav = (ImageView) dialog.findViewById(R.id.btn_nav1);
                nav.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Uri uri = Uri.parse("https://www.youtube.com/watch?v=TU-yDCgpbZw");
                        startActivity(new Intent(Intent.ACTION_VIEW,uri));
                    }
                });
            case R.id.changepassword:

                Intent intent1 = new Intent(MainActivity.this, ChangePasswordActivity.class);
                startActivity(intent1);
                return false;
            case R.id.ngu:
                HashMap<String, Object> obj1 = new HashMap<>();
                obj1.put("users", "Ngu");
                database.getReference().child("Status").child(mAuth.getUid())
                        .updateChildren(obj1);
                return false;
            case R.id.lamviec:
                HashMap<String, Object> obj2 = new HashMap<>();
                obj2.put("users", "LamViec");
                database.getReference().child("Status").child(mAuth.getUid())
                        .updateChildren(obj2);
                return false;
            case R.id.ranh:
                HashMap<String, Object> obj3 = new HashMap<>();
                obj3.put("users", "Ranh");
                database.getReference().child("Status").child(mAuth.getUid())
                        .updateChildren(obj3);
                return false;
            case R.id.story:
                Intent intent3 = new Intent(MainActivity.this, UpStory.class);
                startActivity(intent3);
                return false;

            case R.id.delete_story:
                database.getReference().child("Story").child(mAuth.getUid())
                        .removeValue();
                HashMap<String, Object> obj5 = new HashMap<>();
                obj5.put("checkStory", "false");
                database.getReference().child("Users").child(mAuth.getUid())
                        .updateChildren(obj5);



        }

        return super.onOptionsItemSelected(item);
    }


//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        HashMap<String, Object> obj = new HashMap<>();
//
//        obj.put("statusof", "Offline");
//        database.getReference().child("Users").child(FirebaseAuth.getInstance().getUid())
//                .updateChildren(obj);
//    }

//    @Override
//    protected void onPause() {
//        super.onPause();
//        HashMap<String, Object> obj = new HashMap<>();
//        mAuth.signOut();
//        obj.put("statusof", "Offline");
//        database.getReference().child("Users").child(FirebaseAuth.getInstance().getUid())
//                .updateChildren(obj);
//    }


    @Override
    protected void onResume() {
        super.onResume();

        HashMap<String, Object> obj = new HashMap<>();

        obj.put("statusof", "Online");
        database.getReference().child("Users").child(FirebaseAuth.getInstance().getUid())
                .updateChildren(obj);
    }


}
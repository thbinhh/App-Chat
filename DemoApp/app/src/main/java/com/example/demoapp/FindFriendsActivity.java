package com.example.demoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import com.example.demoapp.databinding.ActivityFindFriendsBinding;
import com.example.demoapp.Models.Contacts;
import com.example.demoapp.databinding.ActivitySignInBinding;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class FindFriendsActivity extends AppCompatActivity {
    ActivityFindFriendsBinding binding;
    private Toolbar mToolbar;
    private RecyclerView FindFriendsRecyclerList;
    private DatabaseReference UsersRef;
    FirebaseDatabase database;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFindFriendsBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        FindFriendsRecyclerList = (RecyclerView) findViewById(R.id.find_friends_recycler_list);
        FindFriendsRecyclerList.setLayoutManager(new LinearLayoutManager(this));
        database = FirebaseDatabase.getInstance();
        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        mToolbar = (Toolbar) findViewById(R.id.find_friends_toolbar);
//        setSupportActionBar(mToolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        getSupportActionBar().setTitle("Find Friends");

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<Contacts> options =
                new FirebaseRecyclerOptions.Builder<Contacts>()
                        .setQuery(UsersRef, Contacts.class)
                        .build();

        FirebaseRecyclerAdapter<Contacts, FindFriendViewHolder> adapter = new FirebaseRecyclerAdapter<Contacts, FindFriendViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull FindFriendViewHolder holder, int position, @NonNull Contacts model) {
                holder.check_contact.setVisibility(View.GONE);

                if(model.getStatusof().equals("Online") ) {
                    holder.check_contact.setVisibility(View.VISIBLE);
                }
                else
                    holder.check_contact.setVisibility(View.GONE);

                holder.username.setText(model.getUserName());
                holder.userstatus.setText(model.getStatus());



                binding.enterMessage.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        if(charSequence.length() > 0) {

                                holder.itemView.setVisibility(View.GONE);
                                holder.username.setVisibility(View.GONE);
                                holder.profileImage.setVisibility(View.GONE);
                                holder.userstatus.setVisibility(View.GONE);
                                holder.check_contact.setVisibility(View.GONE);
                        }
                        else  holder.itemView.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                            if(editable.toString().equals(model.getUserName())){
                                holder.username.setText(model.getUserName());
                                holder.userstatus.setText(model.getStatus());
                                holder.itemView.setVisibility(View.VISIBLE);
                                holder.username.setVisibility(View.VISIBLE);
                                holder.profileImage.setVisibility(View.VISIBLE);
                                holder.userstatus.setVisibility(View.VISIBLE);
                                holder.check_contact.setVisibility(View.VISIBLE);
                            }
                    }
                });



                    Picasso.get().load(model.getProfilePic()).placeholder(R.drawable.avatar).into(holder.profileImage);

                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String visit_user_id = getRef(holder.getAdapterPosition()).getKey();
                            Intent profileIntent = new Intent(FindFriendsActivity.this, ProfileActivity.class);
                            profileIntent.putExtra("visit_user_id", visit_user_id);
                            startActivity(profileIntent);
                        }
                    });


            }

            @NonNull
            @Override
            public FindFriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.users_display_layout,parent, false);
                FindFriendViewHolder viewHolder = new FindFriendViewHolder(view);
                return viewHolder;
            }
        };
        FindFriendsRecyclerList.setAdapter(adapter);
        adapter.startListening();

    }


    public static class FindFriendViewHolder extends RecyclerView.ViewHolder {

        TextView username, userstatus;
        CircleImageView profileImage;
        ImageButton check_contact;



        public FindFriendViewHolder(@NonNull View itemView) {
            super(itemView);

            username = itemView.findViewById(R.id.users_profile_name);
            profileImage = itemView.findViewById(R.id.users_profile_image);
            userstatus = itemView.findViewById(R.id.user_status);
            check_contact = itemView.findViewById(R.id.online_offline_contact);


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
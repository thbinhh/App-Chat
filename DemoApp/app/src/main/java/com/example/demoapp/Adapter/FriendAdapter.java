package com.example.demoapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demoapp.ChatDetailActivity;
import com.example.demoapp.Models.Users;
import com.example.demoapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.ViewHolder> {
    ArrayList<Users> list;
    Context context;

    public FriendAdapter(ArrayList<Users> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sample_show_user,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendAdapter.ViewHolder holder, int position) {
        Users users = list.get(position);
        Picasso.get().load(users.getProfilePic()).placeholder(R.drawable.avatar3).into(holder.image);
        holder.userName.setText(users.getUserName());


        //check status
        holder.check.setVisibility(View.GONE);

        if(users.getStatusof().equals("Online") ) {
            holder.check.setVisibility(View.VISIBLE);
        }
        else
            holder.check.setVisibility(View.GONE);


        FirebaseDatabase.getInstance().getReference().child("chats")
                .child(users.getUserId() + FirebaseAuth.getInstance().getUid()  )
                .orderByChild("timestamp")
                .limitToLast(1)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.hasChildren()){
                            for(DataSnapshot snapshot1:snapshot.getChildren())
                            {
                                try {
                                    holder.lastMessage.setText(snapshot1.child("message").getValue().toString());
                                } catch (Exception e )  {      holder.lastMessage.setText(" "); }

                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


//        FirebaseDatabase.getInstance().getReference().child("Users")
//                .child(FirebaseAuth.getInstance().getUid())
//                 .orderByChild("statusof")
//                .limitToLast(1)
//                .addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        if(snapshot.hasChildren()){
//                            for(DataSnapshot snapshot1:snapshot.getChildren() )
//                            {
//                                holder.online.setText( snapshot1.child("status").getValue().toString() );
////                                if(snapshot1.child("statusof").getValue().toString().equals("online") ==true )
////                                {
////                                    holder.check.setVisibility(View.VISIBLE);
////                                }
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChatDetailActivity.class);
                Intent intent2 = new Intent(context, ChatAdapter.class);
                intent2.putExtra("userID",users.getUserId());


                intent.putExtra("userID",users.getUserId());
                intent.putExtra("profilePic",users.getProfilePic());
                intent.putExtra("userName",users.getUserName());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView image;
        TextView userName, lastMessage, online;
        ImageButton check;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.profile_image);
            userName = itemView.findViewById(R.id.userNameList);
            lastMessage = itemView.findViewById(R.id.lastMessage);
            check = itemView.findViewById(R.id.online_offline);

        }
    }
}

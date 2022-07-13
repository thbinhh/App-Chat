package com.example.demoapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.HashMap;

public class StoryAdapter extends RecyclerView.Adapter<StoryAdapter.ViewHolder> {
    ArrayList<Users> list;
    Context context;
    FirebaseDatabase database;
    FirebaseAuth mAuth;

    public StoryAdapter(ArrayList<Users> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sample_show_user_story,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StoryAdapter.ViewHolder holder, int position) {
        Users users = list.get(position);

        Picasso.get().load(users.getProfilePic()).placeholder(R.drawable.avatar3).into(holder.image);

//try {
//    Picasso.get().load(users.getProfilePic()).into(holder.image2);
//} catch (Exception E) {}


        holder.userName.setText(users.getUserName());


         //check status
        holder.check.setVisibility(View.GONE);
        holder.likered.setVisibility(View.GONE);
        holder.btsaidred.setVisibility(View.GONE);
        holder.btlaughred.setVisibility(View.GONE);

        if(users.getStatusof().equals("Online") ) {
            holder.check.setVisibility(View.VISIBLE);
        }
        else
            holder.check.setVisibility(View.GONE);

        // check button if users come to console again
        FirebaseDatabase.getInstance().getReference().child("FeelStory")
                 .child(users.getUserId())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        try {
                            mAuth = FirebaseAuth.getInstance();

                            if(snapshot.child(mAuth.getUid()).child("like").getValue().toString().equals("true")){
                                holder.likered.setVisibility(View.VISIBLE);
                                holder.btlike.setVisibility(View.GONE);

                                //block 2 button
                                holder.btlaugh.setEnabled(false);
                                holder.btsaid.setEnabled(false);
                            }


                            else if(snapshot.child(mAuth.getUid()).child("said").getValue().toString().equals("true")){
                                holder.btsaidred.setVisibility(View.VISIBLE);
                                holder.btsaid.setVisibility(View.GONE);

                                holder.btlaugh.setEnabled(false);
                                holder.btlike.setEnabled(false);
                            }


                           else if(snapshot.child(mAuth.getUid()).child("laugh").getValue().toString().equals("true")){
                                holder.btlaughred.setVisibility(View.VISIBLE);
                                holder.btlaugh.setVisibility(View.GONE);

                                holder.btsaid.setEnabled(false);
                                holder.btlike.setEnabled(false);
                            }


                        } catch (Exception e )  {      holder.textStory.setText(" "); }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
        // check button if users come to console again
        FirebaseDatabase.getInstance().getReference().child("FeelStory")
                .child(users.getUserId())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        try {
                            mAuth = FirebaseAuth.getInstance();


                            if(snapshot.child(mAuth.getUid()).child("said").getValue().toString().equals("true")){
                                holder.btsaidred.setVisibility(View.VISIBLE);
                                holder.btsaid.setVisibility(View.GONE);

                                holder.btlaugh.setEnabled(false);
                                holder.btlike.setEnabled(false);
                            }





                        } catch (Exception e )  {      holder.textStory.setText(" "); }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
        // check button if users come to console again
        FirebaseDatabase.getInstance().getReference().child("FeelStory")
                .child(users.getUserId())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        try {
                            mAuth = FirebaseAuth.getInstance();

                            if(snapshot.child(mAuth.getUid()).child("laugh").getValue().toString().equals("true")){
                                holder.btlaughred.setVisibility(View.VISIBLE);
                                holder.btlaugh.setVisibility(View.GONE);

                                holder.btsaid.setEnabled(false);
                                holder.btlike.setEnabled(false);
                            }


                        } catch (Exception e )  {      holder.textStory.setText(" "); }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });




        Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                FirebaseDatabase.getInstance().getReference().child("Story")
                        .child(users.getUserId())

                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                try {
                                    mAuth = FirebaseAuth.getInstance();

                                    holder.txtlaugh.setText(snapshot.child("laugh").getValue().toString());
                                    holder.txttsaid.setText(snapshot.child("said").getValue().toString());
                                    holder.like.setText(snapshot.child("like").getValue().toString());
                                    holder.textStory.setText(snapshot.child("text").getValue().toString());
                                    Picasso.get().load(snapshot.child("profilePic").getValue().toString()).into(holder.image2);

                                } catch (Exception e )  {      holder.textStory.setText(" "); }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }
                        });
                mHandler.postDelayed(this, 5000);
            }
        }, 5000);


//        FirebaseDatabase.getInstance().getReference().child("Story")
//                .child(users.getUserId())
//
//                .addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        try {
//                            mAuth = FirebaseAuth.getInstance();
//
//                            holder.textStory.setText(snapshot.child("text").getValue().toString());
//                            Picasso.get().load(snapshot.child("profilePic").getValue().toString()).into(holder.image2);
//
//                        } catch (Exception e )  {      holder.textStory.setText(" "); }
//                    }
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//                    }
//                });

        holder.btlike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseDatabase.getInstance().getReference().child("FeelStory")
                        .child(users.getUserId())

                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                database = FirebaseDatabase.getInstance();
                                try {


                                    mAuth = FirebaseAuth.getInstance();
                                    HashMap<String, Object> obj3 = new HashMap<>();
                                    obj3.put("like" , "true" );
                                    database.getReference().child("FeelStory").child(users.getUserId()).child(mAuth.getUid())
                                            .updateChildren(obj3);

                                    holder.likered.setVisibility(View.GONE);
                                    holder.btlike.setVisibility(View.VISIBLE);

                                    // hide again
                                    holder.btlaugh.setEnabled(false);
                                    holder.btsaid.setEnabled(false);


                                } catch (Exception e )  {      holder.textStory.setText(" "); }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                FirebaseDatabase.getInstance().getReference().child("Story")
                        .child(users.getUserId())
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                database = FirebaseDatabase.getInstance();
                                try {
                              String a =  snapshot.child("like").getValue().toString();
                                int b =    Integer.parseInt(a);
                                b = b + 1;
                                    HashMap<String, Object> obj2 = new HashMap<>();
                                    obj2.put("like", b );
                                    database.getReference().child("Story").child(users.getUserId())
                                            .updateChildren(obj2);
//
//                                    mAuth = FirebaseAuth.getInstance();
//                                    HashMap<String, Object> obj3 = new HashMap<>();
//                                    obj3.put("like" , "true" );
//                                    database.getReference().child("Story").child("RoomFeel").child(users.getUserId()).child(mAuth.getUid())
//                                            .updateChildren(obj3)
//                                            ;

                                    holder.btlike.setVisibility(View.GONE);
                                    holder.likered.setVisibility(View.VISIBLE);

                                } catch (Exception e )  {      holder.textStory.setText(" "); }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
            }
        });

        holder.likered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseDatabase.getInstance().getReference().child("FeelStory")
                        .child(users.getUserId())

                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                database = FirebaseDatabase.getInstance();
                                try {


                                    mAuth = FirebaseAuth.getInstance();
                                    HashMap<String, Object> obj3 = new HashMap<>();
                                    obj3.put("like" , "false" );
                                    database.getReference().child("FeelStory").child(users.getUserId()).child(mAuth.getUid())
                                            .updateChildren(obj3);

                                    holder.likered.setVisibility(View.GONE);
                                    holder.btlike.setVisibility(View.VISIBLE);

                                    // show again
                                    holder.btlaugh.setEnabled(true);
                                    holder.btsaid.setEnabled(true);

                                } catch (Exception e )  {      holder.textStory.setText(" "); }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                FirebaseDatabase.getInstance().getReference().child("Story")
                        .child(users.getUserId())

                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                database = FirebaseDatabase.getInstance();
                                try {
                                    String a =  snapshot.child("like").getValue().toString();
                                    int b =    Integer.parseInt(a);
                                    b = b - 1;
                                    HashMap<String, Object> obj2 = new HashMap<>();
                                    obj2.put("like", b );
                                    database.getReference().child("Story").child(users.getUserId())
                                            .updateChildren(obj2);

//                                    mAuth = FirebaseAuth.getInstance();
//                                    HashMap<String, Object> obj3 = new HashMap<>();
//                                    obj3.put("like" , "false" );
//                                    database.getReference().child("Story").child("RoomFeel").child(users.getUserId()).child(mAuth.getUid())
//                                            .updateChildren(obj3);
                                    holder.btlike.setVisibility(View.VISIBLE);
                                    holder.likered.setVisibility(View.GONE);

                                } catch (Exception e )  {      holder.textStory.setText(" "); }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
            }
        });

        holder.btlaugh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseDatabase.getInstance().getReference().child("FeelStory")
                        .child(users.getUserId())

                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                database = FirebaseDatabase.getInstance();
                                try {


                                    mAuth = FirebaseAuth.getInstance();
                                    HashMap<String, Object> obj3 = new HashMap<>();
                                    obj3.put("laugh" , "true" );
                                    database.getReference().child("FeelStory").child(users.getUserId()).child(mAuth.getUid())
                                            .updateChildren(obj3);

                                    holder.likered.setVisibility(View.GONE);
                                    holder.btlike.setVisibility(View.VISIBLE);

                                    // hide again
                                    holder.btlike.setEnabled(false);
                                    holder.btsaid.setEnabled(false);


                                } catch (Exception e )  {      holder.textStory.setText(" "); }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                FirebaseDatabase.getInstance().getReference().child("Story")
                        .child(users.getUserId())

                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                database = FirebaseDatabase.getInstance();
                                try {
                                    String a =  snapshot.child("laugh").getValue().toString();
                                    int b =    Integer.parseInt(a);
                                    b = b + 1;
                                    HashMap<String, Object> obj2 = new HashMap<>();
                                    obj2.put("laugh", b );
                                    database.getReference().child("Story").child(users.getUserId())
                                            .updateChildren(obj2);


                                    holder.btlaugh.setVisibility(View.GONE);
                                    holder.btlaughred.setVisibility(View.VISIBLE);


                                } catch (Exception e )  {      holder.textStory.setText(" "); }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

            }
        });

        holder.btlaughred.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseDatabase.getInstance().getReference().child("FeelStory")
                        .child(users.getUserId())

                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                database = FirebaseDatabase.getInstance();
                                try {
                                    mAuth = FirebaseAuth.getInstance();
                                    HashMap<String, Object> obj3 = new HashMap<>();
                                    obj3.put("laugh" , "flase" );
                                    database.getReference().child("FeelStory").child(users.getUserId()).child(mAuth.getUid())
                                            .updateChildren(obj3);
                                    holder.likered.setVisibility(View.GONE);
                                    holder.btlike.setVisibility(View.VISIBLE);
                                    // hide again
                                    holder.btlike.setEnabled(true);
                                    holder.btsaid.setEnabled(true);
                                } catch (Exception e )  {      holder.textStory.setText(" "); }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }
                        });

                FirebaseDatabase.getInstance().getReference().child("Story")
                        .child(users.getUserId())
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                database = FirebaseDatabase.getInstance();
                                try {
                                    String a =  snapshot.child("laugh").getValue().toString();
                                    int b =    Integer.parseInt(a);
                                    b = b - 1;
                                    HashMap<String, Object> obj2 = new HashMap<>();
                                    obj2.put("laugh", b );
                                    database.getReference().child("Story").child(users.getUserId())
                                            .updateChildren(obj2);

//                                    mAuth = FirebaseAuth.getInstance();
//                                    HashMap<String, Object> obj3 = new HashMap<>();
//                                    obj3.put("laugh", "true" );
//                                    database.getReference().child("Story").child("roomLike").child(users.getUserId() + mAuth.getUid())
//                                            .updateChildren(obj3);
                                    holder.btlaugh.setVisibility(View.VISIBLE);
                                    holder.btlaughred.setVisibility(View.GONE);
                                    //set laugh = true to block if user click again
//                                    mAuth = FirebaseAuth.getInstance();
//                                    HashMap<String, Object> obj3 = new HashMap<>();
//                                    obj3.put("laugh" , "false" );
//                                    database.getReference().child("Story").child("RoomFeel").child(users.getUserId()).child(mAuth.getUid())
//                                            .updateChildren(obj3);
                                } catch (Exception e )  {      holder.textStory.setText(" "); }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

            }
        });

        holder.btsaid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // set button enabled = false when chose
                FirebaseDatabase.getInstance().getReference().child("FeelStory")
                        .child(users.getUserId())

                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                database = FirebaseDatabase.getInstance();
                                try {


                                    mAuth = FirebaseAuth.getInstance();
                                    HashMap<String, Object> obj3 = new HashMap<>();
                                    obj3.put("said" , "true" );
                                    database.getReference().child("FeelStory").child(users.getUserId()).child(mAuth.getUid())
                                            .updateChildren(obj3);

                                    holder.likered.setVisibility(View.GONE);
                                    holder.btlike.setVisibility(View.VISIBLE);

                                    // hide again
                                    holder.btlaugh.setEnabled(false);
                                    holder.btlike.setEnabled(false);


                                } catch (Exception e )  {      holder.textStory.setText(" "); }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    // check data and ++
                FirebaseDatabase.getInstance().getReference().child("Story")
                        .child(users.getUserId())

                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                database = FirebaseDatabase.getInstance();
                                try {
                                    String a =  snapshot.child("said").getValue().toString();
                                    int b =    Integer.parseInt(a);
                                    b = b + 1;
                                    HashMap<String, Object> obj2 = new HashMap<>();
                                    obj2.put("said", b );
                                    database.getReference().child("Story").child(users.getUserId())
                                            .updateChildren(obj2);

//                                    mAuth = FirebaseAuth.getInstance();
//                                    HashMap<String, Object> obj3 = new HashMap<>();
//                                    obj3.put("said" , "true" );
//                                    database.getReference().child("Story").child("RoomFeel").child(users.getUserId()).child(mAuth.getUid())
//                                            .updateChildren(obj3);

                                    holder.btsaid.setVisibility(View.GONE);
                                    holder.btsaidred.setVisibility(View.VISIBLE);

                                } catch (Exception e )  {      holder.textStory.setText(" "); }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }
                        });
            }
        });

        holder.btsaidred.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // set button enabled = true when don't chose anything
                FirebaseDatabase.getInstance().getReference().child("FeelStory")
                        .child(users.getUserId())

                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                database = FirebaseDatabase.getInstance();
                                try {
                                    mAuth = FirebaseAuth.getInstance();
                                    HashMap<String, Object> obj3 = new HashMap<>();
                                    obj3.put("said" , "false" );
                                    database.getReference().child("FeelStory").child(users.getUserId()).child(mAuth.getUid())
                                            .updateChildren(obj3);
                                    holder.likered.setVisibility(View.GONE);
                                    holder.btlike.setVisibility(View.VISIBLE);

                                    // hide again
                                    holder.btlaugh.setEnabled(true);
                                    holder.btlike.setEnabled(true);

                                } catch (Exception e )  {      holder.textStory.setText(" "); }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                FirebaseDatabase.getInstance().getReference().child("Story")
                        .child(users.getUserId())

                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                database = FirebaseDatabase.getInstance();
                                try {
                                    String a =  snapshot.child("said").getValue().toString();
                                    int b =    Integer.parseInt(a);
                                    b = b - 1;
                                    HashMap<String, Object> obj2 = new HashMap<>();
                                    obj2.put("said", b );
                                    database.getReference().child("Story").child(users.getUserId())
                                            .updateChildren(obj2);
//                                    mAuth = FirebaseAuth.getInstance();
//                                    HashMap<String, Object> obj3 = new HashMap<>();
//                                    obj3.put("said", "false" );
//                                    database.getReference().child("Story").child("RoomFeel").child(users.getUserId() + mAuth.getUid())
//                                            .updateChildren(obj3);
                                    holder.btsaid.setVisibility(View.VISIBLE);
                                    holder.btsaidred.setVisibility(View.GONE);

                                } catch (Exception e )  {      holder.textStory.setText(" "); }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
            }
        });

        holder.btchat.setOnClickListener(new View.OnClickListener() {
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
        ImageView image,image2 , btchat, btlike,btlaugh, btsaid , likered, btsaidred, btlaughred;
        TextView userName, textStory, like,  txtlaugh, txttsaid ;
        ImageButton check;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            like = itemView.findViewById(R.id.txt_show_like);
            image = itemView.findViewById(R.id.profile_image);
            userName = itemView.findViewById(R.id.userNameList);
            textStory = itemView.findViewById(R.id.text_story);
            check = itemView.findViewById(R.id.online_offline);
            image2 =  itemView.findViewById(R.id.image_story);
            btchat = itemView.findViewById(R.id.btn_chat);
            btlike =  itemView.findViewById(R.id.btn_like);
            btlaugh = itemView.findViewById(R.id.btn_verylaugh);
            btsaid = itemView.findViewById(R.id.btn_verysaid);
            txtlaugh = itemView.findViewById(R.id.txt_show_laugh);
            txttsaid = itemView.findViewById(R.id.txt_show_said);
            likered = itemView.findViewById(R.id.btn_like_red);
            btsaidred = itemView.findViewById(R.id.btn_verysaid_red);
            btlaughred = itemView.findViewById(R.id.btn_verylaugh_red);

        }
    }
}

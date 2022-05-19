package com.Hackthon.botshop.AdapterModels;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.Hackthon.botshop.ChatDetailsActivity;
import com.Hackthon.botshop.Models.Users;
import com.Hackthon.botshop.PopUpWindow;
import com.Hackthon.botshop.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder>{

    private static String LOG_TAG = UserAdapter.class.getSimpleName();
    ArrayList<Users> list;
    Context context;

    public UserAdapter(ArrayList<Users> list, Context context) {
        this.list = list;
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.show_users,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Users user = list.get(position);
        Picasso.get().load(user.getProfilePic()).placeholder(R.drawable.user_profile_img).into(holder.profilePic);
        holder.userName.setText(user.getName());

        /*if(user.getStatus().equals("Online")){
            holder.img_online.setVisibility(View.VISIBLE);
        }else {
            holder.img_online.setVisibility(View.GONE);
        }*/

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, ChatDetailsActivity.class);
                i.putExtra("UserId",user.getUserId());
                i.putExtra("profilePic",user.getProfilePic());
                i.putExtra("userName",user.getName());
                i.putExtra("emailId",user.getEmailId());
                i.putExtra("status",user.getStatus());
                context.startActivity(i);
            }
        });

        /*FirebaseDatabase.getInstance().getReference().child("Presence").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    String key = dataSnapshot.getKey();
                    if(key.equals(user.getUserId())){
                        Log.i(LOG_TAG,"ViewBindHOLDeer: "+user.getUserId());
                        user.setStatus(true);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        if(user.getStatus()){
            holder.img_online.setVisibility(View.VISIBLE);
        }

        Log.i(LOG_TAG,"Status: "+user.getStatus()+", user was : "+user.getName());*/

        /*if(isChat){
            if(user.getStatus().equals("Online")){
                holder.img_online.setVisibility(View.VISIBLE);
            }else {
                holder.img_online.setVisibility(View.GONE);
            }
        }*/

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

         ImageView profilePic;
         TextView  userName;
        // ImageView img_online;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            profilePic = itemView.findViewById(R.id.chatDetailsProfilePic);
            userName = itemView.findViewById(R.id.userNameChats);
            //img_online = itemView.findViewById(R.id.img_online);

        }

    }

}

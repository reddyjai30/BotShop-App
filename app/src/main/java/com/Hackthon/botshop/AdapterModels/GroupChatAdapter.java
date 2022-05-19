package com.Hackthon.botshop.AdapterModels;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.Hackthon.botshop.Models.AllMethods;
import com.Hackthon.botshop.Models.GroupMessage;
import com.Hackthon.botshop.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

public class GroupChatAdapter extends RecyclerView.Adapter<GroupChatAdapter.GroupChatAdapterViewHolder> {

    List<GroupMessage> messages;
    Context context;
    DatabaseReference reference;

    int SENDER_VIEW_TYPE = 1;
    int RECEIVER_VIEW_TYPE = 2;

    public GroupChatAdapter(Context context, List<GroupMessage> messages, DatabaseReference reference) {
        this.messages = messages;
        this.context = context;
        this.reference = reference;
    }


    @NonNull
    @Override
    public GroupChatAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if(viewType==SENDER_VIEW_TYPE) {
            View view = LayoutInflater.from(context).inflate(R.layout.sample_senders, parent, false);
            return new GroupChatAdapterViewHolder(view);
        }else {
            View view = LayoutInflater.from(context).inflate(R.layout.sample_receiver,parent,false);
            return new GroupChatAdapterViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull GroupChatAdapterViewHolder holder, int position) {
        GroupMessage message = messages.get(position);
        if (holder.getItemViewType()==SENDER_VIEW_TYPE) {
            holder.tvTitle.setText("YOU: " + message.getMessage());
            holder.tvTitle.setGravity(Gravity.START);
            holder.time.setVisibility(View.GONE);
            /*holder.constraintLayout.setBackgroundColor(Color.parseColor("#ADD8E6"));*/
        } else {
            holder.tvTitleRec.setText(message.getName() + ": " + message.getMessage());
            holder.tvTitleRec.setGravity(Gravity.START);
            holder.timeRec.setVisibility(View.GONE);
            //holder.constraintLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));

        }
    }

    @Override
    public int getItemViewType(int position) {

        if (messages.get(position).getName().equals(AllMethods.name)) {
            return SENDER_VIEW_TYPE;
        } else {
            return RECEIVER_VIEW_TYPE;
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class GroupChatAdapterViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle;
        ConstraintLayout constraintLayout;
        TextView tvTitleRec;
        ConstraintLayout constraintLayoutRec;
        TextView time;
        TextView timeRec;

        public GroupChatAdapterViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.sender_textview);
            constraintLayout = itemView.findViewById(R.id.sender_constraint_Layout);
            tvTitleRec = itemView.findViewById(R.id.reciever_textviewGroup);
            constraintLayoutRec = itemView.findViewById(R.id.receiver_constraint_layout);
            time = itemView.findViewById(R.id.sendersTimeGroup);
            timeRec = itemView.findViewById(R.id.receiverTimeGroup);


        }
    }


}

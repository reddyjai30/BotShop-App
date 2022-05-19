package com.Hackthon.botshop;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.Hackthon.botshop.AdapterModels.ChatAdapter;
import com.Hackthon.botshop.Models.MessagesModels;
import com.Hackthon.botshop.databinding.ActivityChatDetailsBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ChatDetailsActivity extends AppCompatActivity {

    private ActivityChatDetailsBinding binding;
    FirebaseDatabase database;
    FirebaseAuth auth;
    DatabaseReference reference;
    FirebaseStorage storage;
    ProgressDialog progressDialog;
    String senderRoom;
    String receiverRoom;
    String senderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

         senderId = auth.getUid();
         String receiverId = getIntent().getStringExtra("UserId");
        Log.i("ChatDetailsActivity",receiverId);
        String userName = getIntent().getStringExtra("userName");
        String profilePic = getIntent().getStringExtra("profilePic");
        String email = getIntent().getStringExtra("emailId");
        String status = getIntent().getStringExtra("status");

        storage = FirebaseStorage.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Uploading image...");
        progressDialog.setCancelable(false);  //so that when we click side it does not get close

        binding.chatDetailsUserName.setText(userName);
        Picasso.get().load(profilePic).placeholder(R.drawable.user_profile_img).into(binding.chatDetailsProfilePic);

        binding.chatbackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ChatDetailsActivity.this, IndividualChatsDomain.class);
                startActivity(i);
            }
        });




        binding.attachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setAction(Intent.ACTION_GET_CONTENT);
                i.setType("image/*");
                startActivityForResult(i,25);
            }
        });

        binding.descriptionPopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ChatDetailsActivity.this,PopUpWindow.class);
                i.putExtra("UserId",receiverId);
                i.putExtra("profilePic",profilePic);
                i.putExtra("userName",userName);
                i.putExtra("emailId",email);
                i.putExtra("status",status);
                startActivity(i);
            }
        });

        final ArrayList<MessagesModels> messagesModels = new ArrayList<>();
        final ChatAdapter chatAdapter = new ChatAdapter(messagesModels,this);
        binding.chatRecyclerView.setAdapter(chatAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.chatRecyclerView.setLayoutManager(layoutManager);

       senderRoom = senderId + receiverId;
       receiverRoom = receiverId + senderId;


        database.getReference().child("Chats").child(senderRoom)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        messagesModels.clear();
                        for(DataSnapshot snapshot1: snapshot.getChildren()){
                            MessagesModels models = snapshot1.getValue(MessagesModels.class);
                            messagesModels.add(models);
                        }
                        chatAdapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        database.getReference().child("presence").child(receiverId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String status = snapshot.getValue(String.class);
                    if(status.equals("Online")){
                             binding.txtOnline.setVisibility(View.VISIBLE);
                    }else{
                              binding.txtOnline.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


                binding.sendArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String message = binding.sendMessageEdittext.getText().toString();
                final MessagesModels messagesModels1 = new MessagesModels(senderId,message);
                messagesModels1.setTimestamp(new Date().getTime());
                binding.sendMessageEdittext.setText("");

                database.getReference().child("Chats")
                        .child(senderRoom)
                        .push()
                        .setValue(messagesModels1).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        database.getReference().child("Chats")
                                .child(receiverRoom)
                                .push()
                                .setValue(messagesModels1).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {

                            }
                        });

                    }
                });

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 25){
            if(data!=null){
                if(data.getData()!=null){
                    Uri selectedImage = data.getData();
                    Calendar calendar = Calendar.getInstance();
                    StorageReference reference = storage.getReference().child("chats").child(calendar.getTimeInMillis()+"");
                    progressDialog.show();
                    reference.putFile(selectedImage).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            progressDialog.dismiss();
                            if(task.isSuccessful()){
                                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {

                                        String filePath = uri.toString();

                                        String message = binding.sendMessageEdittext.getText().toString();
                                        final MessagesModels messagesModels1 = new MessagesModels(senderId,message);
                                        messagesModels1.setTimestamp(new Date().getTime());
                                        messagesModels1.setMessage("photo");
                                        messagesModels1.setImageUrl(filePath);
                                        binding.sendMessageEdittext.setText("");

                                        database.getReference().child("Chats")
                                                .child(senderRoom)
                                                .push()
                                                .setValue(messagesModels1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {

                                                database.getReference().child("Chats")
                                                        .child(receiverRoom)
                                                        .push()
                                                        .setValue(messagesModels1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {

                                                    }
                                                });

                                            }
                                        });
                                        Toast.makeText(ChatDetailsActivity.this,filePath,Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    });
                }
            }
        }

    }
}
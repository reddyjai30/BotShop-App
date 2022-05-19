package com.Hackthon.botshop;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.Hackthon.botshop.AdapterModels.GroupChatAdapter;
import com.Hackthon.botshop.Models.AllMethods;
import com.Hackthon.botshop.Models.GroupMessage;
import com.Hackthon.botshop.Models.Users;
import com.Hackthon.botshop.databinding.ActivitySportsBinding;
import com.Hackthon.botshop.databinding.ActivityTechDomainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class TechDomain extends AppCompatActivity {

    ActivityTechDomainBinding binding;
    FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference reference;
    List<GroupMessage> list;
    Users users;
    GroupChatAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTechDomainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        list = new ArrayList<>();
        users = new Users();

        getSupportActionBar().hide();
        binding.chatDetailsUserName.setText("Technology");

        binding.chatbackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TechDomain.this, DomainSections.class);
                startActivity(i);
            }
        });

        final FirebaseUser firebaseUser = auth.getCurrentUser();
        users.setUserId(firebaseUser.getUid());
        users.setEmailId(firebaseUser.getEmail());

        database.getReference("Users").child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                users = snapshot.getValue(Users.class);
                users.setUserId(firebaseUser.getUid());
                AllMethods.name = users.getName();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        reference = database.getReference("messagesTech");
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                GroupMessage message = snapshot.getValue(GroupMessage.class);
                message.setKey(snapshot.getKey());
                list.add(message);
                displayMessages(list);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                GroupMessage message = snapshot.getValue(GroupMessage.class);
                message.setKey(snapshot.getKey());
                List<GroupMessage> l = new ArrayList<>();
                for (GroupMessage m : list) {
                    if (m.getKey().equals(message.getKey())) {
                        l.add(message);
                    } else {
                        l.add(m);
                    }
                }
                list = l;
                displayMessages(list);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                GroupMessage message = snapshot.getValue(GroupMessage.class);
                message.setKey(snapshot.getKey());
                List<GroupMessage> l = new ArrayList<>();
                for (GroupMessage m : list) {
                    if (m.getKey().equals(message.getKey())) {
                        l.add(m);
                    }
                }
                list = l;
                displayMessages(list);
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        binding.sendArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(binding.sendMessageEdittext.toString())){
                    GroupMessage message = new GroupMessage(binding.sendMessageEdittext.getText().toString(), users.getName());
                    binding.sendMessageEdittext.setText("");
                    reference.push().setValue(message);
                }else {
                    Toast.makeText(getApplicationContext(),"Blank Messages are not allowed",Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private void displayMessages(List<GroupMessage> list) {

        binding.chatRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new GroupChatAdapter(TechDomain.this, list, reference);
        binding.chatRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    @Override
    protected void onResume() {
        list = new ArrayList<>();
        super.onResume();
    }
}
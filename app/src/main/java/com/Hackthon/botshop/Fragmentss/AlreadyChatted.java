package com.Hackthon.botshop.Fragmentss;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.Hackthon.botshop.AdapterModels.UserAdapter;
import com.Hackthon.botshop.Models.Users;
import com.Hackthon.botshop.databinding.FragmentChatsBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class AlreadyChatted extends Fragment {

    private static String LOG_TAG = AlreadyChatted.class.getSimpleName();

    public AlreadyChatted() {
        // Required empty public constructor
    }

    FragmentChatsBinding binding;
    private UserAdapter userAdapter;
    ArrayList<Users> list = new ArrayList<>();

    FirebaseUser fUser;
    FirebaseDatabase database;
    FirebaseAuth auth;
    DatabaseReference reference;
    private List<String> userList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentChatsBinding.inflate(inflater, container, false);
        database = FirebaseDatabase.getInstance();

         userAdapter = new UserAdapter(list,getContext());
        binding.chatRecyclerView.setAdapter(userAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        binding.chatRecyclerView.setLayoutManager(layoutManager);


        fUser = FirebaseAuth.getInstance().getCurrentUser();
        userList = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Users users = dataSnapshot.getValue(Users.class);
                    users.setUserId(dataSnapshot.getKey());
                    String room = users.getUserId()+fUser.getUid();
                    Log.i(LOG_TAG,"room: "+room);
                    FirebaseDatabase.getInstance().getReference().child("Chats").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                         for (DataSnapshot dataSnapshot1: snapshot.getChildren()){
                             if(dataSnapshot1.getKey().equals(room)){
                                 Log.i(LOG_TAG,"added User: "+users.getUserId()+" with room: "+room);
                                 if(!list.contains(users)){
                                     list.add(users);
                                 }
                             }
                         }
                            userAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

         /*
        reference = FirebaseDatabase.getInstance().getReference();
        reference.child("Presence").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    String key = dataSnapshot.getKey();
                    for (Users user: list){
                        if(key.equals(user.getUserId())){
                            Log.i(LOG_TAG,"Presence of: "+user.getName());
                            user.setStatus(true);
                            break;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/

        return binding.getRoot();

    }


}
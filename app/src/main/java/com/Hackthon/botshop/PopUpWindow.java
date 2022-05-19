package com.Hackthon.botshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.WindowManager;

import com.Hackthon.botshop.Models.Users;
import com.Hackthon.botshop.databinding.ActivityChatDetailsBinding;
import com.Hackthon.botshop.databinding.ActivityPopUpWindowBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.auth.User;
import com.squareup.picasso.Picasso;

public class PopUpWindow extends AppCompatActivity {

    ActivityPopUpWindowBinding binding;
    FirebaseDatabase database;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPopUpWindowBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*0.8),(int)(height*0.8));

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.x = 0;
        params.y = -20;
        getWindow().setAttributes(params);

        String receiverId = getIntent().getStringExtra("UserId");
        String userName = getIntent().getStringExtra("userName");
        String email = getIntent().getStringExtra("emailId");
        String status = getIntent().getStringExtra("status");

        Log.i("POPUPwindow",userName+"");
        String profilePic = getIntent().getStringExtra("profilePic");
        Picasso.get().load(profilePic).placeholder(R.drawable.user_profile_img).into(binding.profileImgPoput);
        binding.namePopup.setText(userName);
        binding.description.setText(status);
        binding.email.setText(email);

    }


}
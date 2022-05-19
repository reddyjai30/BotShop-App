package com.Hackthon.botshop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DomainSections extends AppCompatActivity {

    CardView fashion;
    CardView sports;
    CardView individual;
    CardView tech;
    CardView chatBot;
    CardView user;

    FirebaseUser firebaseUser;
    DatabaseReference refer;
    FirebaseDatabase database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_domain_sections);

        individual = findViewById(R.id.indiviual_chat_domain);
        sports = findViewById(R.id.sports_domain);
        fashion = findViewById(R.id.fashion_domain);
        tech = findViewById(R.id.tech_domain);
        chatBot = findViewById(R.id.chat_bot_domain);
        user = findViewById(R.id.user_domain);

        database = FirebaseDatabase.getInstance();


        chatBot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DomainSections.this,ChatBot.class);
                startActivity(i);
            }
        });

        sports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DomainSections.this,Sports.class);
                startActivity(i);
            }
        });

        tech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DomainSections.this,TechDomain.class);
                startActivity(i);
            }
        });

        fashion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DomainSections.this, FashionDomain.class);
                startActivity(i);
            }
        });

        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DomainSections.this,EditProfile.class);
                startActivity(i);
            }
        });

        individual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DomainSections.this, IndividualChatsDomain.class);
                startActivity(i);
            }
        });

    }



    /*private void status(String status){

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        refer = FirebaseDatabase.getInstance().getReference("Users");
        refer.child(firebaseUser.getUid());
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("status",status);

        refer.updateChildren(hashMap);
    }

    @Override
    protected void onResume() {
        super.onResume();
        status("Online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        status("Offline");
    }*/

    /*@Override
    protected void onStart() {
        super.onStart();
        String currId = FirebaseAuth.getInstance().getUid();
        database = FirebaseDatabase.getInstance();
        database.getReference().child("Presence").child(currId).setValue("Online");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        String currId = FirebaseAuth.getInstance().getUid();
        database = FirebaseDatabase.getInstance();
        database.getReference().child("Presence").child(currId).setValue("Offline");
    } */

    @Override
    public void onBackPressed() {
        finishAffinity();
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        String currentId = FirebaseAuth.getInstance().getUid();
        database.getReference().child("presence").child(currentId).setValue("Online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        String currentId = FirebaseAuth.getInstance().getUid();
        database.getReference().child("presence").child(currentId).setValue("Offline");
    }


}
package com.Hackthon.botshop;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.Hackthon.botshop.Models.MessagesModels;
import com.Hackthon.botshop.Models.Users;
import com.Hackthon.botshop.databinding.ActivityChatDetailsBinding;
import com.Hackthon.botshop.databinding.ActivityEditProfileBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class EditProfile extends AppCompatActivity {

    //Profile
    ActivityEditProfileBinding binding;
    FirebaseStorage storage;
    FirebaseAuth auth;
    FirebaseDatabase database;
    ProgressDialog progressDialog;
    ImageView userProfileImage;
    FirebaseUser firebaseUser;
    Uri selectedImage;
    private static String LOG_TAG = EditProfile.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(EditProfile.this, DomainSections.class);
                startActivity(i);
            }
        });

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Uploading image...");
        progressDialog.setCancelable(false);  //so that when we click side it does not get close
        storage = FirebaseStorage.getInstance();
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        // userProfileImage = findViewById(R.id.userProfile_profilePhoto);


        binding.signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                Intent i = new Intent(EditProfile.this,MainActivity.class);
                startActivity(i);
            }
        });

        database.getReference().child("Users").child(FirebaseAuth.getInstance().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Users users = snapshot.getValue(Users.class);
                        Picasso.get().load(users.getProfilePic()).placeholder(R.drawable.user_profile_img).into(binding.editProfilePhoto);
                        binding.email.setText(users.getEmailId());
                        binding.description.setText(users.getStatus());
                        binding.username.setText(users.getName());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        binding.addCircle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setAction(Intent.ACTION_GET_CONTENT);
                i.setType("image/*");
                startActivityForResult(i, 33);
            }
        });

        binding.saveInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = binding.editProfileUserName.getText().toString();
                String status = binding.editProfileStatus.getText().toString();

                if(!status.isEmpty()&&name.isEmpty()&&selectedImage==null) {

                    String uid = auth.getCurrentUser().getUid();
                    Users user = new Users();
                    user.setStatus(status);
                    database.getReference().child("Users").child(FirebaseAuth.getInstance().getUid())
                            .child("status").setValue(user.getStatus()).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(EditProfile.this, "Status updated", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else if(!name.isEmpty()&&status.isEmpty()&&selectedImage==null){
                    String uid = auth.getCurrentUser().getUid();
                    Users user = new Users();
                    user.setName(name);
                    database.getReference().child("Users").child(FirebaseAuth.getInstance().getUid())
                            .child("name").setValue(user.getName()).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(EditProfile.this,"user name updated",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else if(name.isEmpty()&&status.isEmpty()) {
                    binding.editProfileUserName.setError("Please type a name");
                    binding.editProfileStatus.setError("Please type a name");
                    return;
                }
                else if(selectedImage != null) {
                    StorageReference reference = storage.getReference().child("Profiles").child(auth.getUid());
                    reference.putFile(selectedImage).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if(task.isSuccessful()) {
                                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String imageUrl = uri.toString();

                                        String uid = auth.getCurrentUser().getUid();
                                        String name = binding.editProfileUserName.getText().toString();
                                        String status = binding.editProfileStatus.getText().toString();

                                        Users user = new Users();
                                        user.setProfilePic(imageUrl);
                                        user.setName(name);
                                        user.setStatus(status);

                                        Log.i(LOG_TAG,"created the user info");
                                        database.getReference().child("Users").child(FirebaseAuth.getInstance().getUid())
                                                .child("profilePic").setValue(user.getProfilePic()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(EditProfile.this,"Profile Updated successfully",Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                        database.getReference().child("Users").child(FirebaseAuth.getInstance().getUid())
                                                .child("name").setValue(user.getName()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(EditProfile.this,"user name updated",Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                        database.getReference().child("Users").child(FirebaseAuth.getInstance().getUid())
                                                .child("status").setValue(user.getStatus()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(EditProfile.this,"Status updated",Toast.LENGTH_SHORT).show();
                                            }
                                        });

                                    }
                                });
                            }
                        }
                    });
                }

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

            if(data!=null){
                if(data.getData()!=null){
                    Uri uri = data.getData(); // filepath
                    FirebaseStorage storage = FirebaseStorage.getInstance();
                    long time = new Date().getTime();
                    StorageReference reference = storage.getReference().child("Profiles").child(time+"");
                    reference.putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if(task.isSuccessful()) {
                                Log.i(LOG_TAG,"image storage download");
                                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String filePath = uri.toString();
                                        HashMap<String, Object> obj = new HashMap<>();
                                        obj.put("image", filePath);
                                        database.getReference().child("users")
                                                .child(FirebaseAuth.getInstance().getUid())
                                                .updateChildren(obj).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {

                                            }
                                        });
                                    }
                                });
                            }
                        }
                    });


                    binding.editProfilePhoto.setImageURI(data.getData());
                    selectedImage = data.getData();
                }
            }

        }

   }
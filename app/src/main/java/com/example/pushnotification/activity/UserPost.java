package com.example.pushnotification.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.pushnotification.R;
import com.example.pushnotification.adapter.HomeAdater;
import com.example.pushnotification.model.AddImage;
import com.example.pushnotification.model.Categories;
import com.example.pushnotification.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserPost extends AppCompatActivity {

    TextView name, bname, like, feedstory;
    ImageView feed, heart, red_heart, save,delete;
    CircleImageView profile;
    FirebaseAuth firebaseAuth;
    String userId;
    User user;
    Categories categories;
    AddImage addImage;
    Toolbar toolbar;
    String userlocalid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_post);

        Initialazationdmodulefornavigation();
        InitU();
        firebaseAuth = FirebaseAuth.getInstance();
        userId = firebaseAuth.getCurrentUser().getUid();
        retriveImage();
        retriveName();
        Intent intent = getIntent();
        addImage = intent.getParcelableExtra("key");
        Glide.with(this).load(addImage.getAddimageurl()).thumbnail(0.5f)
                .diskCacheStrategy(DiskCacheStrategy.ALL).into(feed);
        feedstory.setText(addImage.getAddimageFeed());

    }

    private void Initialazationdmodulefornavigation() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Post");
    }

    private void InitU() {

        profile = findViewById(R.id.imageView5);
        heart = findViewById(R.id.imageView7);
        red_heart = findViewById(R.id.imageView8);
        save = findViewById(R.id.imageView9);
        feed = findViewById(R.id.imageView6);
        name = findViewById(R.id.textView2);
        like = findViewById(R.id.textView4);
        bname = findViewById(R.id.textView5);
        feedstory = findViewById(R.id.textView6);
        delete=findViewById(R.id.imageView3);
        delete.setVisibility(View.VISIBLE);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetDialog bottomSheetDialog=new BottomSheetDialog();
                bottomSheetDialog.deletepost(addImage);
                bottomSheetDialog.show(getSupportFragmentManager(), "ModalBottomSheet");
            }
        });
    }



    private void retriveName() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("User").child(userId);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user = snapshot.getValue(User.class);
                name.setText(user.getUserName());
                bname.setText(user.getUserName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UserPost.this, "Faid to show userName", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void retriveImage() {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Categories").child(userId);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    categories = snapshot.getValue(Categories.class);
                    Glide.with(UserPost.this).load(categories.getCategoryImage()).into(profile);
//              Glide.with(context).load(snapshot.child("categoryImage").getValue().toString()).into(holder.profile);
                } else {
                    Toast.makeText(UserPost.this, "Error db", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UserPost.this, "Failed load image", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
package com.example.pushnotification.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.pushnotification.R;
import com.example.pushnotification.model.AddImage;
import com.example.pushnotification.model.Categories;
import com.example.pushnotification.model.Save;
import com.example.pushnotification.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import de.hdodenhof.circleimageview.CircleImageView;

public class SavePost extends AppCompatActivity {

    TextView name, bname, like, feedstory;
    ImageView feed, heart, red_heart, save,delete;
    CircleImageView profile;
    FirebaseAuth firebaseAuth;
    String userId;
    String localid;
    Toolbar toolbar;
    Save saves;
    int count=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_post);

        Intent intent = getIntent();
        saves = intent.getParcelableExtra("SaveKey");

        Initialazationdmodulefornavigation();
        InitU();
        firebaseAuth = FirebaseAuth.getInstance();
        userId = firebaseAuth.getCurrentUser().getUid();
        localid=saves.getSaveID();

        Glide.with(this).load(saves.getSaveProfile()).thumbnail(0.5f)
                .diskCacheStrategy(DiskCacheStrategy.ALL).into(feed);
        Glide.with(this).load(saves.getSaveFeedimge()).thumbnail(0.5f)
                .diskCacheStrategy(DiskCacheStrategy.ALL).into(profile);
        feedstory.setText(saves.getSaveText());
        like.setText(saves.getSaveLike());
        bname.setText(saves.getSaveName());
        name.setText(saves.getSaveuserName());

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
        delete = findViewById(R.id.saveDark);
        delete.setVisibility(View.VISIBLE);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Save").child(userId);
                databaseReference.child(localid).removeValue();
                save.setVisibility(View.VISIBLE);
                Toast.makeText(SavePost.this, "Delete", Toast.LENGTH_SHORT).show();
            }
        });

        heart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count = count + 1;
                like.setText(String.valueOf(count));
                red_heart.setVisibility(View.VISIBLE);
                heart.setVisibility(View.INVISIBLE);
            }
        });

        red_heart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count = count - 1;
                like.setText(String.valueOf(count));
                heart.setVisibility(View.VISIBLE);
                red_heart.setVisibility(View.INVISIBLE);
            }
        });
    }
}

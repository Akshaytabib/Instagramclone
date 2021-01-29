package com.example.pushnotification.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.example.pushnotification.R;
import com.example.pushnotification.adapter.FindFriendAdapter;
import com.example.pushnotification.adapter.SaveAdapter;
import com.example.pushnotification.model.Save;
import com.example.pushnotification.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class FindFriend extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<User> arrayList;
    FindFriendAdapter findFriendAdapter;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    String userid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_friend);


        databaseReference= FirebaseDatabase.getInstance().getReference("User");
        firebaseAuth=FirebaseAuth.getInstance();
        userid=firebaseAuth.getCurrentUser().getUid();


        recyclerView=findViewById(R.id.recyclerView3);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);


        arrayList=new ArrayList<User>();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User data = snapshot.getValue(User.class);
                arrayList.add(data);
                findFriendAdapter = new FindFriendAdapter(FindFriend.this, arrayList);
                recyclerView.setAdapter(findFriendAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(FindFriend.this, ""+ error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
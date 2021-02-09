package com.example.pushnotification.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.pushnotification.R;
import com.example.pushnotification.adapter.ProfileAdapter;
import com.example.pushnotification.adapter.SaveAdapter;
import com.example.pushnotification.model.Save;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class SaveData extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<Save> arrayList;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    SaveAdapter saveAdapter;
    String userid;
    ProfileAdapter profileAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save);

        if(isConnected()) {

            firebaseAuth = FirebaseAuth.getInstance();
            userid = firebaseAuth.getCurrentUser().getUid();
            databaseReference = FirebaseDatabase.getInstance().getReference("Save").child(userid);

            recyclerView = findViewById(R.id.recyclerView2);
            GridLayoutManager manager = new GridLayoutManager(SaveData.this, 3, GridLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(manager);
            recyclerView.setHasFixedSize(true);
            arrayList = new ArrayList<Save>();
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Save data = dataSnapshot.getValue(Save.class);
                        arrayList.add(data);
                        Collections.reverse(arrayList);
                        saveAdapter = new SaveAdapter(SaveData.this, arrayList);
                        recyclerView.setAdapter(saveAdapter);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(SaveData.this, "Opps something went to wrong", Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            Toast.makeText(this, "No Internet COnnection", Toast.LENGTH_SHORT).show();
        }

    }

    public boolean isConnected() {
        boolean connected = false;
        try {
            ConnectivityManager cm = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo nInfo = cm.getActiveNetworkInfo();
            connected = nInfo != null && nInfo.isAvailable() && nInfo.isConnected();
            return connected;
        } catch (Exception e) {
            Log.e("Connectivity Exception", e.getMessage());
        }
        return connected;
    }
}
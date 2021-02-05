package com.example.pushnotification.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.pushnotification.R;
import com.example.pushnotification.activity.FindFriend;
import com.example.pushnotification.model.Categories;
import com.example.pushnotification.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class FindFriendAdapter extends RecyclerView.Adapter<FindFriendAdapter.ViewHolder> {

    Context context;
    ArrayList<User> arrayList;
    Categories categories;
    public FindFriendAdapter(FindFriend findFriend, ArrayList<User> arrayList) {
        this.context=findFriend;
        this.arrayList=arrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.find_friend_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        User user=arrayList.get(position);
        holder.textView.setText(user.getUserName());
        retriveImage(holder);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        CircleImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView=(itemView).findViewById(R.id.textView13);
            imageView=(itemView).findViewById(R.id.cropImageView);
        }
    }


    private void retriveImage(ViewHolder holder) {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Categories");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    categories = snapshot.getValue(Categories.class);
//                    Glide.with(context).load(categories.getCategoryImage()).into(holder.imageView);
                } else {
                    Toast.makeText(context, "Error db", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "Failed load image", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
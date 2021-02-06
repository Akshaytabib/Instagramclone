package com.example.pushnotification.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.pushnotification.R;
import com.example.pushnotification.activity.HomeDemo;
import com.example.pushnotification.model.AddImage;
import com.example.pushnotification.model.Categories;
import com.example.pushnotification.model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeAdater extends RecyclerView.Adapter<HomeAdater.ViewHolder> {
    Context context;
    ArrayList<AddImage> arrayList;
    FirebaseAuth firebaseAuth;
    String userId;
    User user;
    //    ArrayList<Categories> categories;
    Categories categories;
    AddImage addImage;
    int count = 0;

    public HomeAdater(FragmentActivity activity, ArrayList<AddImage> arrayList) {
        this.context = activity;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.homeitem_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        addImage = arrayList.get(position);
        Glide.with(context).load(addImage.getAddimageurl()).into(holder.feed);
        holder.feedstory.setText(addImage.getAddimageFeed());
        firebaseAuth = FirebaseAuth.getInstance();
        userId = firebaseAuth.getCurrentUser().getUid();
        retriveImage(holder);
        retriveName(holder);

    }


    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView name, bname, like, feedstory;
        ImageView feed, heart, red_heart, save,darkSave;
        CircleImageView profile;
        String profiledata = "";
        int feeddata;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profile = (itemView).findViewById(R.id.imageView5);
            heart = (itemView).findViewById(R.id.imageView7);
            red_heart = (itemView).findViewById(R.id.imageView8);
            save = (itemView).findViewById(R.id.imageView9);
            feed = (itemView).findViewById(R.id.imageView6);
            name = (itemView).findViewById(R.id.textView2);
            like = (itemView).findViewById(R.id.textView4);
            bname = (itemView).findViewById(R.id.textView5);
            feedstory = (itemView).findViewById(R.id.textView6);
            darkSave = (itemView).findViewById(R.id.saveDark);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            final int position = getAdapterPosition();
            Toast.makeText(context, "Position" + position, Toast.LENGTH_SHORT).show();

            heart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    count = count + 1;
                    like.setVisibility(View.VISIBLE);
                    like.setText(String.valueOf(count));
                    red_heart.setVisibility(View.VISIBLE);
                    heart.setVisibility(View.INVISIBLE);

                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Like").child(userId);
                    String userlocalid = databaseReference.push().getKey();

                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("LikeID",userlocalid);
                    hashMap.put("LikeName", like.getText().toString());
                    databaseReference.child(userlocalid).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            darkSave.setVisibility(View.VISIBLE);
                            Toast.makeText(context, "Successful", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });

            red_heart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    count = count - 1;
                    like.setVisibility(View.VISIBLE);
                    like.setText(String.valueOf(count));
                    heart.setVisibility(View.VISIBLE);
                    red_heart.setVisibility(View.INVISIBLE);
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Like").child(userId);
                    databaseReference.child("SaveID").removeValue();

                }
            });

            save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Save").child(userId);
                    String userlocalid = databaseReference.push().getKey();

                    addImage = arrayList.get(position);
                    String profile = addImage.getAddimageurl();
                    String feedimage = categories.getCategoryImage();
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("SaveID",userlocalid);
                    hashMap.put("saveName", name.getText().toString());
                    hashMap.put("saveProfile", profile);
                    hashMap.put("saveFeedimge", feedimage);
                    hashMap.put("saveLike", like.getText().toString());
                    hashMap.put("saveuserName", bname.getText().toString());
                    hashMap.put("saveText", feedstory.getText().toString());

                    databaseReference.child(userlocalid).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            darkSave.setVisibility(View.VISIBLE);
                            Toast.makeText(context, "Successful", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });

            darkSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

//                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Save").child(userId);
//                    databaseReference.child("SaveID").removeValue();
                    save.setVisibility(View.VISIBLE);
//                    Toast.makeText(context, "Delete", Toast.LENGTH_SHORT).show();

                }
            });
        }
    }

    private void retriveName(final ViewHolder holder) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("User").child(userId);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user = snapshot.getValue(User.class);
                holder.name.setText(user.getUserName());
                holder.bname.setText(user.getUserName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "Faid to show userName", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void retriveImage(final ViewHolder holder) {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Categories").child(userId);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    categories = snapshot.getValue(Categories.class);
                    Glide.with(context).load(categories.getCategoryImage()).into(holder.profile);
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

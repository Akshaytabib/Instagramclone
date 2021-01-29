package com.example.pushnotification.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.pushnotification.R;
import com.example.pushnotification.activity.UserPost;
import com.example.pushnotification.model.AddImage;

import java.util.ArrayList;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ViewHolder> {

    Context context;
    ArrayList<AddImage> arrayList;
    public ProfileAdapter(FragmentActivity activity, ArrayList<AddImage> arrayList) {
        this.context=activity;
        this.arrayList=arrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.griditem_layout, parent, false);
        return  new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AddImage addImage=arrayList.get(position);

        Glide.with(context).load(addImage.getAddimageurl()).thumbnail(0.5f)
                .diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=(itemView).findViewById(R.id.imageView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            int postion = getAdapterPosition();
            Toast.makeText(context, "postion"+postion, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(context , UserPost.class);
            intent.putExtra("key", (Parcelable) arrayList.get(postion));
            context.startActivity(intent);

        }
    }
}

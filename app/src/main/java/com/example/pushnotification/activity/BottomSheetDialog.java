package com.example.pushnotification.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.pushnotification.R;
import com.example.pushnotification.model.AddImage;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


public class BottomSheetDialog extends BottomSheetDialogFragment {

    TextView delete, share;
    FirebaseAuth firebaseAuth;
    String userid;
    String userlocalid,image,feed,sFeed;
    DatabaseReference databaseReference;
    ArrayList<AddImage> addImages;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable
            ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.bottomsheetlayout,
                container, false);

        delete = v.findViewById(R.id.textView3);
        share = v.findViewById(R.id.textView7);
        firebaseAuth = FirebaseAuth.getInstance();
        userid = firebaseAuth.getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference("AddImage").child(userid);
        addImages=new ArrayList<AddImage>();

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference.child(userlocalid).removeValue();
                Toast.makeText(getActivity(), "Delete", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getActivity(),HomeDemo.class));
                dismiss();
            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(Intent.ACTION_SEND);
                myIntent.setType("text/plain");
                myIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(image));
                myIntent.putExtra(Intent.EXTRA_TEXT,feed);
                startActivity(Intent.createChooser(myIntent, "Share Using"));
                Toast.makeText(getActivity(), "Shared", Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });
        return v;
    }

    public void deletepost(AddImage addImage) {
        userlocalid=addImage.getAddimageId();
        image=addImage.getAddimageurl();
        feed=addImage.getAddimageFeed();
    }
}
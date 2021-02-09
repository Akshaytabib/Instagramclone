package com.example.pushnotification.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.pushnotification.R;
import com.example.pushnotification.activity.EditProfile;
import com.example.pushnotification.adapter.ProfileAdapter;
import com.example.pushnotification.model.AddImage;
import com.example.pushnotification.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    RecyclerView recyclerView;

    CircleImageView profile;
    Button editprofile;
    TextView name;

    private ProfileAdapter profileAdapter;
    ArrayList<AddImage> arrayList;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    String userid = "";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {

    }

    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.nav_items, menu);
//        menu.findItem(R.id.first).setVisible(false);
        menu.findItem(R.id.second).setVisible(false);
        menu.findItem(R.id.third).setVisible(true);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
//        if(id == R.id.second){
//            Toast.makeText(getActivity(), "SaveFragments", Toast.LENGTH_SHORT).show();
//            startActivity(new Intent(getContext(), SaveFragment.class));
//        }
//
//        if (id == R.id.third) {
//            Toast.makeText(getActivity(), "LOGOUt", Toast.LENGTH_SHORT).show();
//        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        if (isConnected()) {

            profile = (view).findViewById(R.id.imageView11);
            editprofile = (view).findViewById(R.id.button2);
            name = (view).findViewById(R.id.textView8);
            recyclerView = (view).findViewById(R.id.recyclerView);
            GridLayoutManager manager = new GridLayoutManager(getActivity(), 3, GridLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(manager);
            recyclerView.setHasFixedSize(true);
            arrayList = new ArrayList<AddImage>();
            firebaseAuth = FirebaseAuth.getInstance();
            userid = firebaseAuth.getCurrentUser().getUid();
            databaseReference = FirebaseDatabase.getInstance().getReference("AddImage").child(userid);
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        AddImage data = dataSnapshot.getValue(AddImage.class);
                        arrayList.add(data);
                        profileAdapter = new ProfileAdapter(getActivity(), arrayList);
                        recyclerView.setAdapter(profileAdapter);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getActivity(), "Opps something went to wrong", Toast.LENGTH_SHORT).show();
                }
            });

            retriveName();
            retriveImage();
        } else {
            Toast.makeText(getContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
        }

        editprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), EditProfile.class));
            }
        });

        return view;
    }

    private void retriveName() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("User").child(userid);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User data = snapshot.getValue(User.class);
                name.setText(data.getUserName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Faid to show userName", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void retriveImage() {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Categories").child(userid);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Glide.with(getActivity()).load(snapshot.child("categoryImage").getValue().toString()).into(profile);
                } else {
                    Toast.makeText(getActivity(), "update your profile photo", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Failed load image", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public boolean isConnected() {
        boolean connected = false;
        try {
            ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo nInfo = cm.getActiveNetworkInfo();
            connected = nInfo != null && nInfo.isAvailable() && nInfo.isConnected();
            return connected;
        } catch (Exception e) {
            Log.e("Connectivity Exception", e.getMessage());
        }
        return connected;
    }

}
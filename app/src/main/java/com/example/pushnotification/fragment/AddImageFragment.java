package com.example.pushnotification.fragment;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.pushnotification.R;
import com.example.pushnotification.activity.HomeDemo;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddImageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddImageFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    //
    ImageView Iback, Inext;
    ImageView  Iaddimgs;
    TextInputLayout comment;
    FirebaseAuth firebaseAuth;
    String userlocalid;

    DatabaseReference databaseReference;
    StorageTask uploadTask;
    StorageReference storageReference;

    Uri imageUri;
    String myUrl = "";
    String userId="";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AddImageFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static AddImageFragment newInstance(String param1, String param2) {
        AddImageFragment fragment = new AddImageFragment();
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
        inflater.inflate(R.menu.nav_items,menu);
//        menu.findItem(R.id.first).setVisible(false);
        menu.findItem(R.id.second).setVisible(true);
        menu.findItem(R.id.third).setVisible(false);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id=item.getItemId();
        if(id == R.id.second){
            Uploadimg();
            Toast.makeText(getActivity(), "Hello", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_image, container, false);
//        Iback = (view).findViewById(R.id.imageView3);
//        Inext = (view).findViewById(R.id.imageView4);
        Iaddimgs = (view).findViewById(R.id.imageView2);
        comment = (view).findViewById(R.id.editTextTextPersonName3);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("AddImage");
        userlocalid= databaseReference.push().getKey();
        userId=FirebaseAuth.getInstance().getCurrentUser().getUid();
        storageReference = FirebaseStorage.getInstance().getReference("AddImage");

        Iaddimgs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CropImage.activity().
                        setAspectRatio(1, 1)
                        .start( getContext(),AddImageFragment.this);
            }
        });

        return view;
    }

    private void Uploadimg() {
        if (imageUri != null) {
            final StorageReference reference = storageReference.child(userId + "." + getExtension(imageUri));
            uploadTask = reference.putFile(imageUri);
            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if (!task.isComplete()) {
                        task.getException();
                    }
                    return reference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {

                    if (task.isSuccessful()) {
                        Uri downloaduri = (Uri) task.getResult();
                        myUrl = downloaduri.toString();
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("AddImage");

                        String name = comment.getEditText().getText().toString();

                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("addimageId",userlocalid);
                        hashMap.put("addimageurl", myUrl);
                        hashMap.put("addimageFeed", name);

                        databaseReference.child(userId).child(userlocalid).setValue(hashMap);

                        startActivity(new Intent(getContext(), HomeDemo.class));

                    } else {
                        Toast.makeText(getContext(), "Failed!", Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), "Failed" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(getContext(), "No Image Selected", Toast.LENGTH_SHORT).show();
        }
    }

    private String getExtension(Uri uri) {
        ContentResolver resolver = getActivity().getContentResolver();
        MimeTypeMap map = MimeTypeMap.getSingleton();
        return map.getExtensionFromMimeType(resolver.getType(uri));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                imageUri = result.getUri();
                Iaddimgs.setImageURI(imageUri);
            }
        } else {
            Toast.makeText(getContext(), "An Error Ocurred!", Toast.LENGTH_SHORT).show();
        }
    }
}
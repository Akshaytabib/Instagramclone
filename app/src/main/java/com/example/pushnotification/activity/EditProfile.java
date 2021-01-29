package com.example.pushnotification.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.pushnotification.R;
import com.example.pushnotification.fragment.ProfileFragment;
import com.example.pushnotification.model.AddImage;
import com.example.pushnotification.model.ProfileName;
import com.example.pushnotification.model.User;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.internal.cache.DiskLruCache;

public class EditProfile extends AppCompatActivity {
    ImageView back, next;
    CircleImageView profile;
    TextView changeprofile;
    TextInputEditText name;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    StorageReference storageReference;
    String userId;
    Uri imageUri;
    String myUrl = "";
    StorageTask uploadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        InitUi();
        firebaseAuth = FirebaseAuth.getInstance();
        userId = firebaseAuth.getCurrentUser().getUid();
        storageReference = FirebaseStorage.getInstance().getReference("ProfileImage");
        retriveName();
        retriveImage();

    }

    private void InitUi() {
        back = findViewById(R.id.imageView12);
        next = findViewById(R.id.imageView13);
        profile = findViewById(R.id.imageView14);
        changeprofile = findViewById(R.id.textView10);
        name = findViewById(R.id.editTextTextPersonName7);

        changeprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CropImage.activity().
                        setAspectRatio(1, 1)
                        .start(EditProfile.this);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(EditProfile.this, HomeDemo.class));
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                storeImage();
                StoreNeame();
                startActivity(new Intent(EditProfile.this, HomeDemo.class));
            }
        });
    }

    private void retriveImage() {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Categories").child(userId);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Glide.with(EditProfile.this).load(snapshot.child("categoryImage").getValue().toString()).into(profile);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(EditProfile.this, "Failed load image", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void retriveName() {
        databaseReference = FirebaseDatabase.getInstance().getReference("User").child(userId);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    User data = snapshot.getValue(User.class);
                    name.setText(data.getUserName());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void StoreNeame() {
        databaseReference = FirebaseDatabase.getInstance().getReference("User").child(userId);
        String username = name.getText().toString();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("userName", username);
        databaseReference.updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(EditProfile.this, "Sucessful", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void storeImage() {

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
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Categories").child(userId);


                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("categoryImage", myUrl);

                    databaseReference.setValue(hashMap);

                } else {
                    Toast.makeText(EditProfile.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                next.setVisibility(View.VISIBLE);
                Toast.makeText(EditProfile.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private String getExtension(Uri uri) {
        ContentResolver resolver = getContentResolver();
        MimeTypeMap map = MimeTypeMap.getSingleton();
        return map.getExtensionFromMimeType(resolver.getType(uri));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            //CropImage.ActivityResult result = CropImage.getActivityResult(data);
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                imageUri = result.getUri();
                profile.setImageURI(imageUri);
            }
        } else {
            Toast.makeText(this, "An Error Ocurred!", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

}
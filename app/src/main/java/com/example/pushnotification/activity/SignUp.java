package com.example.pushnotification.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.pushnotification.R;
import com.example.pushnotification.fragment.HomeFragment;
import com.example.pushnotification.model.User;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.widget.Toast.LENGTH_SHORT;

public class SignUp extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    TextInputLayout name, email, password, mobilenumber;
    Button next;
    String userId;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    String pattern = "^\\s*(?:\\+?(\\d{1,3}))?[-. (]*(\\d{3})[-. )]*(\\d{3})[-. ]*(\\d{4})(?: *x(\\d+))?\\s*$";
    Matcher m;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("User");

        init();

    }

    private void init() {

        name = findViewById(R.id.editTextTextPersonName4);
        email = findViewById(R.id.editTextTextPersonName5);
        password = findViewById(R.id.editTextTextPersonName6);
        mobilenumber = findViewById(R.id.editTextTextPersonName12);
        next = findViewById(R.id.btnone);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String username = name.getEditText().getText().toString().trim();
                final String useremail = email.getEditText().getText().toString().trim();
                final String userpassword = password.getEditText().getText().toString().trim();
                final String usermobilenumber = mobilenumber.getEditText().getText().toString().trim();
                firebaseAuth.createUserWithEmailAndPassword(useremail, userpassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Pattern r = Pattern.compile(pattern);
                        m = r.matcher(usermobilenumber);
                        if(username.isEmpty()){
                            Toast.makeText(SignUp.this, "enter userName", LENGTH_SHORT).show();
                        }else if(useremail.isEmpty()){
                            Toast.makeText(SignUp.this, "enter EmailAddress", LENGTH_SHORT).show();
                        }else if(!useremail.matches(emailPattern)){
                            Toast.makeText(SignUp.this, "Enter correct emailid", LENGTH_SHORT).show();
                        }else if(userpassword.isEmpty()){
                            Toast.makeText(SignUp.this, "enter Password", LENGTH_SHORT).show();
                        }else if(usermobilenumber.isEmpty()) {
                            Toast.makeText(SignUp.this, "enter mobile number", LENGTH_SHORT).show();
                        }else if(!m.find()){
                            Toast.makeText(SignUp.this, "enter 10 digit Mobile number", LENGTH_SHORT).show();
                        } else if (task.isSuccessful()) {
                            Toast.makeText(SignUp.this, "Successful", LENGTH_SHORT).show();
                            userId = firebaseAuth.getCurrentUser().getUid();

                            HashMap<String, Object> hashMap = new HashMap<>();
                            hashMap.put("userid", userId);
                            hashMap.put("userName", username);
                            hashMap.put("userEmail", useremail);
                            hashMap.put("userPasswrd", userpassword);
                            hashMap.put("usermobilenumber", usermobilenumber);

                            databaseReference.child(userId).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    startActivity(new Intent(SignUp.this, HomeFragment.class));
                                    Toast.makeText(SignUp.this, "user register", LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            Toast.makeText(SignUp.this, "Unsucessful", LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SignUp.this, "Failed", LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
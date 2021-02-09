package com.example.pushnotification.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pushnotification.R;
import com.example.pushnotification.fragment.HomeFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    TextInputLayout name, password;
    TextView textView, forgetpassword;
    Button next;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    String username,passwords;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if (isConnected()) {
            initFireabse();
            init();
            firebaseUser = firebaseAuth.getCurrentUser();

            if (firebaseUser != null) {
                Intent intent = new Intent(MainActivity.this, HomeDemo.class);
                startActivity(intent);
            }
        } else {
            Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
        }

    }

    private void initFireabse() {
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("User");
    }

    private void init() {

        name = findViewById(R.id.editTextTextPersonName);
        password = findViewById(R.id.editTextTextPersonName2);
        textView = findViewById(R.id.textView);
        forgetpassword = findViewById(R.id.textView14);
        next = findViewById(R.id.button);

        forgetpassword.setVisibility(View.INVISIBLE);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, SignUp.class));
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isConnected()) {
                    save();
                } else {
                    Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void save() {

        username=name.getEditText().getText().toString().trim();
        passwords= password.getEditText().getText().toString();

       if( validateLogin()) {

           firebaseAuth.signInWithEmailAndPassword(username, passwords).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
               @Override
               public void onComplete(@NonNull Task<AuthResult> task) {
                   if (task.isSuccessful()) {
                       Intent intent = new Intent(MainActivity.this, HomeDemo.class);
                       startActivity(intent);
                   }
               }
           });
       }else {
           Toast.makeText(this, "enter email and password", Toast.LENGTH_SHORT).show();
       }

    }

    public boolean validateLogin() {
        if(username.isEmpty()){
            Toast.makeText(this, "enter email id", Toast.LENGTH_SHORT).show();
            return false;
        } else if(!username.matches(emailPattern)){
            Toast.makeText(this, "enter valid email id", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(passwords.isEmpty()){
            Toast.makeText(this, "enter password", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
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

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        if(firebaseUser != null){
            startActivity(new Intent(MainActivity.this,HomeDemo.class));
        }
    }
}
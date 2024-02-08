package com.example.eventplanningapp;

import static android.text.TextUtils.isEmpty;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Register_page extends AppCompatActivity {

    private EditText userName, loginEmail, phoneNumber, password;
    private Button signUpButton;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private CheckBox Verfied;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);

        // Initialize Firebase Auth and Firestore
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // UI Components
        userName = findViewById(R.id.UserName);
        loginEmail = findViewById(R.id.LoginEmail);
        phoneNumber = findViewById(R.id.ConfirmPassword); // Assuming ConfirmPassword is used for phoneNumber based on your layout
        password = findViewById(R.id.Password);
        signUpButton = findViewById(R.id.Login_LoginPage);
        Verfied = findViewById(R.id.rememberMe);

        // Sign Up button listener
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUpUser();
            }
        });
    }

    private void signUpUser() {
        String userName = this.userName.getText().toString().trim();
        String email = this.loginEmail.getText().toString().trim();
        String password = this.password.getText().toString().trim();
        String number = this.phoneNumber.getText().toString().trim();
        boolean verfied = this.Verfied.isChecked();

        if (userName.isEmpty() || email.isEmpty() || password.isEmpty() || number.isEmpty()) {
            Toast.makeText(Register_page.this, "Please fill all fields.", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        User user = new User(userName, email, password, number, false,verfied);
                        FirebaseFirestore.getInstance().collection("users").document(email).set(user);
                        Intent intent = new Intent(Register_page.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                        /*
                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
                        if (firebaseUser != null) {
                            String userId = firebaseUser.getUid();
                            Map<String, Object> user = new HashMap<>();
                            user.put("userName", userName);
                            user.put("email", email);
                            user.put("phoneNumber", number);
                            user.put("rememberMe_flage", false);
                            user.put("verified_flage", Verfied);

                            db.collection("users").document(userId) // Using userId as the document ID
                                    .set(user)
                                    .addOnSuccessListener(aVoid -> Toast.makeText(Register_page.this, "User registered successfully.", Toast.LENGTH_SHORT).show())
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(Register_page.this, "Error adding user: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        Log.d("FirestoreError", e.getMessage()); // Log the error
                                    });
                        }*/
                    } else {
                        Toast.makeText(Register_page.this, "Authentication failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
 /*   protected void onStart() {
        super.onStart();
        ArrayList<User> UserList = new ArrayList<>();

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isEmpty(userName.getText())||isEmpty(UserEmail.getText())||isEmpty(userPassword.getText())){
                    Toast.makeText(Register_page.this, "Please enter all data required", Toast.LENGTH_LONG).show();
                }else{
                    User user=new User(userName.getText().toString(),
                            UserEmail.getText().toString(),userPassword.getText().toString(),UserNumber.getText().toString(),false);
                    UserList.add(user);
                    SharedPreferences sharedPreferences= getSharedPreferences("sharedPreferences",MODE_PRIVATE);
                    SharedPreferences.Editor editor=sharedPreferences.edit();
                    Gson gson = new Gson();
                    String json =gson.toJson(UserList);
                    editor.putString("UserList",json);
                    editor.apply();
                    Intent intent=new Intent(Register_page.this,LoginPage.class);
                    startActivity(intent);



                }
            }
        });

    }*/
}